package vn.bromel.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.bromel.jobhunter.util.SecurityUtil;
import vn.bromel.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Double salary;
    private Integer quantity;

    @Enumerated(value = EnumType.STRING)
    private LevelEnum level;

    @Column(columnDefinition = "longText")
    private String description;

    private Instant startDate;
    private Instant endDate;
    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "jobs")
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @JsonIgnore
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<Resume> resumes ;

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
