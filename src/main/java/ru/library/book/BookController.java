package ru.library.book;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.library.book.dto.BookRequest;
import ru.library.book.dto.BookResponse;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest req) {
        return bookService.create(req);
    }

    @GetMapping
    public List<BookResponse> list() { return bookService.findAll(); }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) { return bookService.findById(id); }
    
    @GetMapping("/{author}")
    public List<BookResponse> get (@PathVariable String author) { return bookService.findByAuthor(author); }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        return bookService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { bookService.delete(id); }

    @PostMapping("/{id}/borrow")
    public BookResponse borrow(@PathVariable Long id) { return bookService.borrow(id); }

    @PostMapping("/{id}/return")
    public BookResponse returnBook(@PathVariable Long id) { return bookService.returnBook(id); }
}