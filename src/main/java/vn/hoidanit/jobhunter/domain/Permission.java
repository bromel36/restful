package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotBlank(message = "API path must be filled")
    private String apiPath;

    @NotBlank(message = "HTTP Method must be filled")
    private String method;

    @NotBlank(message = "Module must be filled")
    private String module;
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToMany(mappedBy = "permissions",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Role> roles;



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
