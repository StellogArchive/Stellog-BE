package org.example.stellog.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.example.stellog.global.gcs.exception.GCSFileNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Component
public class GCSConfig {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialJson;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(loadCredentialStream());
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    private InputStream loadCredentialStream() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(credentialJson);
        if (stream == null) {
            throw new GCSFileNotFoundException("GCP credentials 파일이 classpath에 존재하지 않습니다: " + credentialJson);
        }
        return stream;
    }
}
