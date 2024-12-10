package tn.enova.Services.Interfaces;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.enova.Libs.MockMultipartFile;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Services.DrivelessService;
import tn.enova.Services.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service("file-service")
public class FileServiceImpl implements FileService {
    private MultipartFile importFileToMultipartFile(String filePath) throws IOException {
        File file = ResourceUtils.getFile(filePath);
        if (file.exists()) {
            Path path = Paths.get(filePath);
            String name = file.getName();
            String originalFileName = name.substring(0, name.lastIndexOf('.'));
            String contentType = Files.probeContentType(path);
            byte[] content = Files.readAllBytes(path);
            return new MockMultipartFile(originalFileName, name, contentType, content);
        }
        return null;
    }

    @Override
    public Attachment downloadStantardPhoto(String nameFile) throws IOException {
        try {
            MultipartFile  file = this.importFileToMultipartFile(nameFile);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if(fileName.contains("..")) {   throw  new IOException("Filename contains invalid path sequence" + fileName); }
            return new Attachment(fileName,file.getContentType(),file.getSize(),file.getBytes());
        }   catch (IOException e) {   throw new IOException(e.getMessage());  }
    }

}
