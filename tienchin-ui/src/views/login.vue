<template>
    <div class="login">
        <div class="login_box">
            <p class="login_title">
                <span :class="isActiveIndex === 0 ? 'isActiveTitle' : ''" @click="accountLogin">账号登录</span><!-- 利用三元运算符判定点击了哪个登录,从而绑定样式 -->
                <span :class="isActiveIndex === 1 ? 'isActiveTitle' : ''" @click="smsLogin">短信登录</span>
            </p>
            <el-form v-if="isActive" ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
                <h3 class="title">TienChin健身会员管理系统</h3>
                <el-form-item prop="username">
                  <el-input
                    v-model="loginForm.username"
                    type="text"
                    size="large"
                    auto-complete="off"
                    placeholder="账号"
                  >
                    <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
                  </el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input
                    v-model="loginForm.password"
                    type="password"
                    size="large"
                    auto-complete="off"
                    placeholder="密码"
                    @keyup.enter="handleLogin"
                  >
                    <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
                  </el-input>
                </el-form-item>
                
                <el-form-item prop="code" v-if="captchaEnabled">
                  <el-input
                    v-model="loginForm.code"
                    size="large"
                    auto-complete="off"
                    placeholder="验证码"
                    style="width: 63%"
                    @keyup.enter="handleLogin"
                  >
                    <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
                  </el-input>
                  <div class="login-code">
                    <img :src="codeUrl" @click="getCode" class="login-code-img"/>
                  </div>
                </el-form-item>

                <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
                <el-form-item style="width:100%;">
                  <el-button
                    :loading="loading"
                    size="large"
                    type="primary"
                    style="width:100%;"
                    @click.prevent="handleLogin"
                  >
                    <span v-if="!loading">登 录</span>
                    <span v-else>登 录 中...</span>
                  </el-button>
                  <div style="float: right;" v-if="register">
                    <router-link class="link-type" :to="'/register'">立即注册</router-link>
                  </div>
                </el-form-item>
            </el-form>

            
            <el-form v-else ref="loginRef" :model="loginForm" :rules="smsLoginRules" class="login-form">
                <h3 class="title">TienChin健身会员管理系统</h3>
                <el-form-item prop="phoneNumber">
                  <el-input
                    v-model="loginForm.phoneNumber"
                    type="text"
                    size="large"
                    auto-complete="off"
                    placeholder="手机号"
                  >
                    <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
                  </el-input>
                </el-form-item>
                <el-form-item prop="shortMsgCode">
                  <el-input
                    v-model="loginForm.shortMsgCode"
                    type="text"
                    style="width: 63%"
                    size="large"
                    auto-complete="off"
                    placeholder="验证码"
                    @keyup.enter="handleSmsLogin"
                  >
                    <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
                  </el-input>
                   <el-button
                      @click.stop="sendVerificationCode"
                      size="mini"
                      type="primary"
                      style="margin-left: 10px"
                      v-if="show"
                      >发送验证码</el-button>

                    <el-button
                      size="mini"
                      type="primary"
                      style="margin-left: 10px"
                      v-if="!show"
                      disabled>{{ count }}秒后重发</el-button> 
                </el-form-item>
              
                <el-form-item style="width:100%;">
                  <el-button
                    :loading="loading"
                    size="large"
                    type="primary"
                    style="width:100%;"
                    @click.prevent="handleSmsLogin"
                  >
                    <span v-if="!loading">登 录</span>
                    <span v-else>登 录 中...</span>
                  </el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>


<script setup>
import { getCodeImg,getSmsCode } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import useUserStore from '@/store/modules/user'
import { ref } from "@vue/reactivity";

const userStore = useUserStore()
const router = useRouter();
const { proxy } = getCurrentInstance();

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: "",
  phoneNumber: "",
  shortMsgCode: "",
  loginType: ""
  
  
});

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

