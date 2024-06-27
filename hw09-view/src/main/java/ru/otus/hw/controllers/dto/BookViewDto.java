package ru.otus.hw.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookViewDto {
    private long id;
    private String title;
    private long authorId;
    private long genreId;
}
