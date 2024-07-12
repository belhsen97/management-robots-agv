package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Configures.SMTPMailConfig;
import com.enova.web.api.Enums.RecipientType;
import com.enova.web.api.Enums.TypeBody;
import com.enova.web.api.Models.Commons.mail.*;
import com.enova.web.api.Models.Commons.Attachment;
import com.enova.web.api.Services.SmtpMailService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.*;
import java.util.stream.Collectors;

@Service("service-mail-smtp")
@RequiredArgsConstructor
public class SMTPMailServiceImpl implements SmtpMailService {

    private final SMTPMailConfig smtpMailConfig;
    private final Session session;

    private BodyContent convertToEmbeddedImages(BodyContent bodyContent) {
        if (bodyContent.getType() != TypeBody.HTML || bodyContent.getContent() == null) {
            return bodyContent;
        }

        Document doc = Jsoup.parse(bodyContent.getContent());
        Elements imgTags = doc.select("img");

        for (Element imgTag : imgTags) {
            String src = imgTag.attr("src");

            if (src.startsWith("data:image/")) {
                String base64Data = src.substring(src.indexOf(",") + 1);
                String mimeType = src.substring(5, src.indexOf(";"));
                TypeBody imageType = TypeBody.valueOf(mimeType.split("/")[1].toUpperCase());

                String contentId = "image" + UUID.randomUUID().toString();

                imgTag.attr("src", "cid:" + contentId);

                BodyContent embeddedImage = BodyContent.builder()
                        .type(imageType)
                        .content(base64Data)
                        .build();
                if (bodyContent.getEmbeddeds() == null) {
                    bodyContent.setEmbeddeds(new HashMap<>());
                }
                bodyContent.getEmbeddeds().put(contentId, embeddedImage);
            }
        }

        bodyContent.setContent(doc.body().html());
        return bodyContent;
    }

    private List<BodyContent> convertAllToEmbeddedImages(List<BodyContent> bodyContents) {
        if (bodyContents == null) {
            return new ArrayList<>();
        }
        for (int i = 0; i < bodyContents.size(); i++) {
            if (bodyContents.get(i).getType() != TypeBody.HTML || bodyContents.get(i).getContent() == null) {
                continue;
            }
            BodyContent bodyContent = this.convertToEmbeddedImages(bodyContents.get(i));
            bodyContents.set(i, bodyContent);
        }
        return bodyContents;
    }

    @Override
    public void sendingMessage(Msg msg) throws MessagingException {
        // create and send the email with attachment
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpMailConfig.username));

        msg.setBodyContents(this.convertAllToEmbeddedImages(msg.getBodyContents()));


        List<Recipient> listRecipients = msg.getRecipientsByType(RecipientType.TO);
        if (!listRecipients.isEmpty()) {
            //     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(listRecipients.get(0).getAddress()));
            String toAddresses = listRecipients.stream()
                    .map(Recipient::getAddress)
                    .collect(Collectors.joining(","));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses));
        }
        listRecipients = msg.getRecipientsByType(RecipientType.CC);
        if (!listRecipients.isEmpty()) {
            //  message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(listRecipients.get(0).getAddress()));
            String toAddresses = listRecipients.stream()
                    .map(Recipient::getAddress)
                    .collect(Collectors.joining(","));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(toAddresses));
        }
        listRecipients = msg.getRecipientsByType(RecipientType.BCC);
        if (!listRecipients.isEmpty()) {
            //  message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(listRecipients.get(0).getAddress()));
            String toAddresses = listRecipients.stream()
                    .map(Recipient::getAddress)
                    .collect(Collectors.joining(","));
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(toAddresses));
        }


        message.setSubject(msg.getSubject());

        // add multiple body to  body part
        List<BodyPart> attachmentBodyParts = new ArrayList<>();
        for (BodyContent content : msg.getBodyContents()) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            if (content.getType() == TypeBody.HTML) {
                MimeMultipart htmlMultipart = new MimeMultipart("related");

                MimeBodyPart htmlBodyPart = new MimeBodyPart();
                htmlBodyPart.setContent(content.getContent(), content.getType().getMimeType());
                htmlMultipart.addBodyPart(htmlBodyPart);

                // Add embedded images
                for (Map.Entry<String, BodyContent> entry : content.getEmbeddeds().entrySet()) {


                    String cid = entry.getKey();
                    byte[] imageData = Base64.getDecoder().decode(entry.getValue().getContent());

                    MimeBodyPart imageBodyPart = new MimeBodyPart();
                    DataSource fds = new ByteArrayDataSource(imageData, entry.getValue().getType().getMimeType());
                    imageBodyPart.setDataHandler(new DataHandler(fds));
                    imageBodyPart.setHeader("Content-ID", "<" + cid + ">");
                    imageBodyPart.setDisposition(MimeBodyPart.INLINE);

                    htmlMultipart.addBodyPart(imageBodyPart);
                }


                messageBodyPart.setContent(htmlMultipart);
            } else {
                messageBodyPart.setContent(content.getContent(), content.getType().getMimeType());
            }
            attachmentBodyParts.add(messageBodyPart);
        }

        // add multiple attachement to  body part
        for (Attachment attachement : msg.getAttachements()) {
            DataSource source = new ByteArrayDataSource(attachement.getData(), "application/octet-stream");
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(attachement.getFileName());
            attachmentBodyParts.add(attachment);
        }

        // add the message body and attachments to the email
        Multipart multipart = new MimeMultipart();
        for (BodyPart attachmentBodyPart : attachmentBodyParts) {
            multipart.addBodyPart(attachmentBodyPart);
        }
        message.setContent(multipart);
        Transport.send(message);
    }
}


