package tech.hub.techhubwebsocket.entity.conversa;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roomCode;
    @OneToMany(mappedBy = "sala")
    private List<Conversa> conversaList;
    @OneToMany(mappedBy = "sala")
    private List<Mensagem> mensagemList;
}
