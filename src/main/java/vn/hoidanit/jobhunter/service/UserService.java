package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.UserResponseDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.EmailExistException;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserResponseDTO handleUserCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailExistException("Email " + user.getEmail() + " is exist, please try difference email address");
        }

        userRepository.save(user);

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .build() ;
    }

    public void handleUserDelete(Long id) {
        if (userRepository.existsById(id)){
            this.userRepository.deleteById(id);
            return;
        }
        throw new IdInvalidException("Id with id= " + id + " is not exist");
    }

    public UserResponseDTO handleGetUser(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User with id= " + id+ " does not exists "));
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .updatedAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .build() ;
    }

    public PaginationResponseDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {

        Page<User> users = userRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();

        meta.setPageSize(pageable.getPageSize());
        meta.setPage(pageable.getPageNumber() + 1);

        meta.setTotal(users.getTotalElements());
        meta.setPages(users.getTotalPages());
        meta.setTotalOfCurrentPage(users.getNumberOfElements());

        paginationResponseDTO.setMeta(meta);
        paginationResponseDTO.setResult(convertToUserResponseDTO(users.getContent()));

        return paginationResponseDTO;
    }

    public UserResponseDTO handleUpdateUser(User userRequest){
        User user = this.userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new IdInvalidException("User with id= " + userRequest.getId()+ " does not exists "));
        user.setName(userRequest.getName());
        user.setAddress(userRequest.getAddress());
        user.setAge(userRequest.getAge());
        user.setGender(userRequest.getGender());

        this.userRepository.save(user);
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .updatedAt(user.getUpdatedAt())
                .build() ;
    }

    public User handleGetUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public List<UserResponseDTO> convertToUserResponseDTO(List<User> users){
        List<UserResponseDTO> result = users.stream().map(it -> UserResponseDTO.builder()
                .id(it.getId())
                .name(it.getName())
                .email(it.getEmail())
                .address(it.getAddress())
                .age(it.getAge())
                .gender(it.getGender())
                .updatedAt(it.getUpdatedAt())
                .createdAt(it.getCreatedAt())
                .build()).collect(Collectors.toList());
        return result;
    }

}
