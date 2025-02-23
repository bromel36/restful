package vn.hoidanit.jobhunter.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    public void handleSendSimpleMail() {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setTo("minhtien2003.mtmt@gmail.com");
        simpleMail.setSubject("Email for learning test");
        simpleMail.setText("Hello World");
        mailSender.send(simpleMail);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
                              boolean isHtml) {
// Prepare message using a Spring helper
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    public void sendEmailFromTemplateSync(String to, String subject, String
            templateName) {
        Context context = new Context();
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }
}
