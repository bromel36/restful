package vn.bromel.jobhunter.domain.response;

import lombok.*;
import vn.bromel.jobhunter.domain.Skill;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailResponseDTO {

    private String mailTo;
    private String subject;
    private String receiverName;
    private List<MailJob> jobs;
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MailJob{
        private String name;
        private String companyName;
        private Double salary;
        private List<String> skills;
    }
}
