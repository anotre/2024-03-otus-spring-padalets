package ru.otus.hw.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Delivery {
    private final Addressee addressee;

    private final List<PostItem> postItems;
}
