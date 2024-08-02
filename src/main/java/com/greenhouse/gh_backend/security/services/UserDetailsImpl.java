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


    private String email;
    private String firstName;
    private String lastName;

    private String companyName;
    private String sector;

    private String industry;
    private Float revenue;
    private String headquarters;
    private String currency;

    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private final User user;

    public UserDetailsImpl(long idUser, String email, String firstName, String lastName, String companyName, String industry, String sector, String password,
                           Collection<? extends GrantedAuthority> authorities, User user, Float revenue, String headquarters, String currency) {
        this.idUser = idUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.industry = industry;
        this.sector = sector;
        this.password = password;
        this.authorities = authorities;
        this.user = user;
        this.revenue = revenue;
        this.headquarters = headquarters;
        this.currency = currency;
    }


    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UserDetailsImpl(
                user.getIdUser(),
                user.getEmail(),
                user.getFirstName(),
                user.getFirstName(),
                user.getCompanyName(),
                user.getIndustry(),
                user.getSector(),
                user.getPassword(),
                authorities,
                user,
                user.getRevenue(),
                user.getHeadquarters(),
                user.getCurrency()
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

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(idUser, user.idUser);
    }

    public Float getRevenue() {
        return revenue;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
