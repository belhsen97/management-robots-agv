package tn.enova.Services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import tn.enova.Models.Commons.Attachment;

import java.io.IOException;

public interface FileService {
    Attachment downloadStantardPhoto(String nameFile) throws IOException;
}
