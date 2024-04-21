package ru.fildv.tasksjdbc.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.tasksjdbc.database.entity.task.TaskImage;
import ru.fildv.tasksjdbc.exception.ImageUploadException;
import ru.fildv.tasksjdbc.service.ImageService;
import ru.fildv.tasksjdbc.service.property.MinioProperties;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(final TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException(
                    "Image upload failed. " + e.getMessage());
        }
        MultipartFile file = image.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException(
                    "Image upload failed. Image must have name.");
        }
        String fileName = UUID.randomUUID() + "." + getExtension(file);
        try (InputStream inputStream = file.getInputStream()) {
            saveImage(inputStream, fileName);
            return fileName;
        } catch (Exception e) {
            throw new ImageUploadException(
                    "Image upload failed. " + e.getMessage());
        }
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream,
                           final String fileName) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .build());
    }

    private String getExtension(final MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }
}
