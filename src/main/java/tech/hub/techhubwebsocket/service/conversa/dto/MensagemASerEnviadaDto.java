package tech.hub.techhubwebsocket.service.conversa.dto;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.entity.conversa.Mensagem;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;

import java.time.LocalDateTime;

public record MensagemASerEnviadaDto(
      Integer usuarioId,
      String texto,
      LocalDateTime dtHora,
      String urlArquivo,
      TipoArquivo tipoArquivo
) {

    public MensagemASerEnviadaDto(Mensagem mensagem, FtpService ftpService) {
        this(
              mensagem.getUsuario().getId(),
              mensagem.getTexto(),
              mensagem.getDtMensagem(),
              gerarUrlArquivo(mensagem, ftpService),
              obterTipoArquivo(mensagem)
        );
    }

    private static String gerarUrlArquivo(Mensagem mensagem, FtpService ftpService) {
        if (mensagem.getArquivos() != null) {

            if (mensagem.getArquivos().isEmpty()) {
                return null;
            }
            Arquivo arquivo = mensagem.getArquivos().get(0);
            return ftpService.getArquivoUrl(arquivo.getId(), TipoArquivo.DOCUMENTO.equals(arquivo.getTipoArquivo()));
        }

        return null;
    }

    private static TipoArquivo obterTipoArquivo(Mensagem mensagem) {
        // Verificando se a lista não é null e não está vazia
        if (mensagem.getArquivos() != null && !mensagem.getArquivos().isEmpty()) {
            return mensagem.getArquivos().get(0).getTipoArquivo();
        }
        return null;
    }
}
