package tech.hub.techhubwebsocket.repository;

import tech.hub.techhubwebsocket.entity.perfil.Perfil;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil,Integer> {
//
//    @Query("UPDATE Perfil p SET p.sobreMim = :novoSobreMim WHERE p.id = :id")
//    void atualizarSobreMimPorId(Integer id,String novoSobreMim);
//
//    @Query("UPDATE Perfil p SET p.experiencia = :experiencia WHERE p.id = :id")
//    void atualizarExperienciaPorId(Integer perfilId, String experiencia);
//    @Query("UPDATE Perfil p SET p.descricao = :descricao WHERE p.id = :id")
//    void atualizarDescricaoPorId(Integer perfilId, String descricao);
//    @Query("UPDATE Perfil p SET p.pathPerfilImage = :pathPerfilImage WHERE p.id = :id")
//    void atualizarPathPerfilImagePorId(Integer perfilId, String pathPerfilImage);
//    @Query("UPDATE Perfil p SET p.pathWallpaperImage = :pathWallpaperImage WHERE p.id = :id")
//    void atualizarPathWallpaperImagePorId(Integer perfilId, String pathWallpaperImage);
//    @Query("UPDATE Perfil p SET p.precoMedio = :precoMedio WHERE p.id = :id")
//    void atualizarPrecoMedioPorId(Integer perfilId, String precoMedio);
//    @Query("UPDATE Perfil p SET p.linkGithub = :linkGithub WHERE p.id = :id")
//    void atualizarLinkGithub(Integer perfilId, String linkGithub);
//    @Query("UPDATE Perfil p SET p.linkLinkedin = :linkLinkedin WHERE p.id = :id")
//    void atualizarlinkLinkedinPorId(Integer perfilId, String linkLinkedin);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.id = :idUsuario")
    Optional<Perfil> encontrarPerfilPorIdUsuario(Integer idUsuario);

    @Transactional
    @Modifying
    @Query("UPDATE Perfil p SET p.usuario = :usuario WHERE p.id = :id")
    void atualizarUsuarioDoPerfil(Integer id, Usuario usuario);
}
