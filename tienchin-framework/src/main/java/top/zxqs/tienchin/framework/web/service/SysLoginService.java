package top.zxqs.tienchin.framework.web.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import top.zxqs.tienchin.common.constant.Constants;
import top.zxqs.tienchin.common.constant.RabbitConstant;
import top.zxqs.tienchin.common.core.domain.entity.SysUser;
import top.zxqs.tienchin.common.core.domain.model.LoginUser;
import top.zxqs.tienchin.common.core.redis.RedisCache;
import top.zxqs.tienchin.common.enums.LoginType;
import top.zxqs.tienchin.common.exception.ServiceException;
import top.zxqs.tienchin.common.exception.user.CaptchaException;
import top.zxqs.tienchin.common.exception.user.CaptchaExpireException;
import top.zxqs.tienchin.common.exception.user.UserPasswordNotMatchException;
import top.zxqs.tienchin.common.utils.*;
import top.zxqs.tienchin.common.utils.ip.IpUtils;
import top.zxqs.tienchin.common.utils.sms.SmsUtil;
import top.zxqs.tienchin.framework.manager.AsyncManager;
import top.zxqs.tienchin.framework.manager.factory.AsyncFactory;
import top.zxqs.tienchin.framework.security.token.SmsAuthenticationToken;
import top.zxqs.tienchin.system.service.ISysConfigService;
import top.zxqs.tienchin.system.service.ISysUserService;

/**
 * 登录校验方法
 *
 * @author tienchin
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     * @param username    用户名
     * @param password    密码
     * @param code        验证码
     * @param uuid        唯一标识
     * @param phoneNumber  手机号
     * @param shortMsgCode 短信验证码
     * @param loginType    登录方式
     * @return
     */
    public String login(String username, String password, String code, String uuid,String phoneNumber,String shortMsgCode,String loginType) {

        // 用户验证
        Authentication authentication = null;
        try {
            // 登录方式为短信验证码
            if(LoginType.SMS.getType().equals(loginType)){
                SmsUtil.checkPhoneNumber(phoneNumber.trim());
                // 校验短息验证码
                validateSms(username,shortMsgCode.trim(),phoneNumber.trim());
                authentication = authenticationManager
                        .authenticate(new SmsAuthenticationToken(phoneNumber,shortMsgCode));
            } else {
                // 登录方式为用户名/密码
                boolean captchaOnOff = configService.selectCaptchaOnOff();
                // 验证码开关
                if (captchaOnOff) {
                    validateCaptcha(username, code, uuid);
                }
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                // 这句话其实就是去执行登录
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            }

        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }

        // 记录登录信息
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 更新用户信息
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 校验短信验证码
     * @param username 登录用户名
     * @param shortMsgCode 短信验证码
     * @param phoneNumber 手机号
     * @return 结果
     */
    public void validateSms(String username,String shortMsgCode,String phoneNumber) {
        // 获取redis中的短息验证码
        String smsKey = RabbitConstant.SMS_KEY + phoneNumber;
        String smsCode = redisCache.getCacheObject(smsKey);

        if(smsCode == null){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }

        if(!smsCode.equals(shortMsgCode)){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
