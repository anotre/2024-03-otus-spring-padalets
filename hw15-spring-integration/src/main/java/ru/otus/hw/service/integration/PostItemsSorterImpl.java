package ru.otus.hw.service.integration;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Delivery;
import ru.otus.hw.domain.PostItem;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostItemsSorterImpl implements PostItemsSorter {
    @Override
    public List<Message<Delivery>> sort(List<PostItem> postItems) {
        return postItems
                .stream().collect(Collectors.groupingBy(PostItem::getAddressee)).entrySet()
                .stream().map(
                        addresseeListEntry -> MessageBuilder.withPayload(
                                        new Delivery(addresseeListEntry.getKey(), addresseeListEntry.getValue())
                                )
                                .setHeader("addressee", addresseeListEntry.getKey())
                                .build()
                ).collect(Collectors.toList());
    }
}
