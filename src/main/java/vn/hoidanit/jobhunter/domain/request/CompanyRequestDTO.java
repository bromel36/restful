package vn.hoidanit.jobhunter.domain.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDTO {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String name;

    private String description;

    private String address;

    private String logo;
}
