package ru.otus.hw.service.integration;


import org.springframework.messaging.Message;
import ru.otus.hw.domain.Delivery;
import ru.otus.hw.domain.PostItem;

import java.util.List;

public interface PostItemsSorter {
    List<Message<Delivery>> sort(List<PostItem> postItems);
}
