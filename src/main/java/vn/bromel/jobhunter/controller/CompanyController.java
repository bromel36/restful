package vn.bromel.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.bromel.jobhunter.domain.Company;
import vn.bromel.jobhunter.domain.request.CompanyRequestDTO;
import vn.bromel.jobhunter.domain.response.PaginationResponseDTO;
import vn.bromel.jobhunter.service.CompanyService;
import vn.bromel.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }


    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(companyService.handleFetchCompanyById(id));
    }
    @GetMapping("/companies")
    public ResponseEntity<PaginationResponseDTO> getAll(
            @Filter Specification<Company> spec,
            Pageable pageable

    ){
        PaginationResponseDTO paginationResponseDTO = this.companyService.handleGetAllCompanies(spec, pageable);

        return ResponseEntity.ok(paginationResponseDTO);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> insertCompany(@Valid @RequestBody CompanyRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleInsertOrUpdateCompany(dto));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyRequestDTO dto) {
        return ResponseEntity.ok(companyService.handleInsertOrUpdateCompany(dto));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
