package com.jing.msc.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.entity
 * @date : 2023/7/4 10:33
 * @description :
 */
public class LoginSpider implements UserDetails {

    private static final long serialVersionUID = -8312092472666046906L;

    private Spider spider;

    public LoginSpider() {
    }

    public LoginSpider(Spider spider) {
        this.spider = spider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return spider.getPassword();
    }

    @Override
    public String getUsername() {
        return spider.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }
}
