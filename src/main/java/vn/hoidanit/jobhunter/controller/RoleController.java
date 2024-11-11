package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiMessage("create a role")
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleCreateRole(role));
    }


    @ApiMessage("update a role")
    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok().body(this.roleService.handleUpdateRole(role));
    }

    @ApiMessage("delete a role")
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.ok().body(null);
    }


    @ApiMessage("fetch all roles")
    @GetMapping("/roles")
    public ResponseEntity<PaginationResponseDTO> getAllResume(
            @Filter Specification<Role> spec,
            Pageable pageable
    ){
        PaginationResponseDTO paginationResponseDTO = this.roleService.handleGetAllPermission(spec, pageable);

        return ResponseEntity.ok(paginationResponseDTO);
    }



}
