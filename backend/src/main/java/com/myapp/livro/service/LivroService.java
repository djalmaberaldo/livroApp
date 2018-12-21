package com.myapp.livro.service;

import com.myapp.livro.domain.Livro;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Livro.
 */
public interface LivroService {

    /**
     * Save a livro.
     *
     * @param livro the entity to save
     * @return the persisted entity
     */
    Livro save(Livro livro);

    /**
     * Get all the livros.
     *
     * @return the list of entities
     */
    List<Livro> findAll();


    /**
     * Get the "id" livro.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Livro> findOne(Long id);

    /**
     * Delete the "id" livro.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
