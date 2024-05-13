package tech.hub.techhubwebsocket.service.usuario;

import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;
import tech.hub.techhubwebsocket.service.usuario.autenticacao.AutenticacaoService;
import tech.hub.techhubwebsocket.service.usuario.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UsuarioMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AutenticacaoService autenticacaoService;

    public UsuarioDetalhadoDto dtoOf(Usuario usuario) {
        return new UsuarioDetalhadoDto(usuario);
    }



    public static UsuarioTokenDto of(Usuario usuario, String token, String secretQrCodeUrl,
          String secret, FtpService ftpService) {
        return new UsuarioTokenDto(usuario, token, secretQrCodeUrl, secret, ftpService);
    }


}
