package com.enova.web.api.Services;



import com.enova.web.api.Models.Commons.mail.Msg;

import javax.mail.MessagingException;

public interface IsmtpMailService {
    void connect ();
    void sending( Msg msg ) throws MessagingException ;
    void sendingSimple( Msg msg ) throws MessagingException ;
    void sendingWithViewHTML( Msg msg ) throws MessagingException ;
    void sendingWithDocument( Msg msg ) throws MessagingException ;
    void sendingWithDocuments( Msg msg ) throws MessagingException;
    void sendingWithStreamDocuments( Msg msg ) throws MessagingException;
    void sendingMultiBodyContent( Msg msg ) throws MessagingException;
}
