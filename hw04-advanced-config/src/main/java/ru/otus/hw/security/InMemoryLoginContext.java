package ru.otus.hw.security;

import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class InMemoryLoginContext<T> implements LoginContext<T> {
    private T login;

    @Override
    public void login(T login) {
        this.login = login;
    }

    @Override
    public boolean isLoggedIn() {
        return nonNull(login);
    }

    @Override
    public T getLoggedIn() {
        return login;
    }
}
