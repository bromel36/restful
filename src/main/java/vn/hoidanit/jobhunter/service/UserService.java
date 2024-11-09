package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.UserResponseDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.EmailExistException;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    public UserService(UserRepository userRepository, CompanyService companyService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
    }

    public UserResponseDTO handleUserCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailExistException("Email " + user.getEmail() + " is exist, please try difference email address");
        }

        Company company = checkExistCompany(user);

        userRepository.save(user);
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .company(new UserResponseDTO.CompanyResponse(company.getId(),company.getName()))
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
        Company company = user.getCompany();
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .updatedAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .company(new UserResponseDTO.CompanyResponse(company.getId(),company.getName()))
                .build() ;
    }

    public PaginationResponseDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {

        Page<User> users = userRepository.findAll(spec, pageable);

        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();
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

        Company company = checkExistCompany(userRequest);

        user.setCompany(company);

        this.userRepository.save(user);
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .updatedAt(user.getUpdatedAt())
                .company(new UserResponseDTO.CompanyResponse(company.getId(),company.getName()))
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
                .company(it.getCompany()== null ? null : new UserResponseDTO.CompanyResponse(it.getCompany().getId(),it.getCompany().getName()))
                .build()).collect(Collectors.toList());
        return result;
    }

    public User getUserByRefreshTokenAndEmail(String token, String email){
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public void updateUserRefreshToken(User user, String token){
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    public Company checkExistCompany(User user){
        if(user.getCompany()== null || user.getCompany().getId() == null){
            return null;
        }

        Company company = companyService.handleFetchCompanyById(user.getCompany().getId());
        return company;
    }
}
