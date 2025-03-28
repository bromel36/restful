package vn.bromel.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.bromel.jobhunter.domain.Skill;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor {
    boolean existsSkillByName(String name);

    List<Skill> findByIdIn(List<Long> ids);
}
