package com.myapp.livro.repository;

import com.myapp.livro.domain.Livro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Livro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

}
