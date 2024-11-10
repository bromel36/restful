package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.JobResponseDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillService skillService;
    public JobService(JobRepository jobRepository , SkillService skillService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
    }

    public JobResponseDTO handleCreateJob(Job job) {

        if(job.getSkills()!= null){
            List<Long> ids = job.getSkills().stream().map(it->it.getId()).collect(Collectors.toList());

            List<Skill> skills = skillService.handleFindSkills(ids);

            job.setSkills(skills);
        }

        jobRepository.save(job);

        return convertToJobResponseDTO(job, false);
    }

    public JobResponseDTO convertToJobResponseDTO(Job job, boolean isUpdate) {
        List<String> skillsName = job.getSkills().stream().map(it->it.getName()).collect(Collectors.toList());

        return JobResponseDTO.builder()
                .id(job.getId())
                .name(job.getName())
                .level(job.getLevel())
                .active(job.getActive())
                .description(job.getDescription())
                .endDate(job.getEndDate())
                .location(job.getLocation())
                .quantity(job.getQuantity())
                .salary(job.getSalary())
                .createdBy(isUpdate ? null : job.getCreatedBy())
                .startDate(job.getStartDate())
                .createdAt(isUpdate ? null :job.getCreatedAt())
                .updatedAt(!isUpdate ? null :job.getUpdatedAt())
                .updatedBy(!isUpdate ? null : job.getUpdatedBy())
                .skills(skillsName)
                .build();
    }

    public JobResponseDTO handleUpdateJob(Job job) {
        Job currentJob = this.jobRepository.findById(job.getId())
                .orElseThrow(() -> new IdInvalidException("Job not found"));

        currentJob.setDescription(job.getDescription());
        currentJob.setEndDate(job.getEndDate());
        currentJob.setLocation(job.getLocation());
        currentJob.setQuantity(job.getQuantity());
        currentJob.setSalary(job.getSalary());
        currentJob.setActive(job.getActive());
        currentJob.setStartDate(job.getStartDate());
        currentJob.setName(job.getName());
        currentJob.setLevel(job.getLevel());

        List<Long> ids = job.getSkills().stream().map(it->it.getId()).collect(Collectors.toList());

        List<Skill> skills = skillService.handleFindSkills(ids);

        currentJob.setSkills(skills);

        jobRepository.save(currentJob);

        return convertToJobResponseDTO(currentJob, true);
    }

    public Job handleFetchJobById(Long id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(
                        () -> new IdInvalidException("Job not found")
                );
        return job;
    }

    public PaginationResponseDTO handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(spec, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(jobPage.getTotalPages());
        meta.setTotal(jobPage.getTotalElements());
        meta.setTotalOfCurrentPage(jobPage.getNumberOfElements());

        List<Job> result = jobPage.getContent();

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }

    public void handleDeleteJob(Long id) {
        if(this.jobRepository.existsById(id)) {
            this.jobRepository.deleteById(id);
            return;
        }
        throw new IdInvalidException("Job not found");

    }
}
