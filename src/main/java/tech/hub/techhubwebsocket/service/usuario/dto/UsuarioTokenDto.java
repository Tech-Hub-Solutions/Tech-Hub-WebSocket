package tech.hub.techhubwebsocket.service.usuario.dto;

import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.entity.usuario.UsuarioFuncao;
import tech.hub.techhubwebsocket.service.arquivo.ArquivoService;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UsuarioTokenDto(
      Integer id,
      String nome,
      @Enumerated(EnumType.STRING)
      UsuarioFuncao funcao,
      String pais,
      String urlFotoPerfil,
      Boolean isUsing2FA,
      String secretQrCodeUrl,
      String secret,
      String token
) {

    public UsuarioTokenDto(Usuario usuario, String token, String secretQrCodeUrl, String secret,
          FtpService ftpService) {
        this(
              usuario.getId(),
              usuario.getNome(),
              usuario.getFuncao(),
              usuario.getPais(),
              ftpService.getArquivoUrl(
                    ArquivoService.getArquivoOfPerfil(usuario.getPerfil(), TipoArquivo.PERFIL),
                    false),
              usuario.isUsing2FA(),
              secretQrCodeUrl,
              secret,
              token
        );
    }
}
