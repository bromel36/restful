package vn.bromel.jobhunter.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.bromel.jobhunter.service.UserService;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;
    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.bromel.jobhunter.domain.User myUser = this.userService.handleGetUserByUsername(username);
        if(myUser == null){
            throw new UsernameNotFoundException("username/password incorrect");
        }

        return new User(
                myUser.getEmail(),
                myUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
