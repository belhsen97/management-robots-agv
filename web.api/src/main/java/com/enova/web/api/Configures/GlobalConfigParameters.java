package com.enova.web.api.Configures;

import com.enova.web.api.Services.Interfaces.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfigParameters {
    @Value("${myApp.file.forgotPassword_HTML}")
    private String file_forgotPassword_HTML;
    @Value("${myApp.file.ConfirmMail_HTML}")
    private String file_ConfirmMail_HTML;
    @Value("${myApp.file.UpdateNewUser_HTML}")
    private String file_UpdateNewUser_HTML;
    @Value("${myApp.file.defaultUserPhoto}")
    private String file_defaultUserPhoto;
    @Value("${myApp.link.WebPage}")
    private String pageHomeWebPage;
    @Value("${myApp.link.ListUsers}")
    private String pathListUsersPage;
    @Value("${myApp.link.Path.signIn}")
    private String pathLinkSignIn;
    @Value("${myApp.link.Path.update_password_forgot}")
    private String pathLinkPasswordForgot;
    @Value("${myApp.link.Path.error}")
    private String pathLinkError;
    @Value("${myApp.link.Path.AttachementDowload}")
    private String pathAttachementDowload;
    @Value("${myApp.link.GlobalBackEnd}")
    private String linkGlobalBackEnd;
    @Value("${server.servlet.context-path}")
    private String pathServiceUser;
    public static String staticLinkServiceUser;
    public static String  host_ContextPath = "";
    @Bean
    public String configure() {
        GlobalConfigParameters.host_ContextPath = linkGlobalBackEnd + pathServiceUser + pathAttachementDowload;
        //http://localhost:8099/biochar/user-service/attachment/download/
        FileService.link_forgotPassword_HTML = file_forgotPassword_HTML;
        FileService.link_ConfirmMail_HTML = file_ConfirmMail_HTML;
        FileService.link_UpdateNewUser_HTML = file_UpdateNewUser_HTML ;
        FileService.defaultUserPhoto = file_defaultUserPhoto;
        FileService.pageHomeLink = pageHomeWebPage;
        FileService.pageListUsersLink =  pathListUsersPage;
        FileService.pathSignIn = pathLinkSignIn;
        FileService.pathError =pathLinkError;
        FileService.pathLinkPasswordForgot = pathLinkPasswordForgot;
        GlobalConfigParameters.staticLinkServiceUser = linkGlobalBackEnd + pathServiceUser;
        return "configured";
    }

}