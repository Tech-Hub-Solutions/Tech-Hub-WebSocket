package tech.hub.techhubwebsocket.entity.usuario;

import tech.hub.techhubwebsocket.entity.perfil.Perfil;
import tech.hub.techhubwebsocket.entity.conversa.Conversa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Perfil perfil;

    private String nome;
    private String email;
    private String senha;
    private String numeroCadastroPessoa;
    private String pais;
    @Enumerated(EnumType.STRING)
    private UsuarioFuncao funcao;

    private boolean isUsing2FA;
    private boolean isValid2FA;
    private String secret;

    private boolean isAtivo;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Conversa> conversaList;

}
