package tech.hub.techhubwebsocket.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

public class FileConverter {
    public static File convertToFile(MultipartFile arquivo) {
        try {
            String fileName = Objects.nonNull(arquivo.getOriginalFilename()) ?
                  arquivo.getOriginalFilename() : UUID.randomUUID().toString();
            File convFile = File.createTempFile(fileName, null);
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(arquivo.getBytes());
            fos.close();
            return convFile;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                  "Erro ao salvar arquivo");
        }
    }
}
