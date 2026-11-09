package ru.library.book.dto;

import ru.library.book.Book;

public record BookResponse(
        Long id, String isbn, String title, String author, int year, boolean available
) {
    public static BookResponse from(Book b) {
        return new BookResponse(
                b.getId(),
                b.getIsbn(),
                b.getTitle(),
                b.getAuthor(),
                b.getYear(),
                b.isAvailable()
        );
    }
}
