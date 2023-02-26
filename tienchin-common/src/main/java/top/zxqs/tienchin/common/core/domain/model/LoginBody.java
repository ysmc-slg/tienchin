package top.zxqs.tienchin.common.core.domain.model;

/**
 * 用户登录对象
 *
 * @author tienchin
 */
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信验证码
     */
    private String shortMsgCode;

    /**
     * 登录方式
     */
    private String loginType;
    /**
     * 唯一标识
     */
    private String uuid;


    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShortMsgCode() {
        return shortMsgCode;
    }

    public void setShortMsgCode(String shortMsgCode) {
        this.shortMsgCode = shortMsgCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
