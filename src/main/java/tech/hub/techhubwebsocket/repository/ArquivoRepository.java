package tech.hub.techhubwebsocket.repository;

import tech.hub.techhubwebsocket.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoRepository extends JpaRepository<Arquivo,Integer> {
}
