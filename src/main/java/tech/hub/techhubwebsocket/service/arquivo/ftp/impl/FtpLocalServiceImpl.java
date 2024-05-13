package tech.hub.techhubwebsocket.service.arquivo.ftp.impl;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.repository.ArquivoRepository;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "ftp.cloud.enabled", havingValue = "false", matchIfMissing = true)
public class FtpLocalServiceImpl implements FtpService {

    private final ArquivoRepository arquivoRepository;

    private Path diretorioBase = Path.of(System.getProperty("user.dir") + "/arquivos");
    @Value("${app.useCurrentContextPathInImageUrl}")
    private boolean useCurrentContextPathInImageUrl;

    private static boolean useCurrentContextPathInImageUrlStatic;

    @Value("${app.useCurrentContextPathInImageUrl}")
    public void setUseCurrentContextPathInImageUrlStatic(boolean name) {
        FtpLocalServiceImpl.useCurrentContextPathInImageUrlStatic = name;
    }

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static String contextPathStatic;

    @Value("${server.servlet.context-path}")
    public void setContextPathStatic(String name) {
        FtpLocalServiceImpl.contextPathStatic = name;
    }

    @Override
    public String getArquivoUrl(Integer id, boolean download) {
        if (id == null) {
            return null;
        }

        Optional<Arquivo> arquivoOpt = this.arquivoRepository.findById(id);
        if (arquivoOpt.isEmpty()) {
            return null;
        }

        Arquivo arquivo = arquivoOpt.get();

        TipoArquivo tipoArquivo = arquivo.getTipoArquivo();

        String pathUrl = useCurrentContextPathInImageUrlStatic
              ? "/arquivos"
              : contextPathStatic + "/arquivos";

        switch (tipoArquivo) {
            case DOCUMENTO:
                pathUrl += "/file/{id}";
                break;
            case IMAGEM:
                pathUrl += "/image/{id}";
                break;
            default:
                pathUrl += "/usuario/{id}"
                      + (!tipoArquivo.equals(TipoArquivo.CURRICULO) ? "/imagem" : "");

                if (!useCurrentContextPathInImageUrl) {
                    return pathUrl;
                }
                return ServletUriComponentsBuilder
                      .fromCurrentContextPath()
                      .path(pathUrl)
                      .queryParam("tipoArquivo", tipoArquivo)
                      .buildAndExpand(arquivo.getPerfil().getUsuario().getId())
                      .toUri()
                      .toString();
        }

        if (!useCurrentContextPathInImageUrl) {
            return pathUrl.replace("{id}", id.toString());
        }

        return ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path(pathUrl)
              .buildAndExpand(id)
              .toUri()
              .toString();
    }


    @Override
    public Arquivo salvarArquivo(MultipartFile arquivo, TipoArquivo tipo) {

        if (arquivo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!diretorioBase.toFile().exists()) {
            diretorioBase.toFile().mkdir();
        }

        Path diretorioParaSalvar = Path.of(this.diretorioBase + "/" + tipo.toString());
        if (!diretorioParaSalvar.toFile().exists()) {
            diretorioParaSalvar.toFile().mkdir();
        }

        String nomeArquivoFormatado = gerarNomeArquivo(arquivo.getOriginalFilename());

        String diretorioFinal = diretorioParaSalvar + "/" + nomeArquivoFormatado;
        File destino = new File(diretorioFinal);

        try {
            arquivo.transferTo(destino);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(422, "Não foi possível salvar o arquivo", null);
        }

        Arquivo newArquivo = new Arquivo();
        newArquivo.setNomeArquivoOriginal(arquivo.getOriginalFilename());
        newArquivo.setNomeArquivoSalvo(nomeArquivoFormatado);
        newArquivo.setTipoArquivo(tipo);
        return this.arquivoRepository.save(newArquivo);

    }

    @Override
    public Arquivo atualizarArquivo(Integer id, MultipartFile arquivo, TipoArquivo tipoArquivo) {
        Arquivo arquivoAtual = this.arquivoRepository.findById(id)
              .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                          "Arquivo não encontrado")
              );

        deletarArquivo(arquivoAtual);

        return salvarArquivo(arquivo, tipoArquivo);
    }

    private String gerarNomeArquivo(String nomeOriginal) {
        return String.format("%s_%s", UUID.randomUUID(), nomeOriginal);
    }


    public byte[] getImage(String nomeArquivo, TipoArquivo tipoArquivo) {
        try {
            Path path = Path.of(this.diretorioBase + "/" + tipoArquivo.toString());
            Path filePath = Paths.get(path.toString(), nomeArquivo);
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found", e);
        }
    }

    public Resource getFile(Arquivo arquivo) {
        try {
            Path path = Path.of(this.diretorioBase + "/" + arquivo.getTipoArquivo().toString());
            Path filePath = Paths.get(path.toString(), arquivo.getNomeArquivoSalvo());
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(422), "Erro ao ler o arquivo");
        }
    }

    public String getContentType(Resource resource) {
        try {
            return Files.probeContentType(Paths.get(resource.getURI()));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(422),
                  "Erro ao determinar o tipo de arquivo");
        }
    }

    public void deletarArquivo(Arquivo arquivo) {

        this.arquivoRepository.delete(arquivo);

        Path path = Path.of(this.diretorioBase + "/" + arquivo.getTipoArquivo().toString());
        Path filePath = Paths.get(path.toString(), arquivo.getNomeArquivoSalvo());

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(422, "Não foi possível deletar o arquivo", null);
        }

    }


}
