    package com.senac.ApiAvRestaurante.domain.repository;

    import com.senac.ApiAvRestaurante.domain.entities.Avaliacao;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

    @Repository
    public interface AvRestauranteRepository extends JpaRepository<Avaliacao, Long> {

        @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.restaurante.id = :restauranteId")
        Double calcularMediaPorRestauranteId(@Param("restauranteId") Long restauranteId);

        List<Avaliacao> findByRestauranteId(Long restauranteId);

        List<Avaliacao> findByUsuarioId(Long usuarioId);
    }
