package tn.enova.Services.Interfaces;


import tn.enova.Configures.SMTPMailConfig;
import tn.enova.Enums.RecipientType;
import tn.enova.Enums.TypeBody;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Services.SmtpMailService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tn.enova.Models.Commons.mail.BodyContent;
import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Commons.mail.Recipient;

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

       // msg.setBodyContents(this.convertAllToEmbeddedImages(msg.getBodyContents()));


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
                if (content.getEmbeddeds()!=null){
                for (Map.Entry<String, BodyContent> entry : content.getEmbeddeds().entrySet()) {


                    String cid = entry.getKey();
                    byte[] imageData = Base64.getDecoder().decode(entry.getValue().getContent());

                    MimeBodyPart imageBodyPart = new MimeBodyPart();
                    DataSource fds = new ByteArrayDataSource(imageData, entry.getValue().getType().getMimeType());
                    imageBodyPart.setDataHandler(new DataHandler(fds));
                    imageBodyPart.setHeader("Content-ID", "<" + cid + ">");
                    imageBodyPart.setDisposition(MimeBodyPart.INLINE);

                    htmlMultipart.addBodyPart(imageBodyPart);
                }}


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