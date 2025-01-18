package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(final SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @ApiMessage("update a skill")
    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.ok().body(this.skillService.handleUpdateSkill(skill));
    }

    @ApiMessage("fetch a skill")
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.skillService.handleFetchSkillById(id));
    }


    @ApiMessage("fetch all skills")
    @GetMapping("/skills")
    public ResponseEntity<PaginationResponseDTO> getAllSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable
    ){
        PaginationResponseDTO paginationResponseDTO = this.skillService.handleGetAllSkills(spec, pageable);

        return ResponseEntity.ok(paginationResponseDTO);
    }

    @ApiMessage("delete a skill")
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id){
        this.skillService.handldeDeleteSkill(id);
        return ResponseEntity.ok(null);
    }
}
