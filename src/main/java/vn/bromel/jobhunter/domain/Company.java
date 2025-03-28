package vn.bromel.jobhunter.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.bromel.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    private Instant createdAt;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
    List<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
    List<Job> jobs;

    @PrePersist
    public void handleBeforeInsert(){
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                    SecurityUtil.getCurrentUserLogin().get()
                    : "";
        createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get()
                : "";
        updatedAt = Instant.now();
    }
}
