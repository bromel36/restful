package vn.bromel.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.bromel.jobhunter.service.UserService;

@RestController
public class HelloController {

    private final UserService userService;
    public HelloController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String getHelloWorld(){

        return "Hello world";
    } 
}
