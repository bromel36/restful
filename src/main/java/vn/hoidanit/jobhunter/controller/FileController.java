package vn.hoidanit.jobhunter.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.constant.FileConstant;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class FileController {


    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public String upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        String fullDirectoryPath = FileConstant.getBaseURI() + folder;

        this.fileService.createUpLoadFolder(fullDirectoryPath);

        return this.fileService.storeFile(file,fullDirectoryPath);
    }
}
