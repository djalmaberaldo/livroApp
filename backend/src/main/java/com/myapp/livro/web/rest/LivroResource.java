package com.myapp.livro.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.myapp.livro.domain.Livro;
import com.myapp.livro.service.LivroService;
import com.myapp.livro.web.rest.errors.BadRequestAlertException;
import com.myapp.livro.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Livro.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class LivroResource {

    private final Logger log = LoggerFactory.getLogger(LivroResource.class);

    private static final String ENTITY_NAME = "livroLivro";

    private final LivroService livroService;

    public LivroResource(LivroService livroService) {
        this.livroService = livroService;
    }

    /**
     * POST  /livros : Create a new livro.
     *
     * @param livro the livro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new livro, or with status 400 (Bad Request) if the livro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/livros")
    @Timed
    public ResponseEntity<Livro> createLivro(@RequestBody Livro livro) throws URISyntaxException {
        log.debug("REST request to save Livro : {}", livro);
        if (livro.getId() != null) {
            throw new BadRequestAlertException("A new livro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Livro result = livroService.save(livro);
        return ResponseEntity.created(new URI("/api/livros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /livros : Updates an existing livro.
     *
     * @param livro the livro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated livro,
     * or with status 400 (Bad Request) if the livro is not valid,
     * or with status 500 (Internal Server Error) if the livro couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/livros")
    @Timed
    public ResponseEntity<Livro> updateLivro(@RequestBody Livro livro) throws URISyntaxException {
        log.debug("REST request to update Livro : {}", livro);
        if (livro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Livro result = livroService.save(livro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, livro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /livros : get all the livros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of livros in body
     */
    @GetMapping("/livros")
    @Timed
    public List<Livro> getAllLivros() {
        log.debug("REST request to get all Livros");
        return livroService.findAll();
    }

    /**
     * GET  /livros/:id : get the "id" livro.
     *
     * @param id the id of the livro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the livro, or with status 404 (Not Found)
     */
    @GetMapping("/livros/{id}")
    @Timed
    public ResponseEntity<Livro> getLivro(@PathVariable Long id) {
        log.debug("REST request to get Livro : {}", id);
        Optional<Livro> livro = livroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livro);
    }

    /**
     * DELETE  /livros/:id : delete the "id" livro.
     *
     * @param id the id of the livro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/livros/{id}")
    @Timed
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        log.debug("REST request to delete Livro : {}", id);
        livroService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
