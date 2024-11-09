package vn.hoidanit.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;


@Builder
@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    private int age;

    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    private String address;

    private CompanyResponse company;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant updatedAt;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyResponse{
        private Long id;
        private String name;
    }

}
