package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.request.CompanyRequestDTO;
import vn.hoidanit.jobhunter.domain.response.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleInsertOrUpdateCompany(CompanyRequestDTO dto) {
        Company company = new Company();
        if(dto.getId()!= null){
            company = companyRepository.findById(dto.getId()).orElse(null);
        }
        if(company == null){
            return null;
        }
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setLogo(dto.getLogo());
        company.setDescription(dto.getDescription());
        return companyRepository.save(company) ;
    }

    public PaginationResponseDTO handleGetAllCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(specification, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());
        meta.setTotalOfCurrentPage(companyPage.getNumberOfElements());

        List<Company> result = companyPage.getContent();

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }

    public void handleDeleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public Company handleFetchCompanyById(Long id) {
        Company company = this.companyRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Id not found")
                );
        return company;
    }
}
