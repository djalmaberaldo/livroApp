package com.myapp.livro.service.impl;

import com.myapp.livro.service.LivroService;
import com.myapp.livro.domain.Livro;
import com.myapp.livro.repository.LivroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Livro.
 */
@Service
@Transactional
public class LivroServiceImpl implements LivroService {

    private final Logger log = LoggerFactory.getLogger(LivroServiceImpl.class);

    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    /**
     * Save a livro.
     *
     * @param livro the entity to save
     * @return the persisted entity
     */
    @Override
    public Livro save(Livro livro) {
        log.debug("Request to save Livro : {}", livro);
        return livroRepository.save(livro);
    }

    /**
     * Get all the livros.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Livro> findAll() {
        log.debug("Request to get all Livros");
        return livroRepository.findAll();
    }


    /**
     * Get one livro by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> findOne(Long id) {
        log.debug("Request to get Livro : {}", id);
        return livroRepository.findById(id);
    }

    /**
     * Delete the livro by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Livro : {}", id);
        livroRepository.deleteById(id);
    }
}
