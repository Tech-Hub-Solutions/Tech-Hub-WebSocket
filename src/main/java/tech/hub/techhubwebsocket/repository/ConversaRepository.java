package tech.hub.techhubwebsocket.repository;

import tech.hub.techhubwebsocket.entity.conversa.Conversa;
import tech.hub.techhubwebsocket.entity.conversa.Sala;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Integer> {
    boolean existsByUsuarioAndSalaAndIsAtivoTrue(Usuario usuario, Sala sala);

    Conversa findByUsuarioAndSala(Usuario usuarioAutenticado, Sala sala);

    List<Conversa> findByUsuario(Usuario usuarioAutenticado);

    List<Conversa> findByAndSalaIn(List<Sala> salasDoUsuario);

    List<Conversa> findBySala(Sala sala);

    List<Conversa> findBySalaRoomCode(String room);
}
