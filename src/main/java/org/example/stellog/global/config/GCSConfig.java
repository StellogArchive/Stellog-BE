package org.example.stellog.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Component
public class GCSConfig {
    @Value("${spring.cloud.gcp.storage.credentials.path}")
    private String credentialJson;

    @Bean
    public Storage storage() throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream(credentialJson);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
