package tech.hub.techhubwebsocket.service.arquivo.ftp;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import org.springframework.web.multipart.MultipartFile;

public interface FtpService {

    String getArquivoUrl(Integer id, boolean download);

    Arquivo salvarArquivo(MultipartFile arquivo, TipoArquivo tipoArquivo);

    Arquivo atualizarArquivo(Integer id, MultipartFile arquivo, TipoArquivo tipoArquivo);
}