//  @Override
//    public void sending( SimpleMsg msg ) throws MessagingException  {
//      boolean dataEmpty = msg.getAttachements().stream().
//              allMatch(n -> ( n.getData() == null || n.getData().length == 0));
//      boolean nameFilePresent = msg.getAttachements().stream().
//              allMatch(n -> ! n.getFileName().isEmpty());
//
//      if ( msg.getAttachements().isEmpty() && msg.getBodyContents().size() > 0 ) {
//          this.sendingMultiBodyContent(  msg );
//          return ;
//      }
//      if ( msg.getAttachements().size() > 0  && !dataEmpty && nameFilePresent ) {
//          this.sendingWithStreamDocuments(  msg );
//          return ;
//      }
//      if ( msg.getAttachements().size() ==  1 && dataEmpty && nameFilePresent ) {
//          this.sendingWithDocument(  msg );
//          return ;
//      }
//      if ( msg.getAttachements().size() >  1 && dataEmpty && nameFilePresent ) {
//          this.sendingWithDocuments(  msg );
//      }
//    }
//    @Async("mail-smtp")
//    public void sendingMultiBodyContent( SimpleMsg msg ) throws MessagingException  {
//            // create and send the email with attachment
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
//            message.setSubject(msg.getSubject());
//
//            // create the HTML body
//            // create a list of attachments
//            List<BodyPart> attachmentBodyParts = new ArrayList<>();
//            for ( BodyContent content : msg.getBodyContents())
//            {
//                MimeBodyPart messageBodyPart = new MimeBodyPart();
//                messageBodyPart.setContent(content.getContent(), content.getType().getMimeType());
//                attachmentBodyParts.add(messageBodyPart);
//            }
//            // add the message body and attachments to the email
//            Multipart multipart = new MimeMultipart();
//            for (BodyPart attachmentBodyPart : attachmentBodyParts) {
//                multipart.addBodyPart(attachmentBodyPart);
//            }
//            message.setContent(multipart);
//
//            Transport.send(message);
//    }
//    @Override
//    public void sendingSimple( SimpleMsg msg ) throws MessagingException  {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(msg.getTo()));
//            message.setSubject(msg.getSubject());
//            message.setText(msg.getText());
//            Transport.send(message);
//    }
//    @Override
//    public void sendingWithViewHTML( SimpleMsg msg ) throws MessagingException  {
//            // create and send the email with attachment
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
//            message.setSubject(msg.getSubject());
//
//            // create the HTML body
//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            //messageBodyPart.setContent(msg.getBody(), "text/html");
//            messageBodyPart.setContent(msg.getBodyContents().get(0).getContent(), msg.getBodyContents().get(0).getType().getMimeType());
//            // add the HTML body to the email
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            message.setContent(multipart);
//
//            Transport.send(message);
//    }
//    @Override
//    public void sendingWithDocument( SimpleMsg msg ) throws MessagingException {
//
//            // create and send the email with attachment
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
//            message.setSubject(msg.getSubject());
//
//            // create the message body
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText(msg.getText());
//
//            // create the attachment
//            BodyPart attachmentBodyPart = new MimeBodyPart();
//            DataSource source = new FileDataSource(new File(msg.getAttachements().get(0).getFileName()));
//            attachmentBodyPart.setDataHandler(new DataHandler(source));
//            attachmentBodyPart.setFileName(msg.getAttachements().get(0)._getNameFile_());
//
//            // add the message body and attachment to the email
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            multipart.addBodyPart(attachmentBodyPart);
//            message.setContent(multipart);
//
//            Transport.send(message);
//    }
//    @Override
//    public void sendingWithDocuments( SimpleMsg msg ) throws MessagingException {
//            // create and send the email with attachments
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
//            message.setSubject(msg.getSubject());
//
//
//
//
//            // create the message body
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText(msg.getText());
//
//
//            // create a list of attachments
//            List<BodyPart> attachmentBodyParts = new ArrayList<>();
//            for ( Attachment attachement : msg.getAttachements() )
//            {
//                attachmentBodyParts.add(createAttachmentBodyPart(attachement.getFileName()));
//            }
//
//            // add the message body and attachments to the email
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//           for (BodyPart attachmentBodyPart : attachmentBodyParts) {
//                multipart.addBodyPart(attachmentBodyPart);
//            }
//            message.setContent(multipart);
//            Transport.send(message);
//    }
//    private  BodyPart createAttachmentBodyPart(String filePath) throws MessagingException {
//        BodyPart attachmentBodyPart = new MimeBodyPart();
//        DataSource source = new FileDataSource(new File(filePath));
//        attachmentBodyPart.setDataHandler(new DataHandler(source));
//        attachmentBodyPart.setFileName(new File(filePath).getName());
//        return attachmentBodyPart;
//    }
//
//    @Override
//    public void sendingWithStreamDocuments( SimpleMsg msg ) throws MessagingException {
//            // create and send the email with attachments
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpMailConfig.username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.getTo()));
//            message.setSubject(msg.getSubject());
//
//            // create the message body
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText(msg.getText());
//
//
//            // create a list of attachments
//            List<BodyPart> attachmentBodyParts = new ArrayList<>();
//            for ( Attachment attachement : msg.getAttachements() )
//            {
//                DataSource source = new ByteArrayDataSource(attachement.getData(), "application/octet-stream");
//                MimeBodyPart attachment = new MimeBodyPart();
//                attachment.setDataHandler(new DataHandler(source));
//                attachment.setFileName(attachement.getFileName());
//                attachmentBodyParts.add(attachment);
//            }
//
//            // add the message body and attachments to the email
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            for (BodyPart attachmentBodyPart : attachmentBodyParts) {
//                multipart.addBodyPart(attachmentBodyPart);
//            }
//            message.setContent(multipart);
//            Transport.send(message);
//    }


