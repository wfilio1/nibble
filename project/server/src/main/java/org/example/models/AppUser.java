package org.example.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppUser implements UserDetails {

    private int userId;
    private String userName;
    private String userPassword;
    private boolean isEnabled;
    private Collection<GrantedAuthority> authorities;

    public AppUser(int appUserId, String username, String password, boolean enabled, List<String> roles) {
        this.userId = appUserId;
        this.userName = username;
        this.userPassword = password;
        this.isEnabled = enabled;
        this.authorities = convertRolesToAuthorities(roles);
    }

    public AppUser() {

    };

    private static Collection<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
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
        return isEnabled;
    }

    public int getAppUserId() {
        return userId;
    }

    public void setAppUserId(int appUserId) {
        this.userId = appUserId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return userId == appUser.userId && isEnabled == appUser.isEnabled && Objects.equals(userName, appUser.userName) && Objects.equals(userPassword, appUser.userPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userPassword, isEnabled);
    }
}
