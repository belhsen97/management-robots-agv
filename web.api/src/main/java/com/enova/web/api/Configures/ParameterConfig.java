package com.enova.web.api.Configures;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ParameterConfig {

//    @Value("${emqx.admin.username}")
//    public String brokerUsername;
//    @Value("${emqx.admin.password}")
//    public String brokerPassword;
//    @Value("${emqx.url.authentication}")
//    public String brokerurl;

    @Value("${myApp.security.token.expired}")
    public long tokenExpired = 1000 * 60 * 24;

    @Value("${myApp.file.forgotPassword_HTML}")
    public String file_forgotPassword_HTML;
    @Value("${myApp.file.ConfirmMail_HTML}")
    public String file_ConfirmMail_HTML;

    @Value("${myApp.file.UpdateNewUser_HTML}")
    public String link_UpdateNewUser_HTML;

    @Value("${myApp.file.defaultUserPhoto}")
    public String file_defaultUserPhoto;
    @Value("${myApp.link.WebPage}")
    public String pageHomeLink;
    @Value("${myApp.link.ListUsers}")
    public String pageListUsersLink;
    @Value("${myApp.link.Path.signIn}")
    public String pathLinkSignIn;
    @Value("${myApp.link.Path.update_password_forgot}")
    public String pathLinkPasswordForgot;
    @Value("${myApp.link.Path.error}")
    public String pathLinkError;
    @Value("${myApp.link.Path.AttachementDowload}")
    public String pathAttachementDowload;
    @Value("${myApp.link.GlobalBackEnd}")
    public String linkGlobalBackEnd;
    @Value("${server.servlet.context-path}")
    public String pathServiceUser;
    public String staticLinkServiceUser;
    public static String host_ContextPath = "";

    @PostConstruct
    public void onCreate() {
        staticLinkServiceUser = linkGlobalBackEnd + pathServiceUser;
        ParameterConfig.host_ContextPath = linkGlobalBackEnd + pathServiceUser + pathAttachementDowload;
    }
}