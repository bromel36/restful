package vn.hoidanit.jobhunter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.UserNoLongerException;

import java.util.List;


public class PermissionInterceptor implements HandlerInterceptor {


    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

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
