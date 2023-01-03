<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
         <el-form-item label="角色名称" prop="roleName">
            <el-input
               v-model="queryParams.roleName"
               placeholder="请输入角色名称"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="权限字符" prop="roleKey">
            <el-input
               v-model="queryParams.roleKey"
               placeholder="请输入权限字符"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select
               v-model="queryParams.status"
               placeholder="角色状态"
               clearable
               style="width: 240px"
            >
               <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="创建时间" style="width: 308px">
            <el-date-picker
               v-model="dateRange"
               value-format="YYYY-MM-DD"
               type="daterange"
               range-separator="-"
               start-placeholder="开始日期"
               end-placeholder="结束日期"
            ></el-date-picker>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
               v-hasPermi="['tienchin:channel:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['tienchin:channel:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['tienchin:channel:delete']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['tienchin:channel:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="channelList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="渠道编号" prop="channelId" width="120" />
         <el-table-column label="渠道名称" prop="channelName" :show-overflow-tooltip="true" width="150" />
         <el-table-column label="渠道状态" prop="status" :show-overflow-tooltip="true" width="150">
          <template  #default="scope">
            <template v-for="(status,indexi) in channel_status" :key="indexi">
              <el-tag v-if="status.value == scope.row.status" :type="status.elTagType">{{status.label}}</el-tag>
            </template>
          
          </template>
         </el-table-column>
         <el-table-column label="渠道类型" prop="type" width="100">
          <template  #default="scope">
            <template v-for="(type,indexi) in channel_type" :key="indexi">
              <el-tag v-if="type.value == scope.row.type" :type="type.elTagType">{{type.label}}</el-tag>
            </template>
          
          </template>
         </el-table-column>
         <el-table-column label="创建人" prop="createBy" width="100" />
         <el-table-column label="创建时间" prop="createTime" :show-overflow-tooltip="true" width="100" />
         <el-table-column label="备注" prop="remark" :show-overflow-tooltip="true" width="100" />
         
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top" v-if="scope.row.roleId !== 1">
                <el-button
                  type="text"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['tienchin:channel:edit']"
                ></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top" v-if="scope.row.roleId !== 1">
                <el-button
                  type="text"
                  icon="Delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['tienchin:channel:delete']"
                ></el-button>
              </el-tooltip>
            </template>
         </el-table-column>
      </el-table>

      <pagination
         v-show="total > 0"
         :total="total"
         v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize"
         @pagination="getList"
      />

      <!-- 添加或修改角色配置对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
         <el-form ref="channelRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="渠道名称" prop="channelName">
               <el-input v-model="form.channelName" placeholder="请输入渠道名称" />
            </el-form-item>
            
            <el-form-item label="渠道状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio
                     v-for="status in channel_status"
                     :key="status.value"
                     :label="status.value"
                  >{{ status.label }}</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="渠道类型" prop="type">
               <el-radio-group v-model="form.type">
                  <el-radio
                     v-for="type in channel_type"
                     :key="type.value"
                     :label="type.value"
                  >{{ type.label }}</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>

   </div>
</template>

<script setup name="Role">
import { addRole, changeRoleStatus, dataScope, delRole, getRole, listRole, updateRole, deptTreeSelect } from "@/api/system/role";
import {listChannel,addChannel,getChannel,updateChannel} from "@/api/tienchin/channel/channel";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable,channel_type,channel_status } = proxy.useDict("sys_normal_disable","channel_type","channel_status");

const channelList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const deptOptions = ref([]);
const openDataScope = ref(false);
const menuRef = ref(null);
const deptRef = ref(null);

/** 数据范围选项*/
const dataScopeOptions = ref([
  { value: "1", label: "全部数据权限" },
  { value: "2", label: "自定数据权限" },
  { value: "3", label: "本部门数据权限" },
  { value: "4", label: "本部门及以下数据权限" },
  { value: "5", label: "仅本人数据权限" }
]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    roleName: undefined,
    roleKey: undefined,
    status: undefined
  },
  rules: {
    channelName: [{ required: true, message: "渠道名称不能为空", trigger: "blur" }],
    status: [{required: true, message: "渠道状态不能为空", trigger: "blur"}],
    type: [{required: true, message: "渠道类型不能为空", trigger: "blur"}]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询渠道列表 */
function getList() {
  loading.value = true;
  listChannel(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    channelList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const roleIds = row.roleId || ids.value;
  proxy.$modal.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项?').then(function () {
    return delRole(roleIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/role/export", {
    ...queryParams.value,
  }, `role_${new Date().getTime()}.xlsx`);
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.channelId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 角色状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用";
  proxy.$modal.confirm('确认要"' + text + '""' + row.roleName + '"角色吗?').then(function () {
    return changeRoleStatus(row.roleId, row.status);
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功");
  }).catch(function () {
    row.status = row.status === "0" ? "1" : "0";
  });
}


/** 重置新增的表单以及其他数据  */
function reset() {
  if (menuRef.value != undefined) {
    menuRef.value.setCheckedKeys([]);
  }
  form.value = {
    channelId: undefined,
    channelName: undefined,
    status: "1",
    type:1,
    remark: undefined
  };
  proxy.resetForm("channelRef");
}
/** 添加渠道 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加渠道";
}
/** 表格修改按钮 */
function handleUpdate(row) {
  reset();
  const channelId = row.channelId || ids.value;
  console.log(ids.value)
  getChannel(channelId).then(response => {
    form.value = response.data;
    // form.value.roleSort = Number(form.value.roleSort);
    open.value = true;
    // nextTick(() => {
    //   roleMenu.then((res) => {
    //     let checkedKeys = res.checkedKeys;
    //     checkedKeys.forEach((v) => {
    //       nextTick(() => {
    //         menuRef.value.setChecked(v, true, false);
    //       });
    //     });
    //   });
    // });
    title.value = "修改渠道";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["channelRef"].validate(valid => {
    if (valid) {
      if (form.value.channelId != undefined) {
        updateChannel(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        // form.value.menuIds = getMenuAllCheckedKeys();
        addChannel(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 选择角色权限范围触发 */
function dataScopeSelectChange(value) {
  if (value !== "2") {
    deptRef.value.setCheckedKeys([]);
  }
}

/** 取消按钮（数据权限）*/
function cancelDataScope() {
  openDataScope.value = false;
  reset();
}

getList();
</script>
