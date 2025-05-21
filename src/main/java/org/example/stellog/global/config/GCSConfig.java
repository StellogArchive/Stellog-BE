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

    @Value("${spring.cloud.gcp.storage.credentials.path}")
    private String credentialJson;

    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccountStream = getClass().getClassLoader().getResourceAsStream(credentialJson);
        if (serviceAccountStream == null) {
            throw new GCSFileNotFoundException("GCP credentials 파일이 classpath에 존재하지 않습니다: " + credentialJson);
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
