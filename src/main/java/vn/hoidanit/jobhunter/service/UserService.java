package vn.hoidanit.jobhunter.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User handleUserCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String handleUserDelete(Long id) {
        this.userRepository.deleteById(id);
        return "deleted ";
    }

    public User handleGetUser(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User userRequest) {
        if(this.userRepository.existsById(userRequest.getId())) {
            return this.userRepository.save(userRequest);
        }
        return null;
    }

    public User handleGetUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }
}
