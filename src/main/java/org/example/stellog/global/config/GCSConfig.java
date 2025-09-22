package org.example.stellog.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.example.stellog.gcs.exception.GCSFileNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GCSConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialPath;

    @Bean
    public Storage storage() throws IOException {
        try (InputStream stream = new FileInputStream(credentialPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            return StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new GCSFileNotFoundException(
                    "GCP credentials 파일을 찾을 수 없습니다: " + credentialPath);
        }
    }
}
