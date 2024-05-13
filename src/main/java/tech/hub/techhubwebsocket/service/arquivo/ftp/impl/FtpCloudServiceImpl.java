package tech.hub.techhubwebsocket.service.arquivo.ftp.impl;

import static tech.hub.techhubwebsocket.utils.FileConverter.convertToFile;

import tech.hub.techhubwebsocket.entity.Arquivo;
import tech.hub.techhubwebsocket.repository.ArquivoRepository;
import tech.hub.techhubwebsocket.service.arquivo.ftp.FtpService;
import tech.hub.techhubwebsocket.service.arquivo.TipoArquivo;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.*;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "ftp.cloud.enabled", havingValue = "true")
public class FtpCloudServiceImpl implements FtpService {

    private final Cloudinary cloudinary;
    private final ArquivoRepository arquivoRepository;

    public String getArquivoUrl(Integer id, boolean download) {
        if (id == null) {
            return null;
        }

        Arquivo arquivo = arquivoRepository.findById(id)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Arquivo não encontrado"));

        try {
            String nomeArquivoSalvo = arquivo.getNomeArquivoSalvo();
            Map<String, String> nomeArquivoSalvoMap = convertStringToMap(nomeArquivoSalvo);
            String resourceType = nomeArquivoSalvoMap.get("resource_type");

            if (resourceType.equals("raw") || download) {
                String publicId = nomeArquivoSalvoMap.get("public_id");

                return cloudinary.url()
                      .resourceType(resourceType)
                      .transformation(new Transformation().flags(
                            "attachment:" + arquivo.getNomeArquivoOriginal().split("\\.")[0])
                      ).generate(publicId);
            }

            return nomeArquivoSalvoMap.get("secure_url");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Arquivo salvarArquivo(MultipartFile arquivo, TipoArquivo tipoArquivo) {
        try {
            Map uploadResult = cloudinary
                  .uploader()
                  .upload(convertToFile(arquivo), getParams(tipoArquivo, arquivo));

            Arquivo arquivoSalvo = Arquivo.builder()
                  .nomeArquivoOriginal(arquivo.getOriginalFilename())
                  .nomeArquivoSalvo(filterMapRespose(uploadResult).toString())
                  .tipoArquivo(tipoArquivo)
                  .build();

            return arquivoRepository.save(arquivoSalvo);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                  "Erro ao salvar arquivo");
        }
    }


    public Arquivo atualizarArquivo(Integer id, MultipartFile arquivo, TipoArquivo tipoArquivo) {

        Arquivo arquivoAtual = arquivoRepository.findById(id)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Arquivo não encontrado"));

        try {
            Map uploadResult = cloudinary
                  .uploader()
                  .upload(convertToFile(arquivo), getParams(tipoArquivo, arquivo));

            arquivoAtual.setNomeArquivoOriginal(arquivo.getOriginalFilename());
            arquivoAtual.setNomeArquivoSalvo(filterMapRespose(uploadResult).toString());
            arquivoAtual.setTipoArquivo(tipoArquivo);
            arquivoAtual.setDataUpload(LocalDate.now());

            return arquivoRepository.save(arquivoAtual);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                  "Erro ao atualizar arquivo");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                  "Erro ao atualizar arquivo");
        }
    }

    private Map<String, String> getParams(TipoArquivo tipoArquivo, MultipartFile arquivo) {
        var a = arquivo.getOriginalFilename();
        return ObjectUtils.asMap(
              "folder", tipoArquivo.name(),
              "use_filename", true,
              "use_filename_as_display_name", true,
              "resource_type", "auto",
              "format", arquivo.getOriginalFilename().split("\\.")[1]
        );
    }

    private Map<String, String> convertStringToMap(String data) {
        Map<String, String> map = new HashMap<>();
        if (!data.contains("{") || !data.contains("}")) {
            return map;
        }
        StringTokenizer st = new StringTokenizer(data, "{}=, ");
        while (st.hasMoreTokens()) {
            map.put(st.nextToken(), st.nextToken());
        }
        return map;
    }

    private Map<String, String> filterMapRespose(Map map) {
        Map<String, String> nomeArquivoSalvoMap = new HashMap<>();
        nomeArquivoSalvoMap.put("public_id", map.get("public_id").toString());
        nomeArquivoSalvoMap.put("asset_id", map.get("asset_id").toString());
        nomeArquivoSalvoMap.put("resource_type", map.get("resource_type").toString());
        nomeArquivoSalvoMap.put("secure_url", map.get("secure_url").toString());
        nomeArquivoSalvoMap.put("format", map.get("format").toString());
        nomeArquivoSalvoMap.put("width", map.get("width").toString());
        nomeArquivoSalvoMap.put("height", map.get("height").toString());

        return nomeArquivoSalvoMap;
    }
}
