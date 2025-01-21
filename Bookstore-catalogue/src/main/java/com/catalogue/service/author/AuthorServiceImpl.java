package com.catalogue.service.author;

import com.catalogue.entity.Author;
import com.catalogue.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Iterable<Author> findAllAuthors() {
        return this.authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author createAuthor(String name, String details, String image) {
        return this.authorRepository.save(new Author(null, name, details, image));
    }

    @Override
    public Optional<Author> findAuthor(int AuthorId) {
        return this.authorRepository.findById(AuthorId);
    }

    @Override
    @Transactional
    public void updateAuthor(Integer id, String name, String details, String image) {
        this.authorRepository.findById(id)
                .ifPresentOrElse(author -> {
                    author.setName(name);
                    author.setDetails(details);
                    author.setImage(image);

                    this.authorRepository.save(author);
                }, () -> {
                    throw new NoSuchElementException("Author not found");
                });
    }

    @Override
    @Transactional
    public void deleteAuthor(Integer id) {
        this.authorRepository.deleteById(id);
    }
}
