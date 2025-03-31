package vn.bromel.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.bromel.jobhunter.domain.Subscriber;
import vn.bromel.jobhunter.service.SubscriberService;


@RestController
@RequestMapping("/api/v1/")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(final SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }
    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@RequestBody Subscriber subscriber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleCreateSubscriber(subscriber));
    }

    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) {
        return ResponseEntity.ok(this.subscriberService.handleUpdateSubscriber(subscriber));
    }
}
