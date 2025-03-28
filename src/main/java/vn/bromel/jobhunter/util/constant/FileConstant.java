package vn.bromel.jobhunter.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConstant {

    private static FileConstant instance;

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;

    public FileConstant() {
        instance = this;
    }

    public static String getBaseURI() {
        return instance.baseURI;
    }
}
