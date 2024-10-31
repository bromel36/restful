package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginationResponseDTO;
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

    public PaginationResponseDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {

        Page<User> users = userRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();

        meta.setPageSize(pageable.getPageSize());
        meta.setPage(pageable.getPageNumber());

        meta.setTotal(users.getTotalElements());
        meta.setPages(users.getTotalPages());
        meta.setTotalOfCurrentPage(users.getNumberOfElements());

        paginationResponseDTO.setMeta(meta);
        paginationResponseDTO.setResult(users.getContent());

        return paginationResponseDTO;
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
