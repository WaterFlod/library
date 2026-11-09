package ru.library.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.book.dto.BookRequest;
import ru.library.book.dto.BookResponse;
import ru.library.common.ConflictException;
import ru.library.common.NotFoundException;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse create(BookRequest req) {
        if (bookRepository.existsByIsbn(req.isbn())) {
            throw new ConflictException("Book with ISBN " + req.isbn() + " already exists");
        }
        Book book = Book.builder()
                .isbn(req.isbn())
                .title(req.title())
                .author(req.author())
                .year(req.year())
                .build();
        Book saved = bookRepository.save(book);
        return BookResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream().map(BookResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return BookResponse.from(getOrThrow(id));
    }
    
    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthor(String author) {
        List<BookResponse> books = bookRepository.findByAuthor(author)
                .stream()
                .map(BookResponse::from)
                .toList();
        if (books.isEmpty()) throw new NotFoundException("Book not found: author=" + author);
        return books;
    }

    public BookResponse update(Long id, BookRequest req) {
        Book book = getOrThrow(id);
        if (!book.getIsbn().equals(req.isbn()) && bookRepository.existsByIsbn(req.isbn())) {
            throw new ConflictException("ISBN already taken: " + req.isbn());
        }
        book.setIsbn(req.isbn());
        book.setTitle(req.title());
        book.setAuthor(req.author());
        book.setYear(req.year());
        return BookResponse.from(book);
    }

    public void delete(Long id) {
        Book book = getOrThrow(id);
        bookRepository.delete(book);
    }

    public BookResponse borrow(Long id) {
        Book book = getOrThrow(id);
        if (!book.isAvailable()) {
            throw new ConflictException("Book " + id + " is already borrowed");
        }
        book.setAvailable(false);
        return BookResponse.from(book);
    }

    public BookResponse returnBook(Long id) {
        Book book = getOrThrow(id);
        book.setAvailable(true);
        return BookResponse.from(book);
    }

    private Book getOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: id=" + id));
    }
}
