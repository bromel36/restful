package vn.bromel.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.bromel.jobhunter.domain.Role;

@Getter
@Setter
public class LoginResponseDTO {

    @JsonProperty("access_token")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;
    private UserLoginResponseDTO user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginResponseDTO {
        private Long id;
        private String name;
        private String email;
        private Role role;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken {
        private Long id;
        private String name;
        private String email;
    }
}
