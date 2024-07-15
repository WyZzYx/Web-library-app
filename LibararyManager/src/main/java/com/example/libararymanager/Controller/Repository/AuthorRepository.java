package com.example.libararymanager.Controller.Repository;

import com.example.libararymanager.Model.Author;
import com.example.libararymanager.Model.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorRepository {

    private List<Author> authors = new ArrayList<>();
    public List<Author> findAllAuthors(){
        if (authors.isEmpty()) {
            System.out.println("There are no authors in here yet");
            return null;
        }
        return authors;
    }

    public Author findAuthorById(Integer id){
        if(id != 0){
            return authors.stream()
                    .filter(author -> author.id() == id)
                    .findFirst().orElse(null);
        }
        return new Author(0, " ", " ", "no such author found");

    }

    public void deleteAuthor(Integer id){
        authors.removeIf(author -> author.id() == id);
    }

    public void createAuthor(Author author){
        authors.add(author);

    }

    @PostConstruct
    private void init(){
        authors.add( new Author(1, "Joanne", "Rowling", "J.K Rowling"));
        authors.add(new Author(2, "Andrzej", "Sapkowski", "Andrzej Sapkowski"));
        authors.add(new Author(3, "Taras", "Shewchenko", "Taras Shevchenko"));
    }
}
