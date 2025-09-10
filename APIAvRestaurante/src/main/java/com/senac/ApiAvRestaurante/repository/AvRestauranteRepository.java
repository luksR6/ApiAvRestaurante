package com.senac.ApiAvRestaurante.repository;

import com.senac.ApiAvRestaurante.model.Avaliacao;
import com.senac.ApiAvRestaurante.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvRestauranteRepository extends JpaRepository<Avaliacao, Long>{

    Optional<Usuario> findByEmailAndNome(String email, String nome);
}
