package vn.hoidanit.jobhunter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String email;

    private String password;
    private int age;

    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    private String address;

    private String refreshToken;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Resume> resumes ;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @PrePersist
    public void beforePersist() {
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get()
                : "";
        createdAt = Instant.now();
    }

    @PreUpdate
    public void beforeUpdate(){
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get()
                : "";
        updatedAt = Instant.now();
    }
}
