package com.enova.web.api.Services;



import com.enova.web.api.Models.Commons.mail.Msg;

import javax.mail.MessagingException;

public interface SmtpMailService {
//    void sending( SimpleMsg msg ) throws MessagingException ;
//    void sendingSimple( SimpleMsg msg ) throws MessagingException ;
//    void sendingWithViewHTML( SimpleMsg msg ) throws MessagingException ;
//    void sendingWithDocument( SimpleMsg msg ) throws MessagingException ;
//    void sendingWithDocuments( SimpleMsg msg ) throws MessagingException;
//    void sendingWithStreamDocuments( SimpleMsg msg ) throws MessagingException;
//    void sendingMultiBodyContent( SimpleMsg msg ) throws MessagingException;


    void sendingMessage( Msg msg ) throws MessagingException;
}
