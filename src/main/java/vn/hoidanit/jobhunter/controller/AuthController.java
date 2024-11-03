package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginRequestDTO;
import vn.hoidanit.jobhunter.domain.dto.LoginResponseDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final UserRepository userRepository;


    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;


    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService, UserRepository userRepository) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @PostMapping("/login")
    @ApiMessage("success login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO){


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);



        LoginResponseDTO responseLoginDTO = new LoginResponseDTO();
        responseLoginDTO.setAccess_token(securityUtil.createAccessToken(authentication));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUserDB = userService.handleGetUserByUsername(loginDTO.getUsername());

        if(currentUserDB != null){
            LoginResponseDTO.UserLoginResponseDTO userLogin
                    = new LoginResponseDTO.UserLoginResponseDTO(
                            currentUserDB.getId(),
                            currentUserDB.getName(),
                            currentUserDB.getEmail()
            );
            responseLoginDTO.setUser(userLogin);
        }

        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(),responseLoginDTO);

        updateUserRefreshToken(currentUserDB, refreshToken);

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .maxAge(refreshTokenExpiration)
                .secure(true)
                .path("/")

                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseLoginDTO);
    }

    public void updateUserRefreshToken(User user, String token){
        user.setRefreshToken(token);
        userRepository.save(user);
    }
}
