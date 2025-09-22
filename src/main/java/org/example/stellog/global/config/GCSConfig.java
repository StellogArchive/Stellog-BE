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
        GoogleCredentials credentials = GoogleCredentials.fromStream(loadCredentialStream());
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    private InputStream loadCredentialStream() {
        try {
            String path = credentialPath.replaceFirst("^file:", "");
            InputStream stream = new FileInputStream(path);
            return stream;
        } catch (IOException e) {
            throw new GCSFileNotFoundException(
                    "GCP credentials 파일이 존재하지 않습니다: " + credentialPath, e);
        }
    }
}
