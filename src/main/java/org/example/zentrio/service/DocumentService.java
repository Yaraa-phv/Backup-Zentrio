package org.example.zentrio.service;

import com.google.api.services.drive.model.File;
import org.example.zentrio.enums.FileTypes;
import org.example.zentrio.model.Document;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public interface DocumentService {
    String deleteDocumentById(String accessToken, UUID documentId) throws GeneralSecurityException, IOException;

    Document updateFolderName(String accessToken, UUID documentId, String newFolderName) throws GeneralSecurityException, IOException;

    void shareFolder(String folderId, String emailAddress, String accessToken) throws IOException, GeneralSecurityException;

    String publicDocument(UUID documentId, String accessToken) throws GeneralSecurityException, IOException;

    List<Document> getDocumentByName( String folderName, UUID boardId) ;

    Document createFolder(String accessToken, String folderName, FileTypes types, String parentFolderId, UUID boardId) throws GeneralSecurityException, IOException;

    Document getDocumentById(UUID documentId) ;

    List<Document> getAllDocuments(UUID accessToken);


    List<Document> getAllPublicDocument();


    List<Document> getDocumentByType(UUID boardId, FileTypes mimeType);
}
