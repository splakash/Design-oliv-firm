package com.design.agency.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.design.agency.Repository.projectRepository;
import com.design.agency.Service.S3Service;
import com.design.agency.WorkEntity.Projects;



import java.io.File;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")

public class ImageController {

    @Autowired
    private S3Service s3Service;
    
    @Autowired
    private projectRepository projectRepo;

    private final String bucketName = "myfirstbuck2726";
   // private static final String HTML_FOLDER = "D:/Spring boot/agency/src/main/frontend/";
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
    @RequestParam("title") String title, 
    @RequestParam("longDescription") String longDescription,
    @RequestParam("coverDescription") String coverDescription,
    @RequestParam("folderId") String folderId
    ) {
        try {
            String folderUrl = s3Service.createFolder(folderId);
            File convertedFile = convertMultiPartToFile(file);
            String fileUrl = s3Service.uploadFile(bucketName, file.getOriginalFilename(), convertedFile);
            Projects project = new Projects();
            project.setTitle(title);
            project.setCoverDescription(coverDescription);
            project.setCoverImage(fileUrl);
            project.setLongDescription(longDescription);
            project.setFolderId(folderUrl);
            projectRepo.save(project);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    // uploading images inside the s3 folder
    @PostMapping("/upload/{folderName}")
    public ResponseEntity<List<String>> uploadFiles(@PathVariable String folderName,
                                                    @RequestParam("file") MultipartFile[] files,
                                                    @RequestParam("partitionType") String partitionType
                                                    ) {
        List<String> response = s3Service.uploadFilesToFolder(folderName, files,partitionType);
        return ResponseEntity.ok(response);
    }

    //get mapping for cover image
    @GetMapping("/coverImages")
    public ResponseEntity<List<List<String>>>getAllCover(){
        List<List<String>> imageurls = projectRepo.findImagesLink();
        return ResponseEntity.ok(imageurls);
    }

    //get mapping for all the images inside the folder
    @GetMapping("/works")
    public List<Map<String, String>> getImages(@RequestParam String folderName) {
        return s3Service.listObjectsWithMetadata(bucketName, folderName);
    }

    @GetMapping("/details")
    public Optional<Projects> getMethodName(@RequestParam Long id) {
        return projectRepo.findById(id);
    }
    
    @GetMapping("/all")
    public List<Projects>getall(){
        return projectRepo.findAll();
    }

    @GetMapping("/idTitle")
    public List<List<String>>fieldIdTitle(){
        List<List<String>> idTitleList = projectRepo.fieldIdandTitle();
        return idTitleList;
        
    }
}