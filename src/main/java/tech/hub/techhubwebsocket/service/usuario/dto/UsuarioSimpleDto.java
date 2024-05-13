package tech.hub.techhubwebsocket.service.usuario.dto;

import tech.hub.techhubwebsocket.entity.usuario.Usuario;

public record UsuarioSimpleDto(
        String nome,

        String email,

        String pais,

        String funcao,
        boolean isUsing2FA
) {
    public UsuarioSimpleDto(Usuario usuario) {
        this(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPais(),
                usuario.getFuncao().toString(),
                usuario.isUsing2FA()
        );
    }
}
