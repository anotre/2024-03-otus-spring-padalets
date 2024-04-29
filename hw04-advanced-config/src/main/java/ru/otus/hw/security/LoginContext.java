package ru.otus.hw.security;

public interface LoginContext<T> {
    void login(T person);

    boolean isLoggedIn();

    T getLoggedIn();
}
