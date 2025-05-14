package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.ApiResponse;

import org.example.zentrio.enums.FileTypes;
import org.example.zentrio.model.Document;
import org.example.zentrio.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/folder")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;




    @GetMapping("/get-all-document")
    @Operation(summary = "Get All Document By BoardID")
    public ResponseEntity<ApiResponse<List<Document>>>  getAllDocuments(@RequestParam UUID boardId) {
        // Call the DriveService to get the folders
        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Get All folder Successfully ")
                .payload(documentService.getAllDocuments(boardId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/get-document-by-documentId")
    @Operation(summary = "Get Document By Document-Id")
    public ResponseEntity<ApiResponse<Document>> getDocumentById( @RequestParam UUID documentId) {

        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Get Document By id  Successfully ")
                .payload(documentService.getDocumentById( documentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }




    @PostMapping("/create-document")
    @Operation(summary = "Create Document")
    public ResponseEntity<ApiResponse<Document>> createFolder(@RequestParam String accessToken,
                                                              @RequestParam String folderName,
                                                              @RequestParam FileTypes types,
                                                              @RequestParam(required = false) String parentFolderId, @RequestParam  UUID boardId)
            throws IOException, GeneralSecurityException {
        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Folder create Successfully ")
                .payload(documentService.createFolder(accessToken, folderName,types, parentFolderId,  boardId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete-document")
    @Operation(summary = "Delete Document By Folder-Id")
    public ResponseEntity<ApiResponse<String>> deleteDocumentById(@RequestParam String accessToken, @RequestParam UUID  documentId) throws GeneralSecurityException, IOException {


        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Document Deleted Successfully ")
                .payload(documentService.deleteDocumentById(accessToken, documentId))
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-document-name")
    @Operation(summary = "Update Document By Folder-Id")
    public ResponseEntity<ApiResponse<Document>> updateFolderName(@RequestParam String accessToken,
                                                   @RequestParam UUID documenetId,
                                                   @RequestParam String newFolderName) throws GeneralSecurityException, IOException {

        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Document Update Successfully ")
                .payload(  documentService.updateFolderName(accessToken, documenetId, newFolderName))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    // Endpoint to share a folder with a specific email
    @PostMapping("/share-document")
    @Operation(summary = "Share Document with Specific   User")
    public ResponseEntity<ApiResponse<String>> shareFolder(@RequestParam String folderId,
                              @RequestParam String emailAddress,
                              @RequestParam String accessToken) throws GeneralSecurityException, IOException {
        documentService.shareFolder(folderId, emailAddress, accessToken);
        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Folder Share  Successfully ")
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    @PostMapping("/public-document")
    @Operation(summary = "Public Document By Document-Id")
    public ResponseEntity<ApiResponse<String>> publicDocument(@RequestParam UUID documentId,
                               @RequestParam String accessToken) throws GeneralSecurityException, IOException {

        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("file create successfully ")
                .payload(documentService.publicDocument(documentId, accessToken))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/get-public-document")
    @Operation(summary = "Get All Public Document")
    public ResponseEntity<ApiResponse<List<Document>>> getAllPublicDocument()  {

        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Get All Public  Document Successfully ")
                .payload(documentService.getAllPublicDocument())
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }



    @GetMapping("/get-document-name")
    @Operation(summary = "Get Document By Name")
    public  ResponseEntity<ApiResponse<List<Document>>> getDocumentByName(
            @RequestParam String folderName,
            @RequestParam UUID boardId) {
        // Call the DriveService to get the folders
        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Document Get By Name Successfully ")
                .payload(documentService.getDocumentByName( folderName, boardId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-document-by-type")
    @Operation(summary = "Get Document By Document Type")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentByType(
            @RequestParam  UUID boardId ,
            @RequestParam  FileTypes mimeType )  {

        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("file get successfully ")
                .payload(documentService.getDocumentByType(boardId,mimeType))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }



}
