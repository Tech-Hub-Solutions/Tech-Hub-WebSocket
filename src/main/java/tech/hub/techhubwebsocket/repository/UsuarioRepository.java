package tech.hub.techhubwebsocket.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import tech.hub.techhubwebsocket.entity.perfil.Perfil;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.entity.usuario.UsuarioFuncao;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>,
      JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByEmailAndIsAtivoTrue(String email);

    Optional<Usuario> findUsuarioByEmailAndNumeroCadastroPessoa(String email,
          String numeroCadastroPessoa);

    Boolean existsByEmailAndIdNot(String email, Integer usuarioId);

    List<Usuario> findByIdIn(List<Integer> ids);

    Optional<Usuario> findUsuarioByIdAndIsAtivoTrue(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.perfil = :perfil WHERE u.id = :id")
    void atualizarPerfilDoUsuario(Integer id, Perfil perfil);

    Page<Usuario> findAllByFuncaoAndIsAtivoTrueAndEmailNotContains(UsuarioFuncao usuarioFuncao,
          String emailPattern, Pageable pageable);
}
