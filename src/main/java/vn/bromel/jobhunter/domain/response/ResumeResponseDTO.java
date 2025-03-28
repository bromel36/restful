package vn.bromel.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import vn.bromel.jobhunter.util.constant.ResumeStateEnum;

import java.time.Instant;


@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeResponseDTO {
    private Long id;

    private String email;

    private String url;

    private ResumeStateEnum status;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    private UserResumeResponse user;

    private JobResumeResponse job;

    private String companyName;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResumeResponse {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResumeResponse {
        private long id;
        private String name;
    }
}
