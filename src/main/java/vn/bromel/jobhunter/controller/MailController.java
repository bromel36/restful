package vn.bromel.jobhunter.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.bromel.jobhunter.domain.Job;
import vn.bromel.jobhunter.domain.response.MailResponseDTO;
import vn.bromel.jobhunter.service.JobService;
import vn.bromel.jobhunter.service.MailService;
import vn.bromel.jobhunter.service.SubscriberService;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MailController {

    SubscriberService subscriberService;

    @GetMapping("/mails")
    public String sendSimpleMail() {
        this.subscriberService.sendMailToSubscriber();
        return "OK";
    }


}
