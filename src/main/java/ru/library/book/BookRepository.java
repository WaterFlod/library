package ru.library.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @Query(
    """
    SELECT id, isbn, title, author, year, available
    FROM books
    WHERE author ILIKE :author    
    """
    )
    List<Book> findByAuthor(String author);
}
