package ru.otus.hw.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewsPaper implements PostItem {
    private final long id;

    private final Addressee addressee;
}
