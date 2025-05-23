package vn.bromel.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import vn.bromel.jobhunter.util.constant.GenderEnum;

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

    private GenderEnum gender;

    private String address;

    private CompanyResponse company;

    private RoleResponse role;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleResponse{
        private Long id;
        private String name;
    }

}
