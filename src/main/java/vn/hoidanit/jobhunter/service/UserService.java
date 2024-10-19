package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleUserCreate(User user) {

        return userRepository.save(user);
    }

    public String handleUserDelete(Long id) {
        this.userRepository.deleteById(id);
        return "created";
    }
}
