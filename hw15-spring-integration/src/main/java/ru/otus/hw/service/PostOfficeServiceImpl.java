package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.PostOfficeProperties;
import ru.otus.hw.dao.Dao;
import ru.otus.hw.domain.Addressee;
import ru.otus.hw.domain.PostItem;
import ru.otus.hw.service.integration.PostOfficeGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostOfficeServiceImpl implements PostOfficeService {
    private static final int HOUR = 10000;

    private static final int WORKDAY = 3;

    private final PostOfficeGateway gateway;

    private final PostOfficeProperties postOfficeProperties;

    private final Dao<Addressee> addresseeDao;

    public void doWork() {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < WORKDAY; i++) {
            pool.execute(() -> {
                List<PostItem> postItems = this.getPostItems();
                gateway.process(postItems);
            });
            this.delay();
        }
    }

    private List<PostItem> getPostItems() {
        int postItemsAmount = ThreadLocalRandom.current()
                .nextInt(postOfficeProperties.getMin(), postOfficeProperties.getMax());
        return this.generatePostItems(postItemsAmount);
    }

    private List<PostItem> generatePostItems(int amount) {
        List<PostItem> postItems = new ArrayList<>();
        var postItemsTypes = PostItems.values();
        var addressees = addresseeDao.getAll();

        for (int i = 0; i < amount; i++) {
            var typeIndex = ThreadLocalRandom.current().nextInt(0, postItemsTypes.length);
            var addresseeIndex = ThreadLocalRandom.current().nextInt(0, addressees.size());
            var postItemType = postItemsTypes[typeIndex];
            postItems.add(
                    PostItemSimpleFactory.createPostItem(postItemType, addressees.get(addresseeIndex))
            );
        }

        log.info("{} post items was generated.", amount);

        return postItems;
    }

    private void delay() {
        try {
            Thread.sleep(HOUR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
