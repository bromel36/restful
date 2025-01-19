package vn.hoidanit.jobhunter.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void handleSendSimpleMail() {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setTo("minhtien2003.mtmt@gmail.com");
        simpleMail.setSubject("Email for learning test");
        simpleMail.setText("Hello World");
        mailSender.send(simpleMail);
    }
}
