package com.MultiModule.User.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private String link = "http://localhost:4200";
    private final String templatePath = "templates/mailReset.html";
    private final String templatePathAnswer = "templates/mailNewAnswer.html";
    @Autowired
    private JavaMailSender mailSender;
    private String indirizzo = link + "/home/user/changePass?token=";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String loadTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
    private String loadTemplateAnswer() throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePathAnswer);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public void sendTempPasswordEmail(String user, String tempPassword, String resetToken) throws MessagingException, IOException {
        String template = loadTemplate();
        String message = "La tua password temporanea è: " + tempPassword;
        String link = indirizzo + resetToken;

        String htmlContent = template
                .replace("{%MESSAGGIO%}", message)
                .replace("{%LINK%}", link);

        sendHtmlEmail(user, "La tua password temporanea", htmlContent);
    }

    public void sendPasswordResetEmail(String user, String resetToken) throws MessagingException, IOException {
        String template = loadTemplate();
        String link = indirizzo + resetToken;
        String message = "Per cambiare la tua password clicca il tasto sottostante o vai a questo link:" + link;


        String htmlContent = template
                .replace("{%MESSAGGIO%}", message)
                .replace("{%LINK%}", link);

        sendHtmlEmail(user, "Cambio password", htmlContent);
    }
    public void sendAnswerEmail(String user, long feedbackId) throws MessagingException, IOException {

        String template = loadTemplateAnswer();
        String link ="http://localhost:4200/home/feedback;feedbackId="+ feedbackId;
        String message = "Hai ricevuto una risposta! Vedila con il tasto sottostante o dal link:" + link;


        String htmlContent = template
                .replace("{%MESSAGGIO%}", message)
                .replace("{%LINK%}", link);

        sendHtmlEmail(user, "Hai ricevuto una risposta dal nostro team", htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }


}
