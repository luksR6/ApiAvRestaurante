package com.senac.ApiAvRestaurante.domain.repository;

import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
}