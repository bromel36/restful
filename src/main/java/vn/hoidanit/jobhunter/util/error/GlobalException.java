package vn.hoidanit.jobhunter.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.hoidanit.jobhunter.domain.RestResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException{
    /*
    * in function loadUserByUsername() which we override in UserDetailsCustom.java, when can't find user by username, we throw
    * UsernameNotFoundException, then, spring security will catch and finally, it throws BadCredentialsException
    * so in this function, it always catches BadCredentialsException
    * but in password is incorrect, spring security will finally throw BadCredentialsException, and we
    * not be done anything
    *
    * the purpose of function is show error when login failed
    * */
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleUsernameNotFoundException(BadCredentialsException ex){
        RestResponse<Object> res = new RestResponse<>();
        res.setMessage("Exception occur ....");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            IdInvalidException.class,
            EmailExistException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex){
        RestResponse<Object> res = new RestResponse<>();

        res.setMessage("Exception occur ....");
        res.setError(ex.getLocalizedMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        RestResponse<Object> res = new RestResponse<>();
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(it -> it.getDefaultMessage())
                .collect(Collectors.toList());

        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
//        res.setError(HttpStatus.BAD_REQUEST.name());
        res.setError(ex.getBody().getDetail());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleResourceNotFoundException(NoResourceFoundException ex){
        RestResponse<Object> res = new RestResponse<>();

        res.setMessage("Exception occur ....");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RestResponse<Object>> handleNullPointerException(NullPointerException ex){
        RestResponse<Object> res = new RestResponse<>();

        res.setMessage("Exception occur ....");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
