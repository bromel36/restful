package vn.bromel.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.bromel.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class JobResponseDTO {
    private Long id;
    private String name;
    private String location;
    private Double salary;
    private Integer quantity;
    private LevelEnum level;

    private String description;

    private Instant startDate;
    private Instant endDate;
    private Boolean active;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedBy;

    private List<String> skills;



}
