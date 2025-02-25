package com.sportygroup.bookservice.repository;

import com.sportygroup.bookservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    Page<Book> findByIdIn(List<Long> ids, Pageable pageable);

    @Modifying
    @Query("UPDATE Book b SET b.stockQuantity = b.stockQuantity + :quantity WHERE b.id = :id")
    int updateStock(@Param("id") Long id, @Param("quantity") int quantity);
}