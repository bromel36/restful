package vn.bromel.jobhunter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.bromel.jobhunter.domain.Permission;
import vn.bromel.jobhunter.domain.Role;
import vn.bromel.jobhunter.domain.User;
import vn.bromel.jobhunter.service.PermissionService;
import vn.bromel.jobhunter.service.UserService;
import vn.bromel.jobhunter.util.SecurityUtil;

import java.util.List;


@Component
public class PermissionInterceptor implements HandlerInterceptor {



    private final UserService userService;

    private final PermissionService permissionService;
    private final AntPathMatcher pathMatcher;

    public PermissionInterceptor(UserService userService, PermissionService permissionService, AntPathMatcher matcher) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.pathMatcher = matcher;
    }

    List<String> allowedGetEndpoints = List.of(
            "/api/v1/companies/**",
            "/api/v1/jobs/**",
            "/api/v1/skills/**"
    );
    List<String> allowedPostEndpoints = List.of(
            "/api/v1/resumes/**"
    );

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {


        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);


        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        User user = userService.handleGetUserByUsername(email);

        if (user != null) {
            if (httpMethod.equalsIgnoreCase("GET")) {
                for (String pattern : allowedGetEndpoints) {
                    if (pathMatcher.match(pattern, path)) {
                        return true;
                    }
                }
            }
            if (httpMethod.equalsIgnoreCase("POST")) {
                for (String pattern : allowedPostEndpoints) {
                    if (pathMatcher.match(pattern, path)) {
                        return true;
                    }
                }
            }
            Role role = user.getRole();
            if (role != null) {
                List<Permission> permissions = role.getPermissions();

                boolean isAllow = permissions.stream().anyMatch(
                        it -> it.getApiPath().equals(path) && it.getMethod().equals(httpMethod)
                );

                if (!isAllow) {
                    if (permissionService.isExistByApiPathAndMethod(path, httpMethod)) {
                        // xu ly truong hop goi sai api -> 404
                            throw new AccessDeniedException("Access denied");
                    }
                }
            } else {
                throw new AccessDeniedException("Access denied");
            }
        }
//        else {
//            //truong hop hi huu, co token nhung nguoi dung trong db thi khong con
//            throw new UserNoLongerException("Unauthenticated!!!!");
//        } sai roi nhung ma de lai cho biet co truong hop la co token nhung nguoi dung trong db thi khong co

        return true;
    }

}
