package vn.bromel.jobhunter.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.bromel.jobhunter.service.MailService;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/mails")
    public String sendSimpleMail() {

//        this.mailService.handleSendSimpleMail();
//        this.mailService.sendEmailSync("nguyentien7a8@gmail.com","Email for learning test", "<b> Hello </b>",false, true);
        this.mailService.sendEmailFromTemplateSync("minhtien2003.mtmt@gmail.com", "TEST with template", "job");
        return "OK";
    }

}
