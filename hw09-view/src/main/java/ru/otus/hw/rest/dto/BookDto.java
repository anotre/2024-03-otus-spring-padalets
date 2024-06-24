package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private long authorId;
    private long genreId;
}
