package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.RestResponse;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class HelloController {

    private final UserService userService;
    public HelloController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String getAllUsers(){

        return "Toi la bromel";
    } 
}
