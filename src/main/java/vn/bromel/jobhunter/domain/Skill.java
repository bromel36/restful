package vn.bromel.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.bromel.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skill name is required")
    private String name;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;


    @ManyToMany(mappedBy = "skills",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(mappedBy = "skills",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subscriber> subscribers;

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
