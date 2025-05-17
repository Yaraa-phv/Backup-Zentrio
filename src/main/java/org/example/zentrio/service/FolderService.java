package org.example.zentrio.service;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface FolderService {
    String deleteFolder(String accessToken, String folderId) throws GeneralSecurityException, IOException;

    File updateFolderName(String accessToken, String folderId, String newFolderName) throws GeneralSecurityException, IOException;

    void shareFolder(String folderId, String emailAddress, String accessToken) throws IOException, GeneralSecurityException;

    String publicfolder(String folderId, String accessToken) throws GeneralSecurityException, IOException;

    List<File> getFoldersByName(String accessToken, String folderName) throws GeneralSecurityException, IOException;

    File createFolder(String accessToken, String folderName, String parentFolderId) throws GeneralSecurityException, IOException;

    File getFolderById(String accessToken, String folderId) throws GeneralSecurityException, IOException;

    List<File> getAllFolders(String accessToken) throws GeneralSecurityException, IOException;


}
