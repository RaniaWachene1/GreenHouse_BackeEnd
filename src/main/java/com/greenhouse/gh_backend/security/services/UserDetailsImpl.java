package com.greenhouse.gh_backend.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.greenhouse.gh_backend.entities.User;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private long idUser;

    private String username;

    private String email;

    @JsonIgnore
    private String password;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;

    private final User user;

    public UserDetailsImpl(long idUser,String email, String password,
                           Collection<? extends GrantedAuthority> authorities, User user) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.user = user;

    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UserDetailsImpl(
                user.getIdUser(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user
        );
    }


    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return this.active;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(idUser, user.idUser);
    }
}
