package com.example.notebook.service.impl;

import com.example.notebook.entity.NoteBook;
import com.example.notebook.repository.NoteBookRepository;
import com.example.notebook.service.NoteBookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoteBookServiceImpl implements NoteBookService {

    private final NoteBookRepository noteBookRepository;

    @Override
    public List<NoteBook> getAllNoteBooks() {
        return noteBookRepository.findAll();
    }

    @Override
    public List<NoteBook> getAllUserNoteBooks(int userId) {
        return noteBookRepository.findAll().stream().filter(x->x.getUser().getId() == userId).toList();
    }

    @Override
    public NoteBook getNoteBookById(int id) {
        return noteBookRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find noteBook with id: " + id));
    }

    @Override
    public NoteBook createNoteBook(NoteBook noteBook) {
       return noteBookRepository.save(noteBook);
    }

    @Override
    public NoteBook updateNoteBook(NoteBook noteBook) {
        return noteBookRepository.save(noteBook);
    }

    @Override
    public void deleteNoteBook(int id) {
        noteBookRepository.deleteById(id);
    }
}
