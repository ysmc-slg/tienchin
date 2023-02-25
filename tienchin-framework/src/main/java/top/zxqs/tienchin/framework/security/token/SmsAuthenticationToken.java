package top.zxqs.tienchin.framework.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author: zxq
 * @date: 2023-02-23 14:08
 */
public class SmsAuthenticationToken  extends AbstractAuthenticationToken {

    //对应手机号码
    private final Object principal;
    //对应手机验证码
    private Object credentials;


    public SmsAuthenticationToken(String mobile, Object credentials){
        super(null);
        this.principal = mobile;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }


    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}

