package com.example.test_tasks.repo;

import com.example.test_tasks.entites.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepo extends JpaRepository<BookEntity,UUID > {
}
