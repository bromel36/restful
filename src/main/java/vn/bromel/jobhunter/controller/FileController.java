package vn.bromel.jobhunter.controller;


import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.bromel.jobhunter.domain.response.FileResponseDTO;
import vn.bromel.jobhunter.service.FileService;
import vn.bromel.jobhunter.util.annotation.ApiMessage;
import vn.bromel.jobhunter.util.constant.FileConstant;
import vn.bromel.jobhunter.util.error.StorageException;

import java.io.FileNotFoundException;
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

    @GetMapping("/files")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("file") String file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, FileNotFoundException {

        if(file.isBlank() || folder.isBlank()) {
            throw new StorageException("File or folder is empty!!!");
        }

        long fileLength = this.fileService.getFileLength(file, folder);

        if(fileLength == 0){
            throw new StorageException("File name with "+ file +" not found");
        }
        InputStreamResource resource = this.fileService.getResource(file, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
