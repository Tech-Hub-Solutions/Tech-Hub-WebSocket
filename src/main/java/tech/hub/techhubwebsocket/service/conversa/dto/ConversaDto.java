package tech.hub.techhubwebsocket.service.conversa.dto;


import tech.hub.techhubwebsocket.entity.conversa.Conversa;
import tech.hub.techhubwebsocket.entity.conversa.Mensagem;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConversaDto {

    private String roomCode;
    private UsuarioConversaDto usuario;
    private MensagemASerEnviadaDto mensagem;

    public ConversaDto(Conversa conversa, FtpService ftpService) {
        this.roomCode = conversa.getSala().getRoomCode();
        this.usuario = new UsuarioConversaDto(conversa.getUsuario(), ftpService);
        this.mensagem = null;
    }

    public ConversaDto(Conversa conversa, Mensagem mensagem, FtpService ftpService) {
        this(conversa.getSala().getRoomCode(),
              new UsuarioConversaDto(conversa.getUsuario(), ftpService),
              new MensagemASerEnviadaDto(mensagem, ftpService));
    }
}
