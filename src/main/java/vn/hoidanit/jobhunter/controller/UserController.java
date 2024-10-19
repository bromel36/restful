package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id){
        return this.userService.handleGetUser(id);
    }
    @PostMapping("/user")
    public User createUser(@RequestBody User userRequest) {

        return userService.handleUserCreate(userRequest);
    }

    @GetMapping("/user")
    public List<User> getAllUsers(){
        return this.userService.handleGetAllUsers();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User userRequest){
        return this.userService.handleUpdateUser(userRequest);
    }



    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        return this.userService.handleUserDelete(id);
    }
}
