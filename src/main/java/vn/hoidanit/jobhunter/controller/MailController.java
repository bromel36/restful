package vn.hoidanit.jobhunter.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.service.MailService;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/mails")
    public String sendSimpleMail() {

        this.mailService.handleSendSimpleMail();
        return "OK";
    }
}
