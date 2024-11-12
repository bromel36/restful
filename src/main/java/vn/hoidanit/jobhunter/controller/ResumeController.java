package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeResponseDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {


    private final ResumeService resumeService;

    public ResumeController(final ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @ApiMessage("create a resume")
    @PostMapping("/resumes")
    public ResponseEntity<ResumeResponseDTO> createResume(@Valid @RequestBody Resume resume){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }

    @ApiMessage("update a resume")
    @PutMapping("/resumes")
    public ResponseEntity<ResumeResponseDTO> updateResume(@RequestBody Resume resume){
        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(resume));
    }

    @ApiMessage("delete a resume")
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id){
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @ApiMessage("fetch a resume")
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResumeResponseDTO> getResume(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(this.resumeService.handleFetchResumeById(id));
    }

    @ApiMessage("fetch all resumes")
    @GetMapping("/resumes")
    public ResponseEntity<PaginationResponseDTO> getAllResume(
            @Filter Specification<Resume> spec,
            Pageable pageable
    ){
        PaginationResponseDTO paginationResponseDTO = this.resumeService.handleGetAllResumes(spec, pageable);

        return ResponseEntity.ok(paginationResponseDTO);
    }

    @ApiMessage("fetch resume by authentication")
    @PostMapping("/resumes/by-user")
    public ResponseEntity<PaginationResponseDTO> getResumeByUser(
            Pageable pageable
    ){
        String username = SecurityUtil.getCurrentUserLogin().orElse("");

        return ResponseEntity.ok(this.resumeService.handleFetchResumeByUsername(username, pageable));
    }
}
