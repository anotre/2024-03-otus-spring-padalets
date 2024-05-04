package ru.otus.hw.security;

public interface LoginContext<T> {
    void login(T login);

    boolean isLoggedIn();

    T getLoggedIn();
}
