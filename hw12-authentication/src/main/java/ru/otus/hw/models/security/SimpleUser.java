package ru.otus.hw.models.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class SimpleUser implements UserDetails {
    @Id
    private String username;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(targetEntity = SimpleAuthority.class)
    @JoinColumn(name = "username")
    private Set<GrantedAuthority> authorities;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public SimpleUser() {
    }
}
