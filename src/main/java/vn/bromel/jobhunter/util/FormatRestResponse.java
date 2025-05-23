package vn.bromel.jobhunter.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import vn.bromel.jobhunter.domain.response.RestResponse;
import vn.bromel.jobhunter.util.annotation.ApiMessage;

/*
*ben controller, khi tra ve thi phai tra ve mot object hoac la mot ResponseEntity gi do chu khong tra ve String hay Long ...
* sau khi controller tra response ve, no se chay qua day, tai day ta lay body va status de xu ly va
* gan vao trong thang RestFormat
* con neu xay ra Exception thi no nhay qua ben class co chua HandlerException truoc roi moi qua day*/
@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpResponse = ((ServletServerHttpResponse)response).getServletResponse();
        int status = httpResponse.getStatus();
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

//        handle case controller return a string
        if(body instanceof String || body instanceof Resource){
            return body;
        }
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        if(status < 400){
            //success case
            res.setData(body);

            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);

            res.setMessage(apiMessage!= null ? apiMessage.value() : "CALL API SUCCESS");

            return res;
        }
        else{
            // error case
            return body;
        }
    }
}
