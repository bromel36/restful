package vn.hoidanit.jobhunter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant updatedAt;
}
