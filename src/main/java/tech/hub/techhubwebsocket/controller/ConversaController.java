package tech.hub.techhubwebsocket.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import tech.hub.techhubwebsocket.entity.conversa.Mensagem;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.service.arquivo.ArquivoService;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.conversa.ConversaService;
import tech.hub.techhubwebsocket.service.conversa.dto.ConversaDto;
import tech.hub.techhubwebsocket.service.conversa.dto.MensagemASerEnviadaDto;
import tech.hub.techhubwebsocket.service.conversa.dto.RoomCodeDto;

@RestController
@RequestMapping("/conversas")
@RequiredArgsConstructor
public class
ConversaController {

    private final ConversaService conversaService;
    private final ArquivoService arquivoService;

    @GetMapping
    public ResponseEntity<List<ConversaDto>> carregarConversas() {
        List<ConversaDto> conversaDtos = this.conversaService.listarConversas();

        if (conversaDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(conversaDtos);
    }

    @PostMapping("/iniciar/{id}")
    public ResponseEntity<RoomCodeDto> cadastrarSala(@PathVariable Integer id) {
        return ResponseEntity.ok(conversaService.iniciarConversa(id));
    }

    @PostMapping("/sala/{roomCode}")
    public ResponseEntity<Object> enviarMensagem(
          @PathVariable String roomCode,
          @RequestParam String mensagem,
          @RequestParam(required = false) MultipartFile arquivo,
          @RequestParam(required = false) TipoArquivo tipoArquivo) {

        this.conversaService.enviarMensagem(roomCode, mensagem, arquivo, tipoArquivo);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/sala/{roomCode}")
    public ResponseEntity<List<MensagemASerEnviadaDto>> getMensagens(
          @PathVariable String roomCode) {
        List<MensagemASerEnviadaDto> mensagens = this.conversaService.listarMensagens(roomCode);
        if (mensagens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(mensagens);
    }

    @DeleteMapping("/sala/{roomCode}")
    public ResponseEntity<Object> apagarConversa(@PathVariable String roomCode) {
        this.conversaService.apagarConversa(roomCode);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gerar-csv/{room}")
    public ResponseEntity<Resource> gerarCsvConversa(@PathVariable String room) {
        List<Mensagem> mensagens = this.conversaService.listarMensagensBanco(room);
        List<Usuario> usuarios = this.conversaService.listarUsuarios(room);

        Resource resource = this.arquivoService.gerarCsvConversa(mensagens, usuarios);
        String contentType;
        long fileSize;

        // Getting file size
        try {
            fileSize = resource.contentLength(); // It's important that resource should be able to provide
            contentType = Files.probeContentType(Paths.get(resource.getURI()));
            // this info
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        String nome = "Conversa-" + LocalDateTime.now()
              .format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss")) + ".csv";

        return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                    + nome + "\"")
              .header(HttpHeaders.CONTENT_LENGTH,
                    String.valueOf(fileSize)) // Adding size to the headers
              .body(resource);
    }

}
