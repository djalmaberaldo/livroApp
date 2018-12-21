package com.myapp.livro.web.rest;

import com.myapp.livro.LivroApp;

import com.myapp.livro.domain.Livro;
import com.myapp.livro.repository.LivroRepository;
import com.myapp.livro.service.LivroService;
import com.myapp.livro.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.myapp.livro.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LivroResource REST controller.
 *
 * @see LivroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LivroApp.class)
public class LivroResourceIntTest {

    private static final String DEFAULT_ISBN = "AAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_EDITORA = "AAAAAAAAAA";
    private static final String UPDATED_EDITORA = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDICAO = 1;
    private static final Integer UPDATED_EDICAO = 2;

    private static final Double DEFAULT_PRECO = 1D;
    private static final Double UPDATED_PRECO = 2D;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroService livroService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLivroMockMvc;

    private Livro livro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LivroResource livroResource = new LivroResource(livroService);
        this.restLivroMockMvc = MockMvcBuilders.standaloneSetup(livroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livro createEntity(EntityManager em) {
        Livro livro = new Livro()
            .isbn(DEFAULT_ISBN)
            .titulo(DEFAULT_TITULO)
            .autor(DEFAULT_AUTOR)
            .editora(DEFAULT_EDITORA)
            .edicao(DEFAULT_EDICAO)
            .preco(DEFAULT_PRECO);
        return livro;
    }

    @Before
    public void initTest() {
        livro = createEntity(em);
    }

    @Test
    @Transactional
    public void createLivro() throws Exception {
        int databaseSizeBeforeCreate = livroRepository.findAll().size();

        // Create the Livro
        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isCreated());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate + 1);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testLivro.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testLivro.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testLivro.getEditora()).isEqualTo(DEFAULT_EDITORA);
        assertThat(testLivro.getEdicao()).isEqualTo(DEFAULT_EDICAO);
        assertThat(testLivro.getPreco()).isEqualTo(DEFAULT_PRECO);
    }

    @Test
    @Transactional
    public void createLivroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = livroRepository.findAll().size();

        // Create the Livro with an existing ID
        livro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivroMockMvc.perform(post("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLivros() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get all the livroList
        restLivroMockMvc.perform(get("/api/livros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livro.getId().intValue())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR.toString())))
            .andExpect(jsonPath("$.[*].editora").value(hasItem(DEFAULT_EDITORA.toString())))
            .andExpect(jsonPath("$.[*].edicao").value(hasItem(DEFAULT_EDICAO)))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get the livro
        restLivroMockMvc.perform(get("/api/livros/{id}", livro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(livro.getId().intValue()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR.toString()))
            .andExpect(jsonPath("$.editora").value(DEFAULT_EDITORA.toString()))
            .andExpect(jsonPath("$.edicao").value(DEFAULT_EDICAO))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLivro() throws Exception {
        // Get the livro
        restLivroMockMvc.perform(get("/api/livros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLivro() throws Exception {
        // Initialize the database
        livroService.save(livro);

        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Update the livro
        Livro updatedLivro = livroRepository.findById(livro.getId()).get();
        // Disconnect from session so that the updates on updatedLivro are not directly saved in db
        em.detach(updatedLivro);
        updatedLivro
            .isbn(UPDATED_ISBN)
            .titulo(UPDATED_TITULO)
            .autor(UPDATED_AUTOR)
            .editora(UPDATED_EDITORA)
            .edicao(UPDATED_EDICAO)
            .preco(UPDATED_PRECO);

        restLivroMockMvc.perform(put("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLivro)))
            .andExpect(status().isOk());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testLivro.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testLivro.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testLivro.getEditora()).isEqualTo(UPDATED_EDITORA);
        assertThat(testLivro.getEdicao()).isEqualTo(UPDATED_EDICAO);
        assertThat(testLivro.getPreco()).isEqualTo(UPDATED_PRECO);
    }

    @Test
    @Transactional
    public void updateNonExistingLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Create the Livro

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivroMockMvc.perform(put("/api/livros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livro)))
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLivro() throws Exception {
        // Initialize the database
        livroService.save(livro);

        int databaseSizeBeforeDelete = livroRepository.findAll().size();

        // Get the livro
        restLivroMockMvc.perform(delete("/api/livros/{id}", livro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livro.class);
        Livro livro1 = new Livro();
        livro1.setId(1L);
        Livro livro2 = new Livro();
        livro2.setId(livro1.getId());
        assertThat(livro1).isEqualTo(livro2);
        livro2.setId(2L);
        assertThat(livro1).isNotEqualTo(livro2);
        livro1.setId(null);
        assertThat(livro1).isNotEqualTo(livro2);
    }
}
