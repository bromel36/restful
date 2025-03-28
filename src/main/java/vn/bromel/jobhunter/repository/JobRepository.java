package vn.bromel.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.bromel.jobhunter.domain.Job;

public interface  JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor {
}
