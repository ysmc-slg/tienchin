package top.zxqs.tienchin.framework.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import top.zxqs.tienchin.framework.web.service.SmsUserDetailsService;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 处理短信验证码登录的 Provider
 */
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Autowired
    @Qualifier(value = "smsUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken token = (SmsAuthenticationToken) authentication;
        String mobile = (String)token.getPrincipal();

        //然后，查询对应用户
        UserDetails user = userDetailsService.loadUserByUsername(mobile);
        if (Objects.isNull(user)) {
            throw new InternalAuthenticationServiceException("根据手机号：" + mobile + "，无法获取对应的用户信息！");
        }
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, authentication.getCredentials(),this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        authenticationResult.setDetails(token.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
