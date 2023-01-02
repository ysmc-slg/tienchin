import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      /**
       * 保存了本地的公共路由和服务端返回的动态路由（菜单），这个 routes 将来主要是在两个地方使用:
       * 1。首页搜索时使用
       * 2,。tagview 中使用
       * 
       * 从 routes 的使用场景中可以看到，routes 变量实际上并不涉及到页面渲染，单纯的只是处理页面的逻辑
       * 
       *  
       */
      routes: [],
      // 暂无使用场景，可以删除
      addRoutes: [],
      /**
       * 相当于是动态路由的备份，也可以理解为是完整的 sidebarRouters 的一个备份，无论什么情况 defaultRoutes 都表示 1--n 级菜单
       * 
       */
      defaultRoutes: [],
      /**
       * 如果开启了顶部导航菜单，那么相关的渲染就是有 topbarRouters 来完成
       */
      topbarRouters: [],
      /**
       * 左侧导航栏，正常来说会参与左侧菜单的渲染，默认也是完整的 1--n 级菜单，但是如果开启了顶部菜单，那么这个变量就是 2--n 级菜单
       * 
       */
      sidebarRouters: []
    }),
    actions: {
      setRoutes(routes) {
        this.addRoutes = routes
        this.routes = constantRoutes.concat(routes)
      },
      setDefaultRoutes(routes) {
        this.defaultRoutes = constantRoutes.concat(routes)
      },
      setTopbarRoutes(routes) {
        this.topbarRouters = routes
      },
      setSidebarRouters(routes) {
        this.sidebarRouters = routes
      },
      generateRoutes(roles) {
        return new Promise(resolve => {
          // 向后端请求路由数据
          getRouters().then(res => {
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            const defaultData = JSON.parse(JSON.stringify(res.data))

            const sidebarRoutes = filterAsyncRouter(sdata)
            // 这是一个重写后的 routers，这个重写后的 routers 主要是将 3-n 级菜单统统变为 2 级菜单
            const rewriteRoutes = filterAsyncRouter(rdata, false, true)
            // 这个变量保存的是服务端返回的动态 JSON 中的 component 字符串变为了对象
            const defaultRoutes = filterAsyncRouter(defaultData)
            // 动态路由，根据用户权限查询出本地定义的可用的动态路由，这些动态路由都不涉及到菜单渲染，所以查到之后直接遍历，放到 router 对象中即可
            const asyncRoutes = filterDynamicRoutes(dynamicRoutes)

            asyncRoutes.forEach(route => {
               router.addRoute(route) 
            })

            // 这个实际行是前端公共路由和服务端返回的动态路由，赋值给routes
            this.setRoutes(rewriteRoutes)
            // 左侧菜单栏，这个也是公共路由+sidebarRoutes
            this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
            // 这个中永远保存的是最完整的菜单项数据，所以也是 公共路由+sidebarRoutes，只不过是在 set方法中加的
            this.setDefaultRoutes(sidebarRoutes)
            // 这个是顶部菜单数据源 
            this.setTopbarRoutes(defaultRoutes)
            resolve(rewriteRoutes)
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    } 
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

/**
 * 根据当前登录用户的权限，过滤出本地定义的动态路由中，有哪些是可用的
 * @param {} routes 
 * @returns 
 */
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  let res;
  for (const path in modules) {
    const dir = path.split('views/')[1].split('.vue')[0];
    if (dir === view) {
      res = () => modules[path]();
    }
  }
  return res;
}

export default usePermissionStore
