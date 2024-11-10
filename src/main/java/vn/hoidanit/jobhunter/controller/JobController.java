package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.JobResponseDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(final JobService jobService) {
        this.jobService = jobService;
    }
    @ApiMessage("create a job")
    @PostMapping("/jobs")
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody Job job){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(job));
    }

    @ApiMessage("update a job")
    @PutMapping("/jobs")
    public ResponseEntity<JobResponseDTO> updateJob(@RequestBody Job job){
        return ResponseEntity.ok().body(this.jobService.handleUpdateJob(job));
    }

    @ApiMessage("fetch a job")
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(this.jobService.handleFetchJobById(id));
    }

    @ApiMessage("fetch all jobs")
    @GetMapping("/jobs")
    public ResponseEntity<PaginationResponseDTO> getAllJobs(
            @Filter Specification<Job> spec,
            Pageable pageable
    ){
        PaginationResponseDTO paginationResponseDTO = this.jobService.handleGetAllJobs(spec, pageable);

        return ResponseEntity.ok(paginationResponseDTO);
    }

    @ApiMessage("delete a job")
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id){
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok(null);
    }
}
