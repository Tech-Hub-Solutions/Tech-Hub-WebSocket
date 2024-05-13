package tech.hub.techhubwebsocket.entity.perfil;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sobreMim;
    private String experiencia;
    private String descricao;
    private Double precoMedio;
    private String nomeGithub;
    private String linkGithub;
    private String linkLinkedin;

    @OneToOne(mappedBy = "perfil")
    private Usuario usuario;

    @OneToMany(mappedBy = "perfil")
    private List<Arquivo> arquivos;
}
