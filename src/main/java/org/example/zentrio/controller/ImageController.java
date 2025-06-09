package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.FileMetadata;
import org.example.zentrio.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
@Tag(name = "Images Controller")
@SecurityRequirement(name = "bearerAuth")
public class ImageController {
    private final FileService fileService;

    @Operation(summary = "Upload a file", description = "Uploads a file and returns metadata about the uploaded file.")
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetadata>> uploadFile(@RequestParam MultipartFile file) {
        System.out.println("Uploading file " + file.getOriginalFilename());
        FileMetadata fileMetadata = fileService.uploadFile(file);
        ApiResponse<FileMetadata> apiResponse = ApiResponse.<FileMetadata>builder()
                .success(true)
                .message("Upload file successfully.")
                .payload(fileMetadata)
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Preview a file", description = "Fetches a file by its name and returns the file as a byte array (usually for image previews).")
    @GetMapping("preview-file/{file-name}")
    public ResponseEntity<?> getFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        InputStream inputStream = fileService.getFileByFileName(fileName);
        byte[] fileContent = inputStream.readAllBytes();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(fileContent);
    }

    @Operation(summary = "Upload multiple files  ", description = "Uploads multiple files and returns metadata for each uploaded file.")
    @PostMapping(value = "/boards/{board-id}/upload-multiple-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileMetadata>>> uploadMultipleFiles(
            @PathVariable("board-id") UUID boardId,
            @RequestParam("files") List<MultipartFile> files) {

        System.out.println("Uploading " + files.size() + " files");
        List<FileMetadata> metadataList = fileService.uploadFiles(boardId,files);

        ApiResponse<List<FileMetadata>> apiResponse = ApiResponse.<List<FileMetadata>>builder()
                .success(true)
                .message("Uploaded " + metadataList.size() + " file(s) successfully.")
                .payload(metadataList)
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
