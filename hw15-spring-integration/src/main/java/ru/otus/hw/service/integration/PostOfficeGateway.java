package ru.otus.hw.service.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.PostItem;

import java.util.List;

@MessagingGateway
public interface PostOfficeGateway {
    @Gateway(requestChannel = "mailingInputChannel")
    void process(List<PostItem> postItems);
}
