package vn.bromel.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.bromel.jobhunter.domain.Job;
import vn.bromel.jobhunter.domain.Skill;

import java.util.List;

public interface  JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor {
    List<Job> findAllBySkillsIn(List<Skill> skills);
}
