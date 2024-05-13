package tech.hub.techhubwebsocket.service.conversa.dto;

import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.service.arquivo.ArquivoService;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;

public record UsuarioConversaDto(
      Integer id,
      String nome,
      String urlFotoPerfil
) {

    public UsuarioConversaDto(Usuario usuario, FtpService ftpService) {
        this(
              usuario.getId(), usuario.getNome(),
              ftpService.getArquivoUrl(
                    ArquivoService.getArquivoOfPerfil(usuario.getPerfil(), TipoArquivo.PERFIL),
                    false)
        );
    }

}
