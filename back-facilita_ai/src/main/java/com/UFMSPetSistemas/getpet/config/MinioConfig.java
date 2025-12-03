package com.UFMSPetSistemas.getpet.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public CommandLineRunner initializeBucket(MinioClient minioClient) {
        return args -> {
            int tentativas = 0;
            boolean sucesso = false;

            // Loop de tentativas (substitui o healthcheck do Docker)
            while (tentativas < 5 && !sucesso) {
                try {
                    System.out.println("Verificando MinIO (Tentativa " + (tentativas + 1) + "/5)...");

                    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

                    if (!found) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                        System.out.println("Bucket '" + bucketName + "' criado com sucesso!");

                        // Política Pública
                        String policy = """
                            {
                              "Version": "2012-10-17",
                              "Statement": [
                                {
                                  "Effect": "Allow",
                                  "Principal": {"AWS": ["*"]},
                                  "Action": ["s3:GetObject"],
                                  "Resource": ["arn:aws:s3:::%s/*"]
                                }
                              ]
                            }
                            """.formatted(bucketName);

                        minioClient.setBucketPolicy(
                                SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build()
                        );
                        System.out.println("Política pública aplicada!");
                    } else {
                        System.out.println("Bucket '" + bucketName + "' já existe.");
                    }

                    sucesso = true; // Sai do loop
                } catch (Exception e) {
                    tentativas++;
                    System.err.println("MinIO ainda indisponível. Erro: " + e.getMessage());
                    if (tentativas < 5) {
                        System.out.println("Aguardando 5 segundos para tentar novamente...");
                        Thread.sleep(5000); // Espera 5 segundos
                    }
                }
            }

            if (!sucesso) {
                System.err.println("ALERTA: Não foi possível configurar o MinIO após várias tentativas.");
            }
        };
    }
}