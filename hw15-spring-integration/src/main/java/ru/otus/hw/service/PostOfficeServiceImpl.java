package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.AddresseeDao;
import ru.otus.hw.domain.PostItem;
import ru.otus.hw.service.factory.PostItemFactory;
import ru.otus.hw.service.integration.PostOfficeGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostOfficeServiceImpl implements PostOfficeService {
    private static final int HOUR = 5_000;

    private static final int WORKDAY = 3;

    private static final int MIN_POST_ITEMS_LOAD = 0;

    private static final int MAX_POST_ITEMS_LOAD = 400;

    private final PostOfficeGateway gateway;

    private final AddresseeDao addresseeDao;

    private final List<PostItemFactory> postItemFactories;

    public void doWork() {
        var pool = Executors.newFixedThreadPool(WORKDAY);

        for (int i = 0; i < WORKDAY; i++) {
            pool.execute(() -> {
                List<PostItem> postItems = this.getPostItems();
                gateway.process(postItems);
            });

            this.waitLoad();
        }
    }

    private List<PostItem> getPostItems() {
        int postItemsAmount = ThreadLocalRandom.current()
                .nextInt(MIN_POST_ITEMS_LOAD, MAX_POST_ITEMS_LOAD);
        return this.generatePostItems(postItemsAmount);
    }

    private List<PostItem> generatePostItems(int amount) {
        List<PostItem> postItems = new ArrayList<>();
        var addressees = addresseeDao.getAll();
        var postItemTypesNumber = this.postItemFactories.size();
        var addresseesNumber = addressees.size();

        for (int i = 0; i < amount; i++) {
            var typeIndex = ThreadLocalRandom.current().nextInt(0, postItemTypesNumber);
            var addresseeIndex = ThreadLocalRandom.current().nextInt(0, addresseesNumber);
            var factory = this.postItemFactories.get(typeIndex);
            postItems.add(
                    factory.create(addressees.get(addresseeIndex))
            );
        }

        log.info("{} post items was generated.", amount);

        return postItems;
    }

    private void waitLoad() {
        try {
            Thread.sleep(HOUR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
