package com.edureka.bookServiceManagement.controller;

import com.edureka.bookServiceManagement.entity.Book;
import com.edureka.bookServiceManagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping
    @Operation(summary = "Add a new Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "500", description = "Unable to add Book")})
    private ResponseEntity<String> addNewBook(@RequestBody Book book){
        return bookService.addNewBook(book);
    }

    @GetMapping
    @Operation(summary = "Retrieve all Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Books Added")})
    private ResponseEntity<?> getAllBooks(){
        return bookService.getAllBooks();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get a Book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No Book with specified Id found")})
    private ResponseEntity<?> getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update a Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book details updated successfully"),
            @ApiResponse(responseCode = "500", description = "Specified Book details updating failed"),
            @ApiResponse(responseCode = "404", description = "No Book with specified Id found")})
    private ResponseEntity<String> updateBook(@RequestBody Book book,@PathVariable Long id){
        return bookService.updateBook(book,id);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book details updated successfully")})
    private ResponseEntity<String> deleteBookById(@PathVariable Long id){
        return bookService.deleteBookById(id);
    }
    @GetMapping("/borrow/{id}/{user}")
    @Operation(summary = "Borrow a Book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book borrowed successfully"),
            @ApiResponse(responseCode = "404", description = "Book not available for borrowing")})
    private ResponseEntity<?> borrowBookById(@PathVariable Long id,@PathVariable String user){
        return bookService.borrowBookById(id,user);
    }
    @GetMapping("/return/{id}")
    @Operation(summary = "Return a Book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book returned successfully"),
            @ApiResponse(responseCode = "406", description = "Book is already available in library")})
    private ResponseEntity<?> returnBookById(@PathVariable Long id){
        return bookService.returnBookById(id);
    }
    @GetMapping("/{name}")
    @Operation(summary = "Get all books by author name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified Book details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No Book with specified author found")})
    private ResponseEntity<?> getBooksByAuthorName(@PathVariable String name){
        return bookService.getBooksByAuthorName(name);
    }

}
