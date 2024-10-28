package vn.hoidanit.jobhunter.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Base;

import java.time.Instant;

public class AuditEntityListener {

    @PrePersist
    @PreUpdate
    public void prePersist(Base entity) {
        entity.setCreatedBy("bromel");
        entity.setCreatedAt(Instant.now());
    }
}
