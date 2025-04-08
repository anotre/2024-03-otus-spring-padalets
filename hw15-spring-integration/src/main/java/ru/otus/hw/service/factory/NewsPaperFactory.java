package ru.otus.hw.service.factory;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Addressee;
import ru.otus.hw.domain.NewsPaper;
import ru.otus.hw.domain.PostItem;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class NewsPaperFactory implements PostItemFactory {
    public static final AtomicLong POST_ITEMS_SEQUENCE = new AtomicLong(0L);

    @Override
    public PostItem create(Addressee addressee) {
        return new NewsPaper(POST_ITEMS_SEQUENCE.incrementAndGet(), addressee);
    }
}
