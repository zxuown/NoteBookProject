package com.example.notebook.service;

import com.example.notebook.entity.NoteBook;

import java.util.List;

public interface NoteBookService {
    List<NoteBook> getAllNoteBooks();

    List<NoteBook> getAllUserNoteBooks(int userId);

    NoteBook getNoteBookById(int id);

    NoteBook createNoteBook(NoteBook noteBook);

    NoteBook updateNoteBook(NoteBook noteBook);

    void deleteNoteBook(int id);
}
