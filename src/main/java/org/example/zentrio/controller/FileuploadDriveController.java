package org.example.zentrio.controller;


import com.google.api.services.drive.model.File;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.Res;
import org.example.zentrio.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FileuploadDriveController {

    private final FileUploadService fileSevice;


    @GetMapping("/getFileBy-FolderID")
    public ResponseEntity<ApiResponse<List<File>>> getAllFilesByFolderId(@RequestParam String accessToken,
                                            @RequestParam String folderId) throws IOException, GeneralSecurityException {

        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("file get succesfully ")
                .payload(fileSevice.getAllFilesByFolderId(accessToken,folderId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getFileBy-typ")
    public ResponseEntity<ApiResponse<List<File>>> getFilesByMimeTypeInFolder(@RequestParam String accessToken,
                                                 @RequestParam  String mimeType ,
                                                 @RequestParam  String folderId ) throws IOException, GeneralSecurityException {

        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("file get successfully ")
                .payload(fileSevice.getFilesByMimeTypeInFolder(accessToken,mimeType, folderId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
        // Call the DriveService to get the folders
     //   return fileSevice.getFilesByMimeTypeInFolder(accessToken,mimeType, folderId);
    }

    @GetMapping("/file")
    public ResponseEntity<ApiResponse<File>> getFileById(@RequestParam String accessToken,
                                         @RequestParam String fileId) throws GeneralSecurityException, IOException {

        ApiResponse<File> response = ApiResponse.<File>builder()
                .success(true)
                .message("file get successfully ")
                .payload(fileSevice.getFileById(accessToken, fileId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/file")
    public ResponseEntity<ApiResponse<String>> deleteFileById(@RequestParam String accessToken,
                                                 @RequestParam String fileId) throws GeneralSecurityException, IOException {
        fileSevice.deleteFileById(accessToken, fileId);
        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("file create succesfully ")
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
//        fileSevice.deleteFileById(accessToken, fileId);
//        return ResponseEntity.ok("File deleted successfully.");
    }



    @PostMapping("/create")
    public ResponseEntity<ApiResponse<File>> createDriveFile(
            @RequestParam("accessToken") String accessToken,
            @RequestParam("name") String name,
            @RequestParam("type") String type, // e.g., doc, sheet, slide
            @RequestParam(value = "folderId", required = false) String folderId) throws GeneralSecurityException, IOException {
        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("file create succesfully ")
                .status(HttpStatus.ACCEPTED)
                .payload(fileSevice.createDriveFile(accessToken, name, type, folderId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);


//        try {
//            String fileId = fileSevice.createDriveFile(accessToken, name, type, folderId);
//            return ResponseEntity.ok("File created successfully. File ID: " + fileId);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Invalid file type: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error creating file: " + e.getMessage());
//        }
    }


    @PatchMapping("/rename")
    public ResponseEntity<ApiResponse<File>> renameFile(
            @RequestParam("accessToken") String accessToken,
            @RequestParam("fileId") String fileId,
            @RequestParam("newName") String newName) throws GeneralSecurityException, IOException {
        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("rename file  successfully ")
                .payload(fileSevice.renameDriveFile(accessToken, fileId, newName))
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
//
//        try {
//            String renamed = fileSevice.renameDriveFile(accessToken, fileId, newName);
//            return ResponseEntity.ok("File renamed successfully to: " + renamed);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to rename file: " + e.getMessage());
//        }
    }


    @PostMapping(value = "/upload-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Res>> uploadWithUserToken(
            @RequestParam("token") String accessToken,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException, GeneralSecurityException {

        ApiResponse<Res> response =  ApiResponse.<Res>builder()
                .success(true)
                .message("file create succesfully ")
                .payload(fileSevice.uploadImageToRootDrive(accessToken, multipartFile))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    @PostMapping(value = "/upload-image-to-drive", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Res>>  uploadImageToFolderDrive (
            @RequestParam("token") String accessToken,
            @RequestParam("folderId") String folderId,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException, GeneralSecurityException {

        ApiResponse<Res> response =  ApiResponse.<Res>builder()
                .success(true)
                .message("file create succesfully ")
                .payload(fileSevice.uploadImageToFolderDrive(accessToken, folderId, multipartFile))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();


        return ResponseEntity.ok(response);


    }







}
