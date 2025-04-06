package ru.otus.hw.service.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempTablesServiceImpl implements TempTablesService {
    private final JdbcOperations jdbc;

    @Override
    public void dropTempTables() {
        jdbc.execute("DROP TABLE IF EXISTS authors_ids, genres_ids, books_ids, comments_ids");
    }
}
