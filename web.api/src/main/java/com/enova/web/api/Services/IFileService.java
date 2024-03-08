package com.enova.web.api.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    String Edit_forgotPasswordPage (String username , String code   ) throws IOException;
    String Edit_ConfirmMailPage (String username ) throws IOException;
    String convert_page_to_String_UTF_8 ( String srcPage) throws IOException;
    void write_file_UTF_8 ( String srcPage , String data) throws IOException;
    MultipartFile importFileToMultipartFile(String filePath) throws IOException;
    String Edit_NewUser (String username,String email ) throws IOException ;

}
