package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.CompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginationResponseDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.net.URI;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleInsertOrUpdateCompany(CompanyDTO dto) {
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

    public PaginationResponseDTO handleGetAllCompanies(Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        Meta meta = new Meta();

        meta.setPage(companyPage.getNumber());
        meta.setPages(companyPage.getTotalPages());
        meta.setTotal(companyPage.getTotalElements());
        meta.setPageSize(companyPage.getSize());

        paginationResponseDTO.setResult(companyPage.getContent());
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }

    public void handleDeleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
