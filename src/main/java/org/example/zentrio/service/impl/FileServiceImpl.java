package org.example.zentrio.service.impl;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.zentrio.enums.ImageExtension;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.FileMetadata;
import org.example.zentrio.repository.ProfileRepository;
import org.example.zentrio.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;


    @Value("${minio.bucket.name}")
    private String bucketName;


    @SneakyThrows
    @Override
    public FileMetadata uploadFile(MultipartFile file) {
        System.out.println("file: " + file.getOriginalFilename());
        List<String> imageExtensions = new ArrayList<>();

        for (ImageExtension imageExtension : ImageExtension.values()) {
            imageExtensions.add(imageExtension.getExtension());
        }

        if (!imageExtensions.contains(StringUtils.getFilenameExtension(file.getOriginalFilename()))) {
            throw new BadRequestException("Profile image must be a valid image URL ending with .png, .svg, .jpg, .jpeg, or .gif");
        }

        boolean bucketExits = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExits) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String fileName = file.getOriginalFilename();
        fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );


        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/preview-file/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();

    }

    @SneakyThrows
    @Override
    public InputStream getFileByFileName(String fileName) {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }
}
