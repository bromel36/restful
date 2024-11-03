package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String access_token;
    private UserLoginResponseDTO user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginResponseDTO {
        private Long id;
        private String name;
        private String email;
    }
}
