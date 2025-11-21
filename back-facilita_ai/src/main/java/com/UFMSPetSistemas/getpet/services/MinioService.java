package com.UFMSPetSistemas.getpet.services;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String internalUrl;
    private final String publicUrl;

    public MinioService(
            @Value("${minio.url}") String internalUrl,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket-name}") String bucketName,
            @Value("${minio.public-url}") String publicUrl
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(internalUrl)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
        this.internalUrl = internalUrl;
        this.publicUrl = publicUrl;
    }

    // -----------------------------
    // UPLOAD
    // -----------------------------
    public void upload(MultipartFile file, String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para MinIO", e);
        }
    }

    // -----------------------------
    // GERAR URL PÚBLICA
    // -----------------------------
    public String generatePresignedUrl(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            String contentType = stat.contentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = "image/jpeg";
            }

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(3600)
                            .extraQueryParams(
                                    Map.of("response-content-type", contentType)
                            )
                            .build()
            );

            return url.replace("http://minio:9000", publicUrl);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL", e);
        }
    }

    // -----------------------------
    // DOWNLOAD
    // -----------------------------
    public byte[] getObject(String objectName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        )) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter arquivo", e);
        }
    }

    public String getContentType(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return stat.contentType();
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    public void delete(String fileUrl) {
        try {
            String objectName = extractObjectName(fileUrl);
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo no MinIO", e);
        }
    }

    // -----------------------------
    // EXTRAIR NOME DO OBJETO
    // -----------------------------
    public String extractObjectName(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath(); // /bucket/usuarios/1/perfil-uuid
            String prefix = "/" + bucketName + "/";
            if (path.startsWith(prefix)) {
                path = path.substring(prefix.length());
            }

            int qm = path.indexOf("?");
            if (qm != -1) {
                path = path.substring(0, qm);
            }

            return path;
        } catch (Exception e) {
            throw new RuntimeException("URL inválida para extrair objectName: " + url, e);
        }
    }

    public String getBucketName() {
        return bucketName;
    }
}
