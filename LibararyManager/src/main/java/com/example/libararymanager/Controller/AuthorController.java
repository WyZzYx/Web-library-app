package com.example.libararymanager.Controller;

import com.example.libararymanager.Controller.Repository.AuthorRepository;

import com.example.libararymanager.Controller.Repository.BookRepository;
import com.example.libararymanager.Model.Author;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/author")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    @GetMapping("/{id}")
    String findBookById(@PathVariable Integer id, Model model){

        model.addAttribute("author", authorRepository.findAuthorById(id));

        model.addAttribute("books", bookRepository.findBooksByAuthor(id));

        return "author";

    }

    @PostMapping("/save")
    public String saveAuthor(@RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String pseudonym) {
        int id = -1;
        if(authorRepository.findAllAuthors() == null){
            id = 1;
        }
         else id = authorRepository.findAllAuthors().size()+1;

        Author author = new Author(id, name, surname, pseudonym);
        authorRepository.createAuthor(author);
        return "redirect:/author/allAuthors";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("author", new Author(0, null, null, null));


        return "createAuthorForm";
    }

    @GetMapping("/allAuthors")
    public String authorsPage(Model model){
        model.addAttribute("authorsText", "Web Library Authors");



        model.addAttribute("author",
                authorRepository.findAllAuthors() != null ?
                        authorRepository.findAllAuthors().toArray()
                        : new ArrayList<>()
        );


        return "authorsMainPage";
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable int id) {
        authorRepository.deleteAuthor(id);
        bookRepository.deleteBooksByAuthor(id);
        return "redirect:/author/allAuthors";
    }



}
