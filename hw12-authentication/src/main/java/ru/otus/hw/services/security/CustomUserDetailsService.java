package ru.otus.hw.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.security.User;
import ru.otus.hw.repositories.security.UserDetailsRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    public static final String USERNAME_NOT_FOUND_TEMPLATE_MESSAGE = "Username: %s not found";

    private final UserDetailsRepository userDetailsRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDetailsRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format(USERNAME_NOT_FOUND_TEMPLATE_MESSAGE, username)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .disabled(!user.isEnabled())
                .accountLocked(!user.isAccountNonLocked())
                .accountExpired(!user.isAccountNonExpired())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .build();
    }
}