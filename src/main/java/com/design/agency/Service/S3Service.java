package com.design.agency.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
//import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;

import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class S3Service {

    private final S3Client s3Client;



    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_WEST_2) // Choose your region
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    public String uploadFile(String bucketName, String key, File file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
   



public String createFolder(String folderName) {
    try {
        // Ensure folder name ends with "/"
        if (!folderName.endsWith("/")) {
            folderName += "/";
        }

        // Create an empty object to represent the folder
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("myfirstbuck2726")
                .key(folderName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.empty());

        return folderName;
    } catch (Exception e) {
        throw new RuntimeException("Error creating folder in S3: " + e.getMessage(), e);
    }
}

 public List<String> uploadFilesToFolder(String folderName, MultipartFile[] files, String partitionType) {
        List<String> fileUrls = new ArrayList<>();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("partitiontype", partitionType);
        
        for (MultipartFile file : files) {
            try {
                // Validate folder name to ensure it has '/'
                if (!folderName.endsWith("/")) {
                    folderName += "/";
                }

                // Generate the file path within the folder
                String filePath = folderName + file.getOriginalFilename();

                // Create S3 PutObjectRequest
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket("myfirstbuck2726")
                        .key(filePath)
                        .metadata(metadata)
                        .build();

                // Upload File to S3
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

                // Construct the file URL
                String fileUrl = "https://myfirstbuck2726.s3.amazonaws.com/" + filePath;
                fileUrls.add(fileUrl);

            } catch (IOException | S3Exception e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
            }
        }

        return fileUrls;
    }


   
     public List<Map<String, String>> listObjectsWithMetadata(String bucketName, String folderName) {
    ListObjectsV2Request request = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(folderName + "/") // Ensure folder ends with slash
            .build();

    ListObjectsV2Response result = s3Client.listObjectsV2(request);

    List<Map<String, String>> filesWithMetadata = new ArrayList<>();

    for (S3Object object : result.contents()) {
        String key = object.key();

        // Get metadata
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        HeadObjectResponse headResponse = s3Client.headObject(headRequest);
        Map<String, String> metadata = headResponse.metadata();

        // Generate S3 URL (optional but useful for frontend)
        String url = s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()).toString();

        // Combine data
        Map<String, String> fileInfo = new HashMap<>();
        fileInfo.put("key", key);
        fileInfo.put("url", url);
        fileInfo.put("partitionType", metadata.getOrDefault("partitiontype", "1")); // fallback to "1"

        filesWithMetadata.add(fileInfo);
    }

    return filesWithMetadata;
}

    
}