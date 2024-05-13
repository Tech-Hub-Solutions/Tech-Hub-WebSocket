package tech.hub.techhubwebsocket.service.usuario.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAtualizacaoDto(
        @NotBlank
        String nome,

        @Email
        String email,

        @Nullable
        String pais,

        @NotBlank
        String senha,

        @NotNull
        boolean isUsing2FA

) {
}
