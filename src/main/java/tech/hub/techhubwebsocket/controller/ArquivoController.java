package tech.hub.techhubwebsocket.controller;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.service.arquivo.ArquivoService;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.arquivo.ftp.impl.FtpLocalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/arquivos")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "ftp.cloud.enabled", havingValue = "false", matchIfMissing = true)
public class ArquivoController {

    private final ArquivoService arquivoService;
    private final FtpLocalServiceImpl ftpLocalService;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Arquivo arquivo = this.arquivoService.getArquivo(id);
        byte[] imagem = this.ftpLocalService.getImage(arquivo.getNomeArquivoSalvo(),
              TipoArquivo.IMAGEM);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=" + arquivo.getNomeArquivoOriginal());

        return ResponseEntity.status(200).headers(headers).body(imagem);

    }

    @GetMapping("/usuario/{id}/imagem")
    public ResponseEntity<byte[]> imagensUsuario(
          @PathVariable Integer id,
          @RequestParam TipoArquivo tipoArquivo
    ) {

        Arquivo arquivo = this.arquivoService.getArquivo(id, tipoArquivo);
        byte[] imagem = this.ftpLocalService.getImage(arquivo.getNomeArquivoSalvo(), tipoArquivo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=" + arquivo.getNomeArquivoOriginal());

        return ResponseEntity.status(200).headers(headers).body(imagem);

    }


    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Arquivo arquivo = this.arquivoService.getArquivo(id);
        Resource resource = this.ftpLocalService.getFile(arquivo);
        String contentType = this.ftpLocalService.getContentType(resource);
        long fileSize;
        // Getting file size
        try {

            fileSize = resource.contentLength(); // It's important that resource should be able to provide this info
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                    + arquivo.getNomeArquivoOriginal() + "\"")
              .header(HttpHeaders.CONTENT_LENGTH,
                    String.valueOf(fileSize)) // Adding size to the headers
              .body(resource);
    }


    @GetMapping("/usuario/{id}")
    public ResponseEntity<Resource> arquivosUsuario(
          @PathVariable Integer id,
          @RequestParam TipoArquivo tipoArquivo
    ) {
        Arquivo arquivo = this.arquivoService.getArquivo(id, tipoArquivo);
        Resource resource = this.ftpLocalService.getFile(arquivo);
        String contentType = this.ftpLocalService.getContentType(resource);
        long fileSize;
        // Getting file size
        try {
            fileSize = resource.contentLength(); // It's important that resource should be able to provide this info
        } catch (IOException e) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                    + arquivo.getNomeArquivoOriginal() + "\"")
              .header(HttpHeaders.CONTENT_LENGTH,
                    String.valueOf(fileSize)) // Adding size to the headers
              .body(resource);
    }
}
