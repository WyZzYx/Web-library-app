package com.example.libararymanager.Controller.Repository;


import com.example.libararymanager.Model.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private List<Book> books = new ArrayList<>();


    public List<Book> findAllBooks(){
        if(books.isEmpty()){
            System.out.println("There are no books in here yet");
            return null;
        }
        return books;
    }

    public Book findById(Integer id){
        return books.stream()
                .filter(book -> book.id() == id)
                .findFirst().orElse(null);
    }

    public List<Book> findBooksByAuthor(int id) {
        return books.stream()
                .filter(book -> book.authorId() == id).toList();
    }


    public List<Book> filterBooks(String query, AuthorRepository authorRepository) {
        if (query == null || query.trim().isEmpty()) {
            return books;
        }
        String lowerCaseQuery = query.toLowerCase();
        return books.stream()
                .filter(book -> book.title().toLowerCase().contains(lowerCaseQuery) ||
                        (authorRepository.findAuthorById(book.authorId()) != null &&
                                authorRepository.findAuthorById(book.authorId()).pseudonym().toLowerCase().contains(lowerCaseQuery)))
                .collect(Collectors.toList());
    }

    public void createBook(Book book){
        books.add(book);

    }
    public void updateBook(Book book, Integer id){
        Optional<Book> existingBook = Optional.ofNullable(findById(id));
        if(existingBook.isPresent()){
            books.set(books.indexOf(existingBook.get()), book);
        }
    }

    public void deleteBook(Integer id){
        books.removeIf(book -> book.id() == id);
    }

    public void deleteBooksByAuthor(int id) {
        books.removeIf(book -> book.authorId() == id);
    }

    public void writeBooksToCsv(List<Book> books, PrintWriter writer) {
        writer.write("Id,Title,Description,Year of Publishing,Author Id\n");
        for (Book book : books) {
            writer.write(book.id() + ","
                    + book.title() + ","
                    + book.description() + ","
                    + book.yearOfPublishing() + ","
                    + book.authorId() + "\n");
        }
    }

    @PostConstruct
    private void init(){
        books.add(new Book(1,
                "Harry Potter",
                "Description of book 1",
                1,
                2001));
        books.add(new Book(2,
                "The Witcher",
                "Description of book 2",
                2,
                2003));
        books.add(new Book(3,
                "Kobzar",
                "Description of book 3",
                3,
                1843)
        );
    }

}
