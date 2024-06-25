package com.example.notebook.repository;

import com.example.notebook.entity.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteBookRepository extends JpaRepository<NoteBook, Integer> {

}
