package tech.hub.techhubwebsocket.service.usuario.dto;

import tech.hub.techhubwebsocket.entity.usuario.UsuarioFuncao;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCriacaoDto(
        @NotBlank
        String nome,

        @Email
        String email,

        @NotBlank
        String senha,

        String numeroCadastroPessoa,

        @Nullable
        String pais,

        @NotNull
        UsuarioFuncao funcao,

        @NotNull
        boolean isUsing2FA
) {
}
