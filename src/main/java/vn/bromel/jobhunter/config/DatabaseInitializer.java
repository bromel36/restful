package vn.bromel.jobhunter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.bromel.jobhunter.domain.Permission;
import vn.bromel.jobhunter.domain.Role;
import vn.bromel.jobhunter.domain.User;
import vn.bromel.jobhunter.repository.PermissionRepository;
import vn.bromel.jobhunter.repository.RoleRepository;
import vn.bromel.jobhunter.repository.UserRepository;
import vn.bromel.jobhunter.util.constant.GenderEnum;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j(topic = "INITIAL-SERVICRE")
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DatabaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {
        log.info("Initializing Database...");

        Long roleSize = roleRepository.count();
        Long permissionSize = permissionRepository.count();
        Long userSize = userRepository.count();


        if(permissionSize == 0){

            this.permissionRepository.saveAll(createAllPermission());

            log.info(">>>>>> Successful initiate permission");
        }
        if (roleSize == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();

            adminRole.setName("SUPER_ADMIN");

            adminRole.setDescription("Admin role has full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);

            log.info(">>>>>> Successful initiate role");

        }
        if(userSize == 0){
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("sample address");
            adminUser.setAge(100);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);

        }

        if (permissionSize > 0 && roleSize > 0 && userSize > 0) {
            log.info(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            log.info(">>> END INIT DATABASE");

    }

    public List<Permission> createAllPermission(){
        ArrayList<Permission> arr = new ArrayList<>();
//        arr.add(new Permission("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
//        arr.add(new Permission("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
//        arr.add(new Permission("Delete a company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
//        arr.add(new Permission("Get a company by id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
//        arr.add(new Permission("Get companies with pagination", "/api/v1/companies", "GET", "COMPANIES"));
//
//        arr.add(new Permission("Create a job", "/api/v1/jobs", "POST", "JOBS"));
//        arr.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
//        arr.add(new Permission("Delete a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
//        arr.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
//        arr.add(new Permission("Get jobs with pagination", "/api/v1/jobs", "GET", "JOBS"));
//
//        arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
//        arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
//        arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
//        arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
//        arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));
//
//        arr.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
//        arr.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
//        arr.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
//        arr.add(new Permission("Get a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
//        arr.add(new Permission("Get resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));
//
//        arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
//        arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
//        arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
//        arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
//        arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));
//
//        arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
//        arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
//        arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
//        arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
//        arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));
//
//        arr.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
//        arr.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
//        arr.add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
//        arr.add(new Permission("Get a subscriber by id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
//        arr.add(new Permission("Get subscribers with pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));
//
//        arr.add(new Permission("Download a file", "/api/v1/files", "POST", "FILES"));
//        arr.add(new Permission("Upload a file", "/api/v1/files", "GET", "FILES"));

        return arr;
    }
}
