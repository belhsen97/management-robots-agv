package com.enova.web.api.Services;



import com.enova.web.api.Models.Commons.mail.Msg;

import javax.mail.MessagingException;

public interface SmtpMailService {
    void sendingMessage( Msg msg ) throws MessagingException;
}
