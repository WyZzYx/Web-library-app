package com.example.libararymanager.Controller;

import com.example.libararymanager.Controller.Repository.AuthorRepository;
import com.example.libararymanager.Controller.Repository.BookRepository;
import com.example.libararymanager.Model.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @PostMapping("/save")
    public String saveBook(@RequestParam String title,
                           @RequestParam String description,
                           @RequestParam int yearOfPublishing,
                           @RequestParam int authorId) {
        int id = -1;
        if(bookRepository.findAllBooks() == null){
            id = 1;
        }
        else id = bookRepository.findAllBooks().size()+1;
        Book book = new Book(id, title, description, authorId, yearOfPublishing);
        bookRepository.createBook(book);
        return "redirect:/book/allBooks";
    }




    @GetMapping("/allBooks")
    public String booksPage(@RequestParam(required = false) String query, Model model) {
         model.addAttribute("booksText", "Web Library Books");
         List<Book> filteredBooks = bookRepository.filterBooks(query, authorRepository);
        model.addAttribute("book", filteredBooks);
         model.addAttribute("author", authorRepository);
         return "booksMainPage";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("author", authorRepository);

        model.addAttribute("authors", authorRepository.findAllAuthors());

        model.addAttribute("book", new Book(0, "", "", 0, 0));



        return "createBookForm";
    }


    @GetMapping("/{id}")
    String findBookById(@PathVariable Integer id, Model model){


        model.addAttribute("book", bookRepository.findById(id));

        model.addAttribute("author", authorRepository);

        return "book";

    }

    @GetMapping("/update/{id}/complete")
    String updateBook(@PathVariable int id,
                      @RequestParam String title,
                      @RequestParam String description,
                      @RequestParam int yearOfPublishing,
                      @RequestParam int authorId) {


        Book book = new Book(id, title, description, authorId, yearOfPublishing);


        bookRepository.updateBook(book, id);
        return "redirect:/book/allBooks";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(Model model, @PathVariable int id) {

        model.addAttribute("author", authorRepository);

        model.addAttribute("authors", authorRepository.findAllAuthors());

        model.addAttribute("book", new Book(id, "", "", 0, 0));

        return "updateBookForm";
    }


    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookRepository.deleteBook(id);
        return "redirect:/book/allBooks";
    }

    @GetMapping("/export")
    public void exportBooksToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"books.csv\"");
        PrintWriter writer = response.getWriter();
        bookRepository.writeBooksToCsv(bookRepository.findAllBooks(), writer);
    }



}
