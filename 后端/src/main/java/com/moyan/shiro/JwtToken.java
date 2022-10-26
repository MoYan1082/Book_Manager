package com.moyan.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }
    //Principals：是一个表示用户的唯一属性，可以是用户名，邮箱之类的。
    //Credentials：是证明用户身份的证书，可以是密码或者指纹之类的。
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
