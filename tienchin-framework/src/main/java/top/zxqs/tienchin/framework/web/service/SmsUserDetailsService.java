package top.zxqs.tienchin.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.zxqs.tienchin.common.core.domain.entity.SysUser;
import top.zxqs.tienchin.common.core.domain.model.LoginUser;
import top.zxqs.tienchin.common.enums.UserStatus;
import top.zxqs.tienchin.common.exception.ServiceException;
import top.zxqs.tienchin.common.utils.StringUtils;
import top.zxqs.tienchin.system.service.ISysUserService;

/**
 * 短信验证码登录验证处理
 *
 * @author tienchin
 */

@Service(value = "smsUserDetailsService")
public class SmsUserDetailsService implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(SmsUserDetailsService.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {

        // 根据手机号查询用户
        SysUser user = userService.selectUserByphoneNumber(mobile);

        if (StringUtils.isNull(user)) {
            log.info("登录手机号：{} 不存在.", mobile);
            throw new ServiceException("登录手机号：" + mobile + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户已被删除.");
            throw new ServiceException("对不起，您的账号：已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("已被停用.");
            throw new ServiceException("对不起，您的账号：已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
