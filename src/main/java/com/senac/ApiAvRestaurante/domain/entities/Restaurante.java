package com.senac.ApiAvRestaurante.domain.entities;

import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteRequestDto;
import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurante")
public class    Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double mediaNota = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Usuario admin;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

}