const smsLoginRules = {
  phoneNumber: [
    { required: true, trigger: "blur", message: "请输入您的手机号" },
    {
      trigger: "blur",
      validator: (rule, value, callback) => {
        let phoneNumberdreg = /1[3-9]\d{9}/;
        if (!phoneNumberdreg.test(value)) {
          callback(new Error("手机号不合符合规范"));
        } else {
          callback();
        }
      },
    }
  ],
  shortMsgCode: [{ required: true, trigger: "blur", message: "请输入验证码" }]
};

const codeUrl = ref("");
const loading = ref(false);
// 验证码开关
const captchaEnabled = ref(true);
// 注册开关
const register = ref(true);
const showSms = ref(false);
const redirect = ref(undefined);
const isActiveIndex = ref(0);
const isActive = ref(true);

const count = ref("");
const show = ref(true);
const timer = ref(null);
// const url = ref("ws://127.0.0.1:8080/websocket/message");
// const message = ref("");
// const text_content = ref("")
// const ws = ref(null)


// function join() {
//     const wsuri = url.value;
//     ws.value = new WebSocket(wsuri);
//     const self = proxy;
//     proxy.ws.onopen = function (event) {
//       text_content.value = text_content.value + "已经打开连接!" + "\n";
//       console.log(text_content.value)
//     };
//     proxy.ws.onmessage = function (event) {
//       text_content.value = event.data + "\n";
//       console.log(text_content.value)
//     };
//     proxy.ws.onclose = function (event) {
//       text_content.value = text_content.value + "已经关闭连接!" + "\n";
//       console.log(text_content.value)
//     };
// }
// function exit() {
//     if (ws.value) {
//       proxy.ws.close();
//       ws.value = null;
//     }
// }

// function send() {
//     if (ws.value) {
//       proxy.ws.send(message.value);
//     } else {
//       alert("未连接到服务器");
//     }
// }

//验证码 倒计时
function sendVerificationCode() {
  let TIME_COUNT = 60;

  if (!timer.value) {
    count.value = TIME_COUNT;
    show.value = false;
    timer.value = setInterval(() => {
      if (count.value > 0 && count.value <= TIME_COUNT) {
        count.value--;
      } else {
        show.value = true;
        clearInterval(timer.value);
        timer.value = null;
      }
    }, 1000);

    getSms(loginForm.value.phoneNumber);
  }
}

// 获取短信验证码
function getSms(phoneNumber){
  getSmsCode(phoneNumber);
}

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 });
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 });
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 });
      } else {
        // 否则移除
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        router.push({ path: redirect.value || "/" });
      }).catch(() => {
        loading.value = false;
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode();
        }
      });
    }
  });
}

/**
 * 短信验证码登录
 */
function handleSmsLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true;
      // 调用action的登录方法
      //设置登录类型为短信验证码
      loginForm.value.loginType = "sms";
      console.log(loginForm.value)
      userStore.login(loginForm.value).then(() => {
        router.push({ path: redirect.value || "/" });
      }).catch(() => {
        loading.value = false;
      });
    }
  });
}

 // 账号登录
function accountLogin() {
    isActive.value = true
    isActiveIndex.value = 0
}
// 短信登录
function smsLogin() { 
    isActive.value = false
    isActiveIndex.value = 1
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaOnOff === undefined ? true : res.captchaOnOff;
    console.log(res)
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  };
}

getCode();
getCookie();
</script>

<style lang='scss' scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 450px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .isActiveTitle{
    color: #333;
    font-weight: bolder;
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}



.login_box{
    width: 500px;
    // height: 490px;
    background-color: #fff;
    border-radius: 18px;
    padding: 30px 50px 30px 30px;
}
.login_title{
    text-align: center;
    margin: 30px 30px 40px 30px;
    font-size: 24px;
    color: #666;
}
.login_title span{
    margin: 0 20px 0 20px;
    cursor: pointer;
}
.login_title span:hover{
    color: #333;
    font-weight: bolder;
}
.isActiveTitle{
    color: #333;
    font-weight: bolder;
}
.login_agree{
    margin-left: 30px;
}
.login_btn{
    width: 100%;
    height: 50px;
    margin: 0 0 0 10px;
    background-color: #0cb95f;
    color: #fff;
}

</style>
