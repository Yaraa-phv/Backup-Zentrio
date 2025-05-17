package org.example.zentrio.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import com.google.api.services.drive.model.Permission;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.service.FolderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


@Service
public class FolderServiceImpl implements FolderService {

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
    public List<File> getAllFolders(String accessToken) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService(accessToken);
        // ——— List only folders ———
        FileList result = drive.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder' and trashed = false")
                .setFields("nextPageToken, files(id, name, mimeType,webViewLink)")
                .execute();

        List<File> folders = result.getFiles();
        if (folders == null || folders.isEmpty()) {
            throw new NotFoundException("No files found");
        }

        return folders;
    }

    @Override
    public File getFolderById(String accessToken, String folderId) throws GeneralSecurityException, IOException {
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
        return fileMetadata;
    }


    private static String getReadableType(String mimeType) {
        return switch (mimeType) {
            case "application/vnd.google-apps.folder" -> "Folder";
            case "application/vnd.google-apps.document" -> "Google Docs";
            case "application/vnd.google-apps.spreadsheet" -> "Google Sheets";
            case "application/vnd.google-apps.presentation" -> "Google Slides";
            case "application/pdf" -> "PDF";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "Word Document";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "Excel Spreadsheet";
            default -> mimeType;
        };

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
    public String publicfolder(String folderId, String accessToken) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService(accessToken);

        File folder = drive.files().get(folderId)
                .setFields("id, name, webViewLink, iconLink")
                .setSupportsAllDrives(true)
                .execute();


    if (!"application/vnd.google-apps.folder".equals(folder.getMimeType())) {
        throw new NotFoundException("The provided ID does not refer to a folder.");
    }

        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");

        drive.permissions().create(folderId, permission)
                .setSendNotificationEmail(false)
                .setSupportsAllDrives(true)
                .execute();

        return  "Folder shared successfully with: " + folder.getWebViewLink();
    }

    @Override
    public List<File> getFoldersByName(String accessToken, String folderName) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService(accessToken);

        // Query: match folder name + mimeType for folders + not trashed
        String query = String.format(
                "name contains '%s' and mimeType = 'application/vnd.google-apps.folder' and trashed = false",
                folderName
        );

        FileList result = drive.files().list()
                .setQ(query)
                .setOrderBy("name") // Sort A–Z by name
                .setFields("files(id, name, mimeType, webViewLink)")
                .execute();

        List<File> folders = result.getFiles();

        if (folders == null || folders.isEmpty()) {
            System.out.println("No folders found with name: " + folderName);
            return List.of();
        }

        System.out.println("Matching folders:");
        for (File folder : folders) {
            System.out.printf("Name: %s | ID: %s | Link: %s\n",
                    folder.getName(), folder.getId(), folder.getWebViewLink());
        }

        return folders;
    }

    @Override
    public File createFolder(String accessToken, String folderName, String parentFolderId) throws GeneralSecurityException, IOException {

        Drive drive= createDriveService(accessToken);

        // Prepare folder metadata
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        // Set parent folder if provided
        if (parentFolderId != null && !parentFolderId.isEmpty()) {
            fileMetadata.setParents(List.of(parentFolderId));
        }

        // Create the folder

            File folder = drive.files().create(fileMetadata)
                    .setFields("id, name, webViewLink")  // Only requesting the 'id' field from the response
                    .execute();
            if (folder == null) {
                throw new NotFoundException("No folder created");
            }
        System.out.printf("Name: %s | ID: %s | Link: %s\n",
                folder.getName(), folder.getId(), folder.getWebViewLink());
            return folder;

    }

    @Override
    public File updateFolderName(String accessToken, String folderId, String newFolderName) throws GeneralSecurityException, IOException {
        Drive drive = createDriveService(accessToken);

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

        System.out.printf("Name: %s | ID: %s | Link: %s\n",
                updatedFolder.getName(), updatedFolder.getId(), updatedFolder.getWebViewLink());
            return updatedFolder;

    }

    @Override
    public String deleteFolder(String accessToken, String folderId)
            throws GeneralSecurityException, IOException {

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

        return "Folder deleted with ID: " + folderId;
    }



}
