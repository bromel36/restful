package vn.bromel.jobhunter.controller;


import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.bromel.jobhunter.service.SubscriberService;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MailController {

    SubscriberService subscriberService;

    @GetMapping("/mails")
//    @Scheduled(cron = "0/30 * * * * ?")
//    @Transactional
    public String sendSimpleMail() {
        this.subscriberService.sendMailToSubscriber();
        return "OK";
    }
}
