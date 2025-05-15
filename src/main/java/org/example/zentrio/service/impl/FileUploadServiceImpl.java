package org.example.zentrio.service.impl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.response.Res;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final DocumentServiceImpl documentService;

    @Override
    public List<File> getAllFilesByFolderId(String accessToken, String folderId) throws IOException, GeneralSecurityException {

        Drive drive = documentService.createDriveService(accessToken);

        String query= String.format("'%s' in parents and trashed = false", folderId);

        FileList result =drive.files().list()
                .setQ(query)
                .setFields("nextPageToken, files(id, name, mimeType, webViewLink)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            throw new NotFoundException("No files found");
        }
        return files;
    }


    @Override
    public void deleteFileById(String accessToken, String fileId) throws GeneralSecurityException, IOException {
        Drive drive = documentService.createDriveService(accessToken);


        File fileMetadata;
        try {
            fileMetadata = drive.files().get(fileId)
                    .setFields("id, name, webViewLink, mimeType")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NotFoundException("File not found with ID: " + fileId);
            }
            throw e; // rethrow other unexpected Google exceptions
        }

        if ("application/vnd.google-apps.folder".equals(fileMetadata.getMimeType())) {
            throw new BadRequestException("This ID is not file id");
        }
        drive.files().delete(fileId).execute();
    }


    @Override
    public File getFileById(String accessToken, String fileId) throws GeneralSecurityException, IOException {
        Drive drive = documentService.createDriveService(accessToken);
        File fileMetadata;
        try {
            fileMetadata = drive.files().get(fileId)
                    .setFields("id, name, webViewLink, mimeType")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NotFoundException("File not found with ID: " + fileId);
            }
            throw e; // rethrow other unexpected Google exceptions
        }

        if ("application/vnd.google-apps.folder".equals(fileMetadata.getMimeType())) {
            throw new BadRequestException("Cannot get  File that id refer to folder");

        }
        return fileMetadata;
    }


    @Override
    public File createDriveFile(String accessToken, String name, String userInputType, String folderId) throws GeneralSecurityException, IOException {

        // Convert user input to official Google MIME type
        String mimeType = getGoogleMimeType(userInputType);

        Drive drive = documentService.createDriveService(accessToken);
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

        File file = new File();
        file.setName(name);
        file.setMimeType(mimeType);


        if (folderId != null && !folderId.isEmpty()) {
            file.setParents(Collections.singletonList(folderId));
        }

        File createdFile = drive.files().create(file)
                .setFields("id, name, mimeType")
                .execute();

        return createdFile;
    }


    @Override
    public File renameDriveFile(String accessToken, String fileId, String newName) throws GeneralSecurityException, IOException {

        Drive drive = documentService.createDriveService(accessToken);

        File fileMetadata;
        try {
            fileMetadata = drive.files().get(fileId)
                    .setFields("id, name, webViewLink, mimeType")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NotFoundException("Folder not found with ID: " + fileId);
            }
            throw e; // rethrow other unexpected Google exceptions
        }

        // If it's a folder, don't allow renaming here
        if ("application/vnd.google-apps.folder".equals(fileMetadata.getMimeType())) {
            throw new IllegalArgumentException("Cannot rename a folder using this method.");
        }

        // Proceed to rename the file

        File updatedFile = drive.files().update(fileId, fileMetadata)
                .setFields("id, name, webViewLink, mimeType")
                .execute();

        return updatedFile;
    }


    @Override
    public List<File> getFilesByMimeTypeInFolder(String accessToken, String mimeType, String folderId)
            throws GeneralSecurityException, IOException {
        String type = getGoogleMimeType(mimeType);

        Drive drive = documentService.createDriveService(accessToken);
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

        // If it's a folder, don't allow renaming here
        if (!"application/vnd.google-apps.folder".equals(fileMetadata.getMimeType())) {
            throw new IllegalArgumentException("This id not refer to a folder.");
        }

        String query = String.format(
                "'%s' in parents and mimeType = '%s' and trashed = false",
                folderId, type
        );

        FileList result = drive.files().list()
                .setQ(query)
                .setFields("files(id, name, mimeType, webViewLink)")
                .setOrderBy("name")
                .execute();

        List<File> files = result.getFiles();

        if (files == null || files.isEmpty()) {
            throw new NotFoundException("No files of type " + mimeType + " found in folder ID: " + folderId);

        }


        return files;
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
            case "drawing":
                return "application/vnd.google-apps.drawing";
            case "form":
                return "application/vnd.google-apps.form";
            case "folder":
                return "application/vnd.google-apps.folder";
            default:
                throw new IllegalArgumentException("Unsupported file type: " + input);
        }
    }

    @Override
    public Res uploadImageToRootDrive(String accessToken, MultipartFile multipartFile) throws GeneralSecurityException, IOException {
        Res res = new Res();

        Drive drive = documentService.createDriveService(accessToken);

        // Step 2: Save MultipartFile to a temporary file
        java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());

        try {
            multipartFile.transferTo(tempFile);

            File fileMetadata = new File();
            fileMetadata.setName(multipartFile.getOriginalFilename());
            // Not setting .setParents() → file will go to root (My Drive)

            FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            String imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

            res.setStatus(201);
            res.setMessage("Image successfully uploaded to My Drive.");
            res.setUrl(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            res.setMessage("Upload failed: " + e.getMessage());
        } finally {
            tempFile.delete(); // Clean up
        }

        return res;
    }

    @Override
    public Res uploadImageToFolderDrive(String accessToken, String folderId, MultipartFile imageFile) throws GeneralSecurityException, IOException {
        Res res = new Res();
        Drive drive = documentService.createDriveService(accessToken);


        // Prepare file metadata: set the file name and the parent folder ID
        File fileMetadata = new File();
        fileMetadata.setName(imageFile.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(folderId)); // put the file in the specified folder

        try (InputStream inputStream = imageFile.getInputStream()) {
            // Prepare media content from the MultipartFile without writing a temp file
            InputStreamContent mediaContent = new InputStreamContent(imageFile.getContentType(), inputStream);
            mediaContent.setLength(imageFile.getSize());

            // Create the file in Drive with the given metadata and content
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            String imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

            res.setStatus(201);
            res.setMessage("Image successfully uploaded to My Drive.");
            res.setUrl(imageUrl);
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            res.setStatus(500);
            res.setMessage("Upload failed: " + e.getMessage());
            throw e;
        }
        return res;
    }






}

