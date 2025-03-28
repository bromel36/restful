package vn.bromel.jobhunter.service;


import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.bromel.jobhunter.domain.response.FileResponseDTO;
import vn.bromel.jobhunter.util.constant.FileConstant;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
public class FileService {

    public void createUpLoadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory()){
            try{
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>>>> created new directory successful "+ tmpDir.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println(">>>>>skip making, directory already exists");
        }
    }

    public FileResponseDTO storeFile(MultipartFile file, String folder) throws URISyntaxException, IOException {

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()); //handle file name contains space character

        URI uri = new URI(folder+ "/" + encodedFileName);

        Path path = Paths.get(uri);

        try(InputStream in = file.getInputStream()){
            Files.copy(in,path, StandardCopyOption.REPLACE_EXISTING);
        }
        return FileResponseDTO.builder()
                .fileName(encodedFileName)
                .uploadedTime(Instant.now())
                .build();
    }

    public long getFileLength(String file, String folder) throws URISyntaxException {
        URI uri = new URI(FileConstant.getBaseURI() + folder + "/" + file);

        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());

        if(tmpDir.isDirectory() || !tmpDir.exists()){
            return 0;
        }
        return tmpDir.length();
    }


    public InputStreamResource getResource(String file, String folder) throws FileNotFoundException, URISyntaxException {
        URI uri = new URI(FileConstant.getBaseURI() + folder + "/" + file);

        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());

        return new InputStreamResource(new FileInputStream(tmpDir));
    }
}
