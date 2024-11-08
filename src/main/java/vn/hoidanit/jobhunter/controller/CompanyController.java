package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.request.CompanyRequestDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.service.CompanyService;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
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
