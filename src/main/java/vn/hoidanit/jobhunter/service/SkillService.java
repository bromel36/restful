package vn.hoidanit.jobhunter.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@Service
public class SkillService {
    private SkillRepository skillRepository;
    private JobRepository jobRepository;

    public SkillService(SkillRepository skillRepository , JobRepository jobRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    public Skill handleCreateSkill(Skill skill) {

        checkNameExists(skill.getName());

        return skillRepository.save(skill);
    }


    public Skill handleUpdateSkill(Skill skill) {
        Skill currentSkill = handleFetchSkillById(skill.getId());

        checkNameExists(skill.getName());

        skill.setCreatedAt(currentSkill.getCreatedAt());
        skill.setCreatedBy(currentSkill.getCreatedBy());

        return skillRepository.save(skill);
    }

    public Skill handleFetchSkillById(Long id) {
        Skill currentSkill = this.skillRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Skill not found"));

        return currentSkill;
    }


    public PaginationResponseDTO handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(spec, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());
        meta.setTotalOfCurrentPage(skillPage.getNumberOfElements());

        List<Skill> result = skillPage.getContent();

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }


    public void handldeDeleteSkill(Long id){
        Skill skill = handleFetchSkillById(id);

        List<Job> jobs = skill.getJobs();

//        jobs.forEach(j->{
//            j.getSkills().removeIf(s -> s.getId().equals(id));
//        });
//
//        this.jobRepository.saveAll(jobs);

        jobs.forEach(j->{
            j.getSkills().remove(skill);
        });

        this.skillRepository.delete(skill);

    }

    public List<Skill> handleFindSkills(List<Long> ids){
        return this.skillRepository.findByIdIn(ids);
    }

    public void checkNameExists(String name){
        if(skillRepository.existsSkillByName(name)){
            throw new IdInvalidException("Skill already exists");
        }
    }

}
