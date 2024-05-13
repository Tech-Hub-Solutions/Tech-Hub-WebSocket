package tech.hub.techhubwebsocket.repository;

import tech.hub.techhubwebsocket.entity.conversa.Mensagem;
import tech.hub.techhubwebsocket.entity.conversa.Sala;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {
    Optional<Mensagem> findFirstBySalaOrderByDtMensagemDesc(Sala sala);

    int countBySalaAndUsuario(Sala sala, Usuario usuario);

}
