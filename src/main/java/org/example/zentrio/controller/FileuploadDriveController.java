package org.example.zentrio.controller;


import com.google.api.services.drive.model.File;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.Res;
import org.example.zentrio.enums.FileTypes;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FileuploadDriveController {

    private final FileUploadService fileUploadService;


    @GetMapping("/get-file-by-documentId")
    @Operation(summary = "Get All File By Document-Id")
    public ResponseEntity<ApiResponse<List<File>>> getAllFilesByDocumentId(@RequestParam String accessToken,
                                            @RequestParam UUID documentId) throws IOException, GeneralSecurityException {

        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("All File Get Successfully ")
                .payload(fileUploadService.getAllFilesByDocumentId(accessToken,documentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-files-by-type")
    @Operation(summary = "Get Files By Files Type")
    public ResponseEntity<ApiResponse<List<File>>> getFilesByMimeTypeInDocument(
            @RequestParam String accessToken,
            @RequestParam  String mimeType ,
            @RequestParam  UUID documentId ) throws IOException, GeneralSecurityException {

        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("file get successfully ")
                .payload(fileUploadService.getFilesByMimeTypeInDocument(accessToken,mimeType, documentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/get-file-by-fileId")
    @Operation(summary = "Get File By File-Id")
    public ResponseEntity<ApiResponse<File>> getFileById(
            @RequestParam String accessToken,
            @RequestParam String fileId,
            @RequestParam UUID documentId) throws GeneralSecurityException, IOException {

        ApiResponse<File> response = ApiResponse.<File>builder()
                .success(true)
                .message("file get successfully ")
                .payload(fileUploadService.getFileById(accessToken, fileId,documentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/delete-file-by-Id")
    @Operation(summary = "Delete File By File-Id")
    public ResponseEntity<ApiResponse<String>> deleteFileById(
            @RequestParam String accessToken,
            @RequestParam String fileId) throws GeneralSecurityException, IOException {
        fileUploadService.deleteFileById(accessToken, fileId);
        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Delete File Successfully ")
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
            @RequestParam("type") FileTypes type, // e.g., doc, sheet, slide
            @RequestParam(value = "folderId", required = false) String folderId) throws GeneralSecurityException, IOException {
        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("file create Successfully ")
                .status(HttpStatus.ACCEPTED)
                .payload(fileUploadService.createDriveFile(accessToken, name, type, folderId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);


    }


    @PatchMapping("/rename")
    public ResponseEntity<ApiResponse<File>> renameFile(
            @RequestParam("accessToken") String accessToken,
            @RequestParam("fileId") String fileId,
            @RequestParam("newName") String newName) throws GeneralSecurityException, IOException {
        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("rename file  successfully ")
                .payload(fileUploadService.renameDriveFile(accessToken, fileId, newName))
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/upload-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Res>> uploadWithUserToken(
            @RequestParam("token") String accessToken,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException, GeneralSecurityException {

        ApiResponse<Res> response =  ApiResponse.<Res>builder()
                .success(true)
                .message("File Upload To Drive Successfully ")
                .payload(fileUploadService.uploadImageToRootDrive(accessToken, multipartFile))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    @PostMapping(value = "/upload-all-kinds-file-to-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload All Kinds Of Files ")
    public ResponseEntity<ApiResponse<Res>>  uploadImageToFolderDrive (
            @RequestParam("token") String accessToken,
            @RequestParam("folderId") String folderId,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException, GeneralSecurityException {

        ApiResponse<Res> response =  ApiResponse.<Res>builder()
                .success(true)
                .message("File Upload  Succesfsully ")
                .payload(fileUploadService.uploadImageToDocument(accessToken, folderId, multipartFile))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();


        return ResponseEntity.ok(response);


    }







}
