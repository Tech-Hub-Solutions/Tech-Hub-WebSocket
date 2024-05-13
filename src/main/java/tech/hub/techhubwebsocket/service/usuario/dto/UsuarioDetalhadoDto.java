package tech.hub.techhubwebsocket.service.usuario.dto;

import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.entity.usuario.UsuarioFuncao;

public record UsuarioDetalhadoDto(
        String nome,
        String email,
        String numeroCadastroPessoa,
        String pais,
        UsuarioFuncao funcao
) {

    public UsuarioDetalhadoDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getNumeroCadastroPessoa(),
                usuario.getPais(), usuario.getFuncao());
    }
}
