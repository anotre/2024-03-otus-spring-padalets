package ru.otus.hw.services.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.converter.BookDtoConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.security.AclService;

@Aspect
@Configuration
@RequiredArgsConstructor
public class BookAclAspect {
    private final BookDtoConverter converter;

    private final AclService<Book> bookAclService;

    @AfterReturning(pointcut = "execution(* ru.otus.hw.services.BookServiceImpl.insert(..))", returning = "bookDto")
    public void doCreateAcl(Object bookDto) {
        var book = converter.toDomain((BookDto) bookDto);
        bookAclService.createAclFor(book);
    }

    @After(value = "execution(* ru.otus.hw.services.BookServiceImpl.deleteById(..)) && args(id)")
    public void doDeleteAcl(long id) {
        bookAclService.deleteAclByObjectId(id);
    }
}
