package ru.otus.hw.security;

import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class InMemoryLoginContext<T> implements LoginContext<T> {
    private T person;

    @Override
    public void login(T person) {
        this.person = person;
    }

    @Override
    public boolean isLoggedIn() {
        return nonNull(person);
    }

    @Override
    public T getLoggedIn() {
        return person;
    }
}
