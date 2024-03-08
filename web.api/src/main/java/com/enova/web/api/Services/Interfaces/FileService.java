package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Libs.MockMultipartFile;
import com.enova.web.api.Services.IFileService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service("file-service")
public class FileService implements IFileService {
    public static String link_forgotPassword_HTML = "";
    public static String link_ConfirmMail_HTML = "";
    public static String link_UpdateNewUser_HTML= "";
    public static String  defaultUserPhoto = "";
    public static String pageHomeLink = "";
    public static String pageListUsersLink = "";
    public static String pathSignIn = "";
    public static String  pathLinkPasswordForgot= "";
    public static String  pathError = "";


    @Override
//"http://baeldung.com"
    public String Edit_ConfirmMailPage (String username ) throws IOException {
        //https://zetcode.com/java/jsoup/
        File input = ResourceUtils.getFile(link_ConfirmMail_HTML);
        Document doc = Jsoup.parse(input,  "UTF-8");
        Element herder_username = doc.getElementById("verify-mail-link-username-h1");
        Element paragrahelink = doc.getElementById("verify-mail-link-page-home-a");
        herder_username.text( "Dear "+ username+",");
        paragrahelink.attr("href",FileService.pageHomeLink);
        return doc.html();
    }

    @Override
//"http://baeldung.com"
    public String Edit_NewUser (String username,String email ) throws IOException {
        //https://zetcode.com/java/jsoup/
        File input = ResourceUtils.getFile(link_UpdateNewUser_HTML);
        Document doc = Jsoup.parse(input,  "UTF-8");
        Element herder_username = doc.getElementById("update-new-user-username-span");
        Element herder_email = doc.getElementById("update-new-user-email-span");
        Element sr_link = doc.getElementById("update-new-user-link-list-users-a");
        Element paragrahelink = doc.getElementById("update-new-user-link-page-home-a");
        herder_username.text(username);
        herder_email.text(email);
        sr_link.attr("href", FileService.pageListUsersLink );
        paragrahelink.attr("href",FileService.pageHomeLink);
        return doc.html();
    }
   @Override
//"http://baeldung.com"
   public String Edit_forgotPasswordPage (String username , String code   ) throws IOException {
     //https://zetcode.com/java/jsoup/
         File input = ResourceUtils.getFile(link_forgotPassword_HTML);
         Document doc = Jsoup.parse(input,  "UTF-8");
         Element herder_username = doc.getElementById("forgot-password-code-username-h1");
         Element paragrahecode = doc.getElementById("forgot-password-code-verification-p");
         Element sr_link = doc.getElementById("forgot-password-link-verification-a");
         Element paragrahelink = doc.getElementById("forgot-password-link-page-home-a");
         herder_username.text( "Hi "+ username+",");
         paragrahecode.text(code);
         sr_link.attr("href", pageHomeLink+pathLinkPasswordForgot+"/"+code );
         paragrahelink.attr("href",FileService.pageHomeLink);
         return doc.html();
   }
    @Override
   public String convert_page_to_String_UTF_8 ( String srcPage) throws IOException {
         File input = ResourceUtils.getFile(srcPage);
         return new String(Files.readAllBytes(input.toPath()), StandardCharsets.UTF_8);
   }
    @Override
    public void write_file_UTF_8 ( String srcPage , String data) throws IOException {
        File input = ResourceUtils.getFile(srcPage);
        PrintWriter writer = new PrintWriter(input,"UTF-8");
        writer.write(data) ;
        writer.flush();
        writer.close();
    }
    @Override
    public MultipartFile importFileToMultipartFile(String filePath) throws IOException {
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
}
