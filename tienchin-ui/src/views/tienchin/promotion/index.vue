<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
         <el-form-item label="渠道名称" prop="channelName">
            <el-input
               v-model="queryParams.channelName"
               placeholder="请输入渠道名称"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="渠道类型" prop="type">
            <el-select
               v-model="queryParams.type"
               placeholder="渠道类型"
               clearable
               style="width: 240px"
            >
               <el-option
                  v-for="dict in channel_type"
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
            type="info"
            plain
            icon="Upload"
            @click="handleImport"
            v-hasPermi="['tienchin:channel:import']"
          >导入</el-button>
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
      <el-table v-loading="loading" :data="channelList" @selection-change="handleSelectionChange" style="width: 100%" >
         <el-table-column type="selection" width="55" align="center"  />
         <el-table-column label="活动编号" prop="promotionId"  v-if="false" />
         <el-table-column label="活动名称" prop="name" :show-overflow-tooltip="true"  />
         <el-table-column label="渠道编号" prop="channelId" :show-overflow-tooltip="true"  v-if="false" />
         <el-table-column label="渠道名称" prop="channelName" :show-overflow-tooltip="true"  />
         <el-table-column label="活动简介" prop="info" :show-overflow-tooltip="true"  />
         <el-table-column label="活动类型" prop="type" >
          <template  #default="scope">
            <template v-for="(type,indexi) in promotion_type" :key="indexi">
              <el-tag v-if="type.value == scope.row.type" :type="type.elTagType">{{type.label}}</el-tag>
            </template>
          
          </template>
         </el-table-column>

         <!-- <el-table-column label="活动明细" prop="type" width="120">
          <template  #default="scope">
            <el-tag v-if="scope.row.type === 1">折扣券/{{scope.row.discount}}折</el-tag>
            <el-tag v-else>代金券/{{scope.row.voucher}}元</el-tag>
          </template>
         </el-table-column> -->

         <el-table-column label="活动明细" prop="type" >
           <template  #default="scope">
            <template v-for="(type,indexi) in promotion_type" :key="indexi">
                <div v-if="type.value == scope.row.type">{{handleActivityDetails(type,scope.row)}}{{result}}</div>
            </template>
          </template>
         </el-table-column>

         <el-table-column label="活动状态" prop="status" :show-overflow-tooltip="true" >
          <template  #default="scope">
            <template v-for="(status,indexi) in promotion_status" :key="indexi">
              <el-tag v-if="status.value == scope.row.status" :type="status.elTagType">{{status.label}}</el-tag>
            </template>
          
          </template>
         </el-table-column>
         <el-table-column label="活动开始时间" prop="beginTime" :show-overflow-tooltip="true" />
         <el-table-column label="活动结束时间" prop="endTime" :show-overflow-tooltip="true" />

         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button
                  type="text"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['tienchin:promotion:edit']"
               >修改</el-button>
               <el-button
                  type="text"
                  icon="Delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['tienchin:promotion:delete']"
               >删除</el-button>
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
                     :label="parseInt(type.value)"
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

      <!-- 渠道导入对话框 -->
      <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
         <el-upload
            ref="uploadRef"
            :limit="1"
            accept=".xlsx, .xls"
            :headers="upload.headers"
            :action="upload.url + '?updateSupport=' + upload.updateSupport"
            :disabled="upload.isUploading"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            :auto-upload="false"
            drag
         >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
               <div class="el-upload__tip text-center">
                  <div class="el-upload__tip">
                     <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
                  </div>
                  <span>仅允许导入xls、xlsx格式文件。</span>
                  <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
               </div>
            </template>
         </el-upload>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitFileForm">确 定</el-button>
               <el-button @click="upload.open = false">取 消</el-button>
            </div>
         </template>
      </el-dialog>

   </div>
</template>

<script setup name="Role">
import {listChannel,addChannel,getChannel,updateChannel,delChannel} from "@/api/tienchin/channel/channel";
import {listPromotion } from '@/api/tienchin/promotion/promotion'
import { getToken } from "@/utils/auth";
const router = useRouter();
const { proxy } = getCurrentInstance();
const { promotion_status,promotion_type } = proxy.useDict("promotion_status","promotion_type");

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
const menuRef = ref(null);

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/tienchin/channel/importData"
});

const data = reactive({
  form: {},
  result:'',
  tableHeight: "",//表格高度
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    channelName: undefined,
    type: undefined
  },
  rules: {
    channelName: [{ required: true, message: "渠道名称不能为空", trigger: "blur" }],
    status: [{required: true, message: "渠道状态不能为空", trigger: "blur"}],
    type: [{required: true, message: "渠道类型不能为空", trigger: "blur"}]
  },
});

const { queryParams,result,form, rules,tableHeight } = toRefs(data);

/** 查询渠道列表 */
function getList() {
  loading.value = true;
  listPromotion(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
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

/** 导入按钮操作 */
function handleImport() {
  upload.title = "渠道导入";
  upload.open = true;
};

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const channelIds = row.channelId || ids.value;
  proxy.$modal.confirm('是否确认删除编号为"' + channelIds + '"的数据项?').then(function () {
    return delChannel(channelIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 下载模板操作 */
function importTemplate() {
  proxy.download("/tienchin/channel/importTemplate", {
  }, `channel_template_${new Date().getTime()}.xlsx`);
};
/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};
/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};
/** 导出按钮操作 */
function handleExport() {
  proxy.download("/tienchin/channel/export", {
    ...queryParams.value,
  }, `channel_${new Date().getTime()}.xlsx`);
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.channelId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

// 拼接活动明细
function handleActivityDetails(type,row){

    let splicing = "";

    // 折扣为null就是代金券
    if(row.discount == null){
        splicing = type.label+"/"+row.voucher
    } else {
        splicing = type.label+"/"+row.discount
    }
    
    const suffix = row.discount == null ? '元':'折'

    result.value = splicing + suffix;
    
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

//计算table高度(动态设置table高度)
function getTableHeight() {
    let tableH = 210; //距离页面下方的高度
    let tableHeightDetil = window.innerHeight - tableH;
    if (tableHeightDetil <= 300) {
    tableHeight.value = 300;
    } else {
    tableHeight.value = window.innerHeight - tableH;
    }
}
// onMounted(()=>{
//     //挂载window.onresize事件(动态设置table高度)
//     let _this = this;
//     window.onresize = () => {
//       if (_this.resizeFlag) {
//         clearTimeout(_this.resizeFlag);
//       }
//       _this.resizeFlag = setTimeout(() => {
//         _this.getTableHeight();
//         _this.resizeFlag = null;
//       }, 100);
//     };

// })


getList();
// getTableHeight();
</script>
