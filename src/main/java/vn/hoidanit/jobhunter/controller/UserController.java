package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
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

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<PaginationResponseDTO> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable
            ){
        PaginationResponseDTO paginationResponseDTO = userService.handleGetAllUsers(spec, pageable);
        return ResponseEntity.ok(paginationResponseDTO);
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
