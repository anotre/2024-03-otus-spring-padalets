package ru.otus.hw.service;

import ru.otus.hw.domain.Addressee;
import ru.otus.hw.domain.Letter;
import ru.otus.hw.domain.NewsPaper;
import ru.otus.hw.domain.Parcel;
import ru.otus.hw.domain.PostItem;

import java.util.concurrent.atomic.AtomicLong;

public class PostItemSimpleFactory {
    public static final AtomicLong POST_ITEMS_SEQUENCE = new AtomicLong(0L);

    public static PostItem createPostItem(PostItems type, Addressee addressee) {
        switch (type) {
            case LETTER:
                return new Letter(POST_ITEMS_SEQUENCE.incrementAndGet(), addressee);
            case NEWS_PAPER:
                return new NewsPaper(POST_ITEMS_SEQUENCE.incrementAndGet(), addressee);
            case PARCEL:
                return new Parcel(POST_ITEMS_SEQUENCE.incrementAndGet(), addressee);
            default:
                throw new IllegalArgumentException("Factory doesn't support such type of post items");
        }
    }
}
