package com.example.notebook.controller;

import com.example.notebook.entity.NoteBook;
import com.example.notebook.repository.NoteBookRepository;
import com.example.notebook.service.NoteBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notebooks")
public class NoteBookController {

    private final NoteBookService noteBookService;

    @GetMapping()
    public List<NoteBook> getAll() {
        return noteBookService.getAllNoteBooks();
    }

    @GetMapping("/{id}")
    public NoteBook getById(@PathVariable int id) {
        return noteBookService.getNoteBookById(id);
    }

    @GetMapping("/user/{id}")
    public List<NoteBook> getAllUserNoteBooks(@PathVariable int id) {
        return noteBookService.getAllUserNoteBooks(id);
    }

    @PostMapping()
    public NoteBook createNoteBook(@RequestBody NoteBook noteBook) {
        return noteBookService.createNoteBook(noteBook);
    }

    @PutMapping()
    public NoteBook updateNoteBook(@RequestBody NoteBook noteBook) {
        return noteBookService.updateNoteBook(noteBook);
    }

    @DeleteMapping("/{id}")
    public void deleteNoteBook(@PathVariable int id) {
        noteBookService.deleteNoteBook(id);
    }
}
