package vn.bromel.jobhunter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.bromel.jobhunter.util.constant.FileConstant;


@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(FileConstant.getBaseURI());
//        config basePath + ten file de doc
    }
}
