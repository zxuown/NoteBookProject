package com.example.notebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.notebook.entity.User;

public interface UserRepository  extends JpaRepository<User,Long>
{
    public User findByUsername(String username);
}
