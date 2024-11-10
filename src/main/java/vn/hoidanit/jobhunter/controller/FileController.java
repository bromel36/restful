package vn.hoidanit.jobhunter.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.jobhunter.domain.response.FileResponseDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.constant.FileConstant;
import vn.hoidanit.jobhunter.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {


    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ApiMessage("Upload a file")
    @PostMapping("/files")
    public ResponseEntity<FileResponseDTO> upload(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        String fullDirectoryPath = FileConstant.getBaseURI() + folder;

        if(file == null || file.isEmpty()) {
            throw new StorageException("File is empty!!!");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        boolean isValid = allowedExtensions.stream().anyMatch(it -> fileName.toLowerCase().endsWith("."+ it));

        if(!isValid) {
            throw new StorageException("File extension is not valid!!!. Only accept "+ allowedExtensions.toString());
        }

        this.fileService.createUpLoadFolder(fullDirectoryPath);

        FileResponseDTO responseDTO = this.fileService.storeFile(file,fullDirectoryPath);

        return ResponseEntity.ok().body(responseDTO);
    }
}
