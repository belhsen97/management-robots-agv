package tn.enova.Services;



import tn.enova.Models.Commons.mail.Msg;

import javax.mail.MessagingException;

public interface SmtpMailService {
    void sendingMessage( Msg msg ) throws MessagingException;
}
