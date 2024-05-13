package tech.hub.techhubwebsocket.repository;

import tech.hub.techhubwebsocket.entity.conversa.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala,Integer> {
    Optional<Sala> findByRoomCode(String roomCode);

}
