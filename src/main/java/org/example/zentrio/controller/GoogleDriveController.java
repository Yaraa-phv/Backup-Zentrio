package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.ApiResponse;

import org.example.zentrio.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/folder")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GoogleDriveController {

    private final FolderService folderService;




    @GetMapping("/getFolders")
    @Operation(summary = "Get All folder")
    public ResponseEntity<ApiResponse<List<File>>>  getFolders(@RequestParam String accessToken) throws IOException, GeneralSecurityException {
        // Call the DriveService to get the folders
        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("Get All folder Successfully ")
                .payload(folderService.getAllFolders(accessToken))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getFolders-byFolderid")
    @Operation(summary = "Get Folder By Folder-Id")
    public ResponseEntity<ApiResponse<File>> getFolderById(@RequestParam String accessToken, @RequestParam String folderId) throws IOException, GeneralSecurityException {

        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("Get folder By id  Successfully ")
                .payload(folderService.getFolderById(accessToken, folderId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }




    @PostMapping("/createFolder")
    @Operation(summary = "Create Folder")
    public ResponseEntity<ApiResponse<File>> createFolder(@RequestParam String accessToken,
                               @RequestParam String folderName,
                               @RequestParam(required = false) String parentFolderId)
            throws IOException, GeneralSecurityException {
        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("Folder create Successfully ")
                .payload(folderService.createFolder(accessToken, folderName, parentFolderId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete-folder")
    @Operation(summary = "Delete Folder By Folder-Id")
    public ResponseEntity<ApiResponse<String>> deleteFolder(@RequestParam String accessToken, @RequestParam String folderId) throws GeneralSecurityException, IOException {


        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Folder Deleted Successfully ")
                .payload(folderService.deleteFolder(accessToken, folderId))
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-folder-name")
    @Operation(summary = "Update Folder By Folder-Id")
    public ResponseEntity<ApiResponse<File>> updateFolderName(@RequestParam String accessToken,
                                                   @RequestParam String folderId,
                                                   @RequestParam String newFolderName) throws GeneralSecurityException, IOException {

        ApiResponse<File> response =  ApiResponse.<File>builder()
                .success(true)
                .message("File Update Successfully ")
                .payload(  folderService.updateFolderName(accessToken, folderId, newFolderName))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    // Endpoint to share a folder with a specific email
    @PostMapping("/share-folder")
    @Operation(summary = "Share Folder To Another User")
    public ResponseEntity<ApiResponse<String>> shareFolder(@RequestParam String folderId,
                              @RequestParam String emailAddress,
                              @RequestParam String accessToken) throws GeneralSecurityException, IOException {
        folderService.shareFolder(folderId, emailAddress, accessToken);
        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Folder Share  Successfully ")
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    @PostMapping("/public-folder")
    public ResponseEntity<ApiResponse<String>> publicfolder(@RequestParam String folderId,
                               @RequestParam String accessToken) throws GeneralSecurityException, IOException {

        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("file create succesfully ")
                .payload(folderService.publicfolder(folderId, accessToken))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }



    @GetMapping("/getFolders-title")
    public  ResponseEntity<ApiResponse<List<File>>> getFoldersByName(@RequestParam String accessToken, @RequestParam String folderName) throws IOException, GeneralSecurityException {
        // Call the DriveService to get the folders
        ApiResponse<List<File>> response =  ApiResponse.<List<File>>builder()
                .success(true)
                .message("file create succesfully ")
                .payload(folderService.getFoldersByName(accessToken, folderName))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }



}
