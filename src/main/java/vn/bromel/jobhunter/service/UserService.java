package vn.bromel.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.bromel.jobhunter.domain.Company;
import vn.bromel.jobhunter.domain.Role;
import vn.bromel.jobhunter.domain.User;
import vn.bromel.jobhunter.domain.response.UserResponseDTO;
import vn.bromel.jobhunter.domain.response.PaginationResponseDTO;
import vn.bromel.jobhunter.repository.UserRepository;
import vn.bromel.jobhunter.util.error.EmailExistException;
import vn.bromel.jobhunter.util.error.IdInvalidException;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;


    public UserService(UserRepository userRepository, CompanyService companyService,
                       PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public UserResponseDTO handleUserCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailExistException("Email " + user.getEmail() + " is exist, please try difference email address");
        }

//        Company company = checkExistCompany(user);
//        user.setCompany(company);
//
//        Role role = checkExistRole(user);
//        user.setRole(role);

        userRepository.save(user);

        return convertToUserResponseDTO(user,"create");
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

        Role role = checkExistRole(userRequest);
        user.setRole(role);

        this.userRepository.save(user);

        return convertToUserResponseDTO(user,"update");
    }

    public void handleUserDelete(Long id) {
        if (userRepository.existsById(id)){
            this.userRepository.deleteById(id);
            return;
        }
        throw new IdInvalidException("Id with id= " + id + " is not exist");
    }

    public UserResponseDTO handleFetchUserById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User with id= " + id+ " does not exists "));
        Company company = user.getCompany();
        return convertToUserResponseDTO(user, "fetch");
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

        List<UserResponseDTO> result = users.getContent()
                .stream()
                .map(it-> convertToUserResponseDTO(it, "fetch"))
                .toList();

        paginationResponseDTO.setResult(result);

        return paginationResponseDTO;
    }



    public User handleGetUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }


    public UserResponseDTO convertToUserResponseDTO(User user, String action){
        Instant createdAt = action.equals("update") ? null : user.getCreatedAt();
        Instant updatedAt = action.equals("create") ? null : user.getUpdatedAt();

        UserResponseDTO.RoleResponse role = null;
        if(user.getRole()!= null){
            role = new UserResponseDTO.RoleResponse(user.getRole().getId(), user.getRole().getName());
        }

        UserResponseDTO.CompanyResponse company = user.getCompany()!= null ? new UserResponseDTO.CompanyResponse(user.getCompany().getId(),user.getCompany().getName()) : null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .updatedAt(updatedAt)
                .createdAt(createdAt)
                .company(company)
                .role(role)
                .build() ;
    }

    public User getUserByRefreshTokenAndEmail(String token, String email){
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public void updateUserRefreshToken(User user, String token){
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    public Company checkExistCompany(User user){
        if(user.getCompany()== null ){
            return null;
        }
        else if(user.getCompany().getId() == null){
            return null;
        }

        Company company = companyService.handleFetchCompanyById(user.getCompany().getId());
        return company;
    }

    public Role checkExistRole(User user){
        if(user.getRole()== null ){
            return null;
        }
        else if(user.getRole().getId() == null){
            return null;
        }

        Role role = roleService.handleFetchRoleById(user.getRole().getId());
        return role;
    }

    public boolean isExistUser(Long id){
        return this.userRepository.existsById(id);
    }
}
