package com.catalogue.repository;



import com.catalogue.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Iterable<Book> findAllByTitleLikeIgnoreCase(String filter);
    Iterable<Book> findBooksByAuthor_Id(int authorId);

}

