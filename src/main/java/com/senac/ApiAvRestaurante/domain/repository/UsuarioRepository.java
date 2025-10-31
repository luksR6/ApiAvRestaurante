package com.senac.ApiAvRestaurante.domain.repository;

import com.senac.ApiAvRestaurante.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndNome(String email, String nome);

    boolean existsUsuarioByEmailContainingAndSenha(String email, String senha); // existe um usuario que tem esse email e a senha é igual

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndTokenSenha(String email, String tokenSenha);

    Optional<Usuario> findByCpf(String cpf);
}