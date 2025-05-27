package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.ApiResponse;

import org.example.zentrio.enums.FileTypes;
import org.example.zentrio.model.Document;
import org.example.zentrio.service.DocumentService;
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
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;




    @GetMapping("/boards/{board-id}")
    @Operation(summary = "Get all documents  by board id")
    public ResponseEntity<ApiResponse<List<Document>>>  getAllDocuments(@PathVariable("board-id") UUID boardId) {
        // Call the DriveService to get the folders
        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Get all folder Successfully ")
                .payload(documentService.getAllDocuments(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{document-id}")
    @Operation(summary = "Get document by document id")
    public ResponseEntity<ApiResponse<Document>> getDocumentById( @PathVariable("document-id") UUID documentId) {

        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Get document by id  successfully ")
                .payload(documentService.getDocumentById( documentId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }




    @PostMapping()
    @Operation(summary = "Create document")
    public ResponseEntity<ApiResponse<Document>> createDocument(@RequestParam String accessToken,
                                                                @RequestParam String folderName,
                                                                @RequestParam FileTypes types,
                                                                @RequestParam(required = false) String parentFolderId, @RequestParam  UUID boardId)
            throws IOException, GeneralSecurityException {
        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Document created Successfully ")
                .payload(documentService.createFolder(accessToken, folderName,types, parentFolderId,  boardId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(response);
    }




    @PostMapping(value = "boards/{board-id}/"  ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create document by upload")
    public ResponseEntity<ApiResponse<Document>> uploadDocument(@RequestParam String accessToken,
                                                                @RequestParam MultipartFile multipartFile,
                                                                @PathVariable("board-id")  UUID boardId)
            throws IOException, GeneralSecurityException {
        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Folder create Successfully ")
                .payload(documentService.uploadDocumentToDrive(accessToken, multipartFile,boardId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(response);
    }




    @DeleteMapping("/{document-id}")
    @Operation(summary = "Delete document by document-id")
    public ResponseEntity<ApiResponse<Void>> deleteDocumentById(@RequestParam String accessToken,
                                                                  @PathVariable("document-id") UUID  documentId) throws GeneralSecurityException, IOException {

            documentService.deleteDocumentById(accessToken, documentId);
        ApiResponse<Void> response =  ApiResponse.<Void>builder()
                .success(true)
                .message("Document Deleted Successfully ")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{document-id}")
    @Operation(summary = "Update document name document id")
    public ResponseEntity<ApiResponse<Document>> updateDocumentName(@RequestParam String accessToken,
                                                                    @PathVariable("document-id") UUID documentId,
                                                                    @RequestParam String documentName) throws GeneralSecurityException, IOException {

        ApiResponse<Document> response =  ApiResponse.<Document>builder()
                .success(true)
                .message("Document Update Successfully ")
                .payload(  documentService.updateDocumentName(accessToken, documentId, documentName))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    // Endpoint to share a folder with a specific email
    @PostMapping("/share-document")
    @Operation(summary = "Share Document with Specific   User")
    public ResponseEntity<ApiResponse<String>> shareFolder(
                                @RequestParam String folderId,
                              @RequestParam String emailAddress,
                              @RequestParam String accessToken) throws GeneralSecurityException, IOException {
        documentService.shareFolder(folderId, emailAddress, accessToken);
        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("Folder Share  Successfully ")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }


    @PutMapping("/public-document/{document-id}")
    @Operation(summary = "Public document by document id")
    public ResponseEntity<ApiResponse<String>> publicDocument(@PathVariable("document-id") UUID documentId,
                               @RequestParam String accessToken) throws GeneralSecurityException, IOException {

        ApiResponse<String> response =  ApiResponse.<String>builder()
                .success(true)
                .message("file create successfully ")
                .payload(documentService.publicDocument(documentId, accessToken))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("get-public-documents/boards/{board-id}")
    @Operation(summary = "Get all public documents")
    public ResponseEntity<ApiResponse<List<Document>>> getAllPublicDocument(
            @PathVariable("board-id") UUID boardId)  {

        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Get all public  document successfully ")
                .payload(documentService.getAllPublicDocument(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }



    @GetMapping("boards/{board-id}/document/{document-name}")
    @Operation(summary = "Get Document By Name")
    public  ResponseEntity<ApiResponse<List<Document>>> getDocumentByName(
            @PathVariable("document-name") String DocumentName,
            @PathVariable("board-id") UUID boardId) {
        // Call the DriveService to get the folders
        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("Document Get By Name Successfully ")
                .payload(documentService.getDocumentByName( DocumentName, boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/boards/{board-id}/file-type/{mimeType}")
    @Operation(summary = "Get Document By Document Type")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentByType(
            @PathVariable("board-id")  UUID boardId ,
            @PathVariable("mimeType")  FileTypes mimeType )  {

        ApiResponse<List<Document>> response =  ApiResponse.<List<Document>>builder()
                .success(true)
                .message("file get successfully ")
                .payload(documentService.getDocumentByType(boardId,mimeType))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);

    }



}
