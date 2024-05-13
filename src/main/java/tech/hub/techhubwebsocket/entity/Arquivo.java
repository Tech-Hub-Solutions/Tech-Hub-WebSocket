package tech.hub.techhubwebsocket.entity;

import tech.hub.techhubwebsocket.entity.conversa.Mensagem;
import tech.hub.techhubwebsocket.entity.perfil.Perfil;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Arquivo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String nomeArquivoOriginal;
  private String nomeArquivoSalvo;
  private LocalDate dataUpload;
  @Enumerated(EnumType.STRING)
  private TipoArquivo tipoArquivo;
  @ManyToOne
  private Mensagem mensagem;
  @ManyToOne
  private Perfil perfil;
}
