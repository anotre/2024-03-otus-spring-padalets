package ru.otus.hw.service.factory;

import ru.otus.hw.domain.Addressee;
import ru.otus.hw.domain.PostItem;

public interface PostItemFactory {
    PostItem create(Addressee addressee);
}
