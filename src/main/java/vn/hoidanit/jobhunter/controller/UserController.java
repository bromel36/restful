package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.exception.IdInvalidException;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        User user = this.userService.handleGetUser(id);
        if(user!=null){
         return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }



    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        User user = userService.handleUserCreate(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = this.userService.handleGetAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User userRequest){
        return this.userService.handleUpdateUser(userRequest);
    }




    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if(id >10){
//            System.out.println("ok");
            throw new IdInvalidException("Id khong the lon hon 10");
        }

        String result = this.userService.handleUserDelete(id);
        return ResponseEntity.ok(result);
    }
}
