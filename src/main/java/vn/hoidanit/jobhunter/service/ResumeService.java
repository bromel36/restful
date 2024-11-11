package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeResponseDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;


    public ResumeService(ResumeRepository resumeRepository, UserService userService, JobService jobService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    public ResumeResponseDTO handleCreateResume(Resume resume) {
        if (!this.userService.isExistUser(resume.getUser().getId())
                || !this.jobService.isExistJob(resume.getJob().getId())) {
            throw new IdInvalidException("Job/User does not exist");
        }

        this.resumeRepository.save(resume);


        return ResumeResponseDTO.builder()
                .id(resume.getId())
                .createdBy(resume.getCreatedBy())
                .createdAt(resume.getCreatedAt())
                .build();
    }

    public ResumeResponseDTO handleUpdateResume(Resume resume) {
        Resume currentResume = this.resumeRepository.findById(resume.getId())
                .orElseThrow(() -> new IdInvalidException("Resume does not found"));

        currentResume.setStatus(resume.getStatus());
        this.resumeRepository.save(currentResume);

        return ResumeResponseDTO.builder()
                .updatedAt(currentResume.getUpdatedAt())
                .updatedBy(currentResume.getUpdatedBy())
                .build();
    }

    public void handleDeleteResume(Long id) {
        if (this.resumeRepository.existsById(id)) {
            this.resumeRepository.deleteById(id);
            return;
        }
        throw new IdInvalidException("Resume does not found");
    }


    public ResumeResponseDTO handleFetchResumeById(Long id) {
        Resume currentResume = this.resumeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Resume does not found"));

        return convertToResumeDTO(currentResume);
    }


    public PaginationResponseDTO handleGetAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = resumeRepository.findAll(spec, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());
        meta.setTotalOfCurrentPage(resumePage.getNumberOfElements());

        List<Resume> resumes = resumePage.getContent();

        List<ResumeResponseDTO> result = resumes.stream().map(this::convertToResumeDTO).collect(Collectors.toList());

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }

    public ResumeResponseDTO convertToResumeDTO(Resume currentResume) {
        return ResumeResponseDTO.builder()
                .id(currentResume.getId())
                .status(currentResume.getStatus())
                .url(currentResume.getUrl())
                .email(currentResume.getEmail())
                .createdBy(currentResume.getCreatedBy())
                .createdAt(currentResume.getCreatedAt())
                .updatedBy(currentResume.getUpdatedBy())
                .updatedAt(currentResume.getUpdatedAt())
                .companyName(currentResume.getJob()!= null ? currentResume.getJob().getCompany().getName():null)
                .user(new ResumeResponseDTO.UserResumeResponse(currentResume.getUser().getId(), currentResume.getUser().getName()))
                .job(new ResumeResponseDTO.JobResumeResponse(currentResume.getJob().getId(), currentResume.getJob().getName()))
                .build();
    }
}
