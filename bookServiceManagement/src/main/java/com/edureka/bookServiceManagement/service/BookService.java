package com.edureka.bookServiceManagement.service;


import com.edureka.bookServiceManagement.entity.Author;
import com.edureka.bookServiceManagement.entity.Book;
import com.edureka.bookServiceManagement.entity.BorrowingRecord;
import com.edureka.bookServiceManagement.repository.BookRepository;
import com.edureka.bookServiceManagement.utils.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    RestTemplate restTemplate;
    @Value("${borrowingRecord.baseUrl}")
    private String borrowingRecordServiceBaseUrl;
    @Value("${author.baseUrl}")
    private String authorServiceBaseUrl;

    public ResponseEntity<?> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
        if (!allBooks.isEmpty()) {
            return ResponseEntity.ok(allBooks);
        }
        return new ResponseEntity<>("No Books Added", HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> addNewBook(Book book) {
        try {
            bookRepository.save(book);
            return new ResponseEntity<>("Book Details added successfully", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Exception raised while adding new book" + exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getBookById(Long id) {
        Optional<Book> bookDetails = bookRepository.findById(id);
        if (bookDetails.isPresent()) {
            return ResponseEntity.ok(bookDetails.get());
        }
        return new ResponseEntity<>("No book with specified Id found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> updateBook(Book book, Long id) {
        ResponseEntity<?> bookDetails = getBookById(id);
        if (bookDetails.getStatusCode().is2xxSuccessful()) {
            try {
                bookRepository.save(book);
                return new ResponseEntity<>("Book Details updated successfully", HttpStatus.OK);
            } catch (Exception exception) {
                return new ResponseEntity<>("Exception raised while updating book" + exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("No book with specified Id found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteBookById(Long id) {
        bookRepository.deleteById(id);
        return new ResponseEntity<>("Book Details deleted successfully", HttpStatus.OK);
    }
    public ResponseEntity<?> borrowBookById(Long id, String user) {
        Optional<Book> bookDetails = bookRepository.findById(id);
        if (bookDetails.isPresent()) {
            Book book = bookDetails.get();
            if(book.getStatus().equals(BookStatus.AVAILABLE)){
                BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                                                 .borrowingDate(LocalDate.now())
                                                 .bookId(book.getId())
                                                 .user(user)
                                                 .build();
                ResponseEntity<?> response = restTemplate.postForEntity(borrowingRecordServiceBaseUrl,borrowingRecord, ResponseEntity.class);
                if(response.getStatusCode().is2xxSuccessful()){
                    book.setStatus(BookStatus.BORROWED);
                    bookRepository.save(book);
                    return new ResponseEntity<>("Book successfully borrowed", HttpStatus.OK);
                }else{
                    return response;
                }
            }
        }
        return new ResponseEntity<>("Book is not available for borrowing", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> returnBookById(Long id) {
        Optional<Book> bookDetails = bookRepository.findById(id);
        if (bookDetails.isPresent()) {
            Book book = bookDetails.get();
            if(book.getStatus().equals(BookStatus.BORROWED)){
                ResponseEntity<?> response = restTemplate.getForEntity(borrowingRecordServiceBaseUrl+"/return/"+id, ResponseEntity.class);
                if(response.getStatusCode().is2xxSuccessful()){
                    book.setStatus(BookStatus.AVAILABLE);
                    bookRepository.save(book);
                    return new ResponseEntity<>("Book successfully returned", HttpStatus.OK);
                }else{
                    return response;
                }
            }
        }
        return new ResponseEntity<>("Book is already available in library", HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<?> getBooksByAuthorName(String name) {
        ResponseEntity<Author> authorResponse = restTemplate.getForEntity(authorServiceBaseUrl+"/"+name, Author.class);
        if(authorResponse.getStatusCode().is2xxSuccessful()){
            List<Book> books = bookRepository.findAllByAuthorId(Objects.requireNonNull(authorResponse.getBody()).getId());
            if(!books.isEmpty()) {
                return ResponseEntity.ok(books);
            }
            return new ResponseEntity<>("No books found for specified author",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Author not found",authorResponse.getStatusCode());
    }
}
