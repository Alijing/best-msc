package com.jing.msc.security.entity;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.entity
 * @date : 2023/7/4 10:33
 * @description :
 */
@Setter
public class LoginSpider implements UserDetails {

    private static final long serialVersionUID = -8312092472666046906L;

    @Getter
    private Spider spider;

    @Getter
    private List<String> permissions;

    private List<SimpleGrantedAuthority> authorities;

    public LoginSpider() {
    }

    public LoginSpider(Spider spider, List<String> permissions) {
        this.spider = spider;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!CollectionUtils.isEmpty(authorities)) {
            return authorities;
        }
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
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

}
