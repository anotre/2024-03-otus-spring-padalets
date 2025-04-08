package ru.otus.hw.service.factory;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Addressee;
import ru.otus.hw.domain.Parcel;
import ru.otus.hw.domain.PostItem;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ParcelFactory implements PostItemFactory {
    public static final AtomicLong POST_ITEMS_SEQUENCE = new AtomicLong(0L);

    @Override
    public PostItem create(Addressee addressee) {
        return new Parcel(POST_ITEMS_SEQUENCE.incrementAndGet(), addressee);
    }
}
