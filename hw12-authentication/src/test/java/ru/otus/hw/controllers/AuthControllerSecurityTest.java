package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.security.SecurityConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ContextConfiguration(classes = AuthController.class)
@Import({SecurityConfiguration.class})
class AuthControllerSecurityTest {
    public final static String USER = "user1";

    public final static String AUTHORITY = "ROLE_ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mustBePublicAccessible() throws Exception {
        var loginUrl = "/login";
        mockMvc.perform(get(loginUrl)
                        .with(user(USER).authorities(new SimpleGrantedAuthority(AUTHORITY))))
                .andExpect(status().isOk());

        mockMvc.perform(get(loginUrl))
                .andExpect(status().isOk());
    }
}