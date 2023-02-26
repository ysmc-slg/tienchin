package top.zxqs.tienchin.common.enums;

/**
 * 登录方式
 * @author: zxq
 * @create: 2023-02-26 15:34
 */
public enum  LoginType {

    USERNAME("username"),SMS("sms");

    private String loginType;

    LoginType(String type) {
        loginType = type;
    }

    public String getType(){
        return this.loginType;
    }
}
