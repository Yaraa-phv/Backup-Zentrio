package org.example.zentrio.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import com.google.api.services.drive.model.Permission;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.enums.FileTypes;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.Document;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.DocumentRepository;
import org.example.zentrio.service.DocumentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final BoardRepository boardRepository;

    public UUID userId (){
        AppUser appUser= (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    public Drive createDriveService(String accessToken) throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Drive drive= new  Drive.Builder(httpTransport, jsonFactory, requestInitializer)
                .setApplicationName("User Drive Uploader")
                .build();


        // 🔎 Validate the token by making a simple request
        try {
            drive.files().list()
                    .setPageSize(1)
                    .setFields("files(id)")  // Lightweight call
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 401) {
               throw  new BadRequestException("❌ Access token is expired or invalid.");
            } else {
                throw e;
            }
        }
        return drive;
    }


    @Override
    public List<Document> getAllDocuments(UUID boardId) {
//
        Board board= boardRepository.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board not found");
        }

        return documentRepository.getAllFolders(boardId, userId());
    }

    @Override
    public List<Document> getAllPublicDocument() {
        List<Document> documents= documentRepository.getAllPublicDocument( userId());
        if (documents.isEmpty()) {
            throw new NotFoundException("No Public Document Found");
        }
        return documents;
    }

    @Override
    public List<Document> getDocumentByType(UUID boardId, FileTypes mimeType) {
        List<Document> document= getAllDocuments(boardId);
        if (document.isEmpty()) {
            throw new NotFoundException("Document not found");
        }
        String mimeTypeString = mimeType.toString();
        String type = getGoogleMimeType(mimeTypeString);
        List<Document> documentList = documentRepository.getDocumentByType(userId(),boardId,type);
        if (documentList.isEmpty()) {
            throw new NotFoundException("Document not found");
        }
        return documentList;

    }

    @Override
    public Document getDocumentById(UUID documentId){


        Document document= documentRepository.getDocumentById(documentId);
        if (document == null) {
            throw new NotFoundException("Document not found");
        }
        return document;
    }


    public static String getGoogleMimeType(String input) {
        switch (input.toLowerCase()) {
            case "doc":
            case "document":
                return "application/vnd.google-apps.document";
            case "sheet":
            case "spreadsheet":
                return "application/vnd.google-apps.spreadsheet";
            case "slide":
            case "presentation":
                return "application/vnd.google-apps.presentation";
            case "folder":
                return "application/vnd.google-apps.folder";
            default:
                throw new IllegalArgumentException("Unsupported file type: " + input);
        }
    }



    @Override
    public void shareFolder(String folderId, String emailAddress, String accessToken) throws IOException, GeneralSecurityException {
        Drive drive = createDriveService(accessToken);

        File folder = drive.files().get(folderId)
                .setFields("id, name, webViewLink, iconLink")
                .setSupportsAllDrives(true)
                .execute();
        if (!"application/vnd.google-apps.folder".equals(folder.getMimeType())) {
            throw new NotFoundException("The provided ID does not refer to a folder.");
        }

        // Create permission object
        Permission permission = new Permission()
                .setType("user") // Share with a user
                .setRole("reader") // User will have "reader" access
                .setEmailAddress(emailAddress);

        // Apply the permission to the folder
        drive.permissions().create(folderId, permission).setSendNotificationEmail(true).execute();

        System.out.println("Folder shared successfully with: " + emailAddress);
    }



@Override
    public String publicDocument(UUID documentId, String accessToken) throws GeneralSecurityException, IOException {

        Document document= documentRepository.getDocumentById(documentId);
        if (document == null) {
            throw new NotFoundException("Document not found");
        }
        String folderId= document.getFolderId();
        Drive drive = createDriveService(accessToken);

        File folder = drive.files().get(folderId)
                .setFields("id, name, webViewLink, iconLink")
                .setSupportsAllDrives(true)
                .execute();


        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");

        drive.permissions().create(folderId, permission)
                .setSendNotificationEmail(false)
                .setSupportsAllDrives(true)
                .execute();
        documentRepository.publicFolder( userId(),documentId,true);
        return  "Folder shared successfully with: " + folder.getWebViewLink();
    }

    @Override
    public List<Document> getDocumentByName( String documentName, UUID boardId)  {

        Board board= boardRepository.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Document not found");
        }
        List<Document>  documents= documentRepository.getDocumentByName(boardId, documentName , userId());
        if (documents.isEmpty()) {
            throw new NotFoundException("Document not found");
        }
        return documents;
    }

    @Override
    public Document createFolder(String accessToken, String folderName, FileTypes types, String parentFolderId, UUID boardId) throws GeneralSecurityException, IOException {

        Board board= boardRepository.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board not found with ID: " + boardId);
        }
        String documentType= getGoogleMimeType(types.toString());
        Drive drive= createDriveService(accessToken);

        // Prepare folder metadata
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType(documentType);

        // Set parent folder if provided
        if (parentFolderId != null && !parentFolderId.isEmpty()) {
            fileMetadata.setParents(List.of(parentFolderId));
        }

        // Create the folder

            File folder = drive.files().create(fileMetadata)
                    .setFields("id, name,mimeType, webViewLink")  // Only requesting the 'id' field from the response
                    .execute();
            if (folder == null) {
                throw new NotFoundException("No folder created");
            }
        Document document= documentRepository.createFolder(LocalDateTime.now() , folder.getMimeType() , userId() , boardId , folder.getWebViewLink() , folder.getName(),  folder.getId());


            return document;

    }

    @Override
    public Document updateFolderName(String accessToken, UUID documentId, String newFolderName) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService(accessToken);

        Document document= documentRepository.getDocumentById(documentId);
        if (document == null) {
            throw new NotFoundException("Document not found with ID: " + documentId);
        }
        String folderId =document.getFolderId();

        // Prepare the updated metadata
        File fileMetadata = new File();
        fileMetadata.setName(newFolderName);
        // Optional: check that the file is actually a folder before updating

        // Update the folder's metadata
        File updatedFolder;

        try {
            updatedFolder = drive.files().update(folderId, fileMetadata)
                    .setFields("id, name, webViewLink")  // Specify the fields to be returned (name in this case)
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NotFoundException("Folder not found with ID: " + folderId);
            }
            throw e; // rethrow other unexpected Google exceptions
        }
        File existingFile = drive.files().get(folderId).setFields("mimeType").execute();
        if (!"application/vnd.google-apps.folder".equals(existingFile.getMimeType())) {
            throw new NotFoundException("The provided ID is not a folder.");
        }
        Document  updateDocument = documentRepository.updateFolderName(userId(), document.getBoardId(), newFolderName);

       return  updateDocument;

    }

    @Override
    public String deleteDocumentById(String accessToken, UUID documentId)
            throws GeneralSecurityException, IOException {
        Document document= documentRepository.getDocumentById(documentId);
        if (document == null) {
            throw new NotFoundException("Document not found with ID: " + documentId);
        }
        String folderId= document.getFolderId();

        Drive drive = createDriveService(accessToken);

        File fileMetadata;
        try {
            fileMetadata = drive.files().get(folderId)
                    .setFields("id, name, webViewLink, mimeType")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NotFoundException("Folder not found with ID: " + folderId);
            }
            throw e; // rethrow other unexpected Google exceptions
        }

        if (!"application/vnd.google-apps.folder".equals(fileMetadata.getMimeType())) {
            throw new NotFoundException("The provided ID does not refer to a folder.");
        }

        drive.files().delete(folderId).execute();
        documentRepository.deleteDocumentById(documentId);

        return "Folder deleted with ID: " + folderId;
    }



}
