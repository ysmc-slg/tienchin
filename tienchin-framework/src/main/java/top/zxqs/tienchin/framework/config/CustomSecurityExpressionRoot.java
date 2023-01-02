package top.zxqs.tienchin.framework.config;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * 自定义权限表达式
 * @author: zxq
 * @create: 2022-08-21 14:33
 */
public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * 是否具备某个权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if(antPathMatcher.match(authority.getAuthority(),permission)){
                return true;
            }
        }
        return  false;
    }

    /**
     * 是否具备多个权限中的任意一个权限
     * @param permissions
     * @return
     */
    public boolean hasAnyPermissions(String... permissions){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(permissions == null || permissions.length == 0){
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            for (String permission : permissions) {
                if(antPathMatcher.match(authority.getAuthority(),permission)){
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
