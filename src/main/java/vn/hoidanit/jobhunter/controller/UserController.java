package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.UserResponseDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.userService.handleFetchUserById(id));
    }



    @PostMapping("/users")
    @ApiMessage("create a new user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody User userRequest){
        UserResponseDTO userCreated = userService.handleUserCreate(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
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

    @PutMapping("/users")
    @ApiMessage("update user success")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody User userRequest){
        UserResponseDTO userResponseDTO = this.userService.handleUpdateUser(userRequest);

        return ResponseEntity.ok(userResponseDTO);
    }




    @DeleteMapping("/users/{id}")
    @ApiMessage("deleted a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {

        this.userService.handleUserDelete(id);
        return ResponseEntity.ok(null);
    }
}
