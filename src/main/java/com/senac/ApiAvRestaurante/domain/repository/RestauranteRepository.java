package com.senac.ApiAvRestaurante.domain.repository;

import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}