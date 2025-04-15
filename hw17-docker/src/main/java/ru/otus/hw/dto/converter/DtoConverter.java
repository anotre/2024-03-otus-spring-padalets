package ru.otus.hw.dto.converter;

public interface DtoConverter<E, D> {
    D toDto(E e);

    E toDomain(D d);
}
