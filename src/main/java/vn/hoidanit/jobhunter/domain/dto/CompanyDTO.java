package vn.hoidanit.jobhunter.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {

    @NotBlank(message = "Company name is required")
    private String name;

    private String description;

    private String address;

    private String logo;
}
