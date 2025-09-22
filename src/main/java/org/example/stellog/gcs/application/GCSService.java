package org.example.stellog.gcs.application;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class GCSService {
    private final Storage storage;
    private final MemberRoomService memberRoomService;

    @Value("${spring.cloud.gcp.storage.bucket.name}")
    private String bucketName;

    public String uploadFile(String email, MultipartFile file) throws IOException {
        log.info("uploadFile 호출 {}", file.getOriginalFilename());
        Member member = memberRoomService.findMemberByEmail(email);
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getBytes());

        String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        log.info("GCS 업로드 완료 - email: {}, bucket: {}, fileName: {}, fileUrl: {}",
                email, bucketName, fileName, fileUrl);

        return fileUrl;
    }

    public void deleteFile(String email, String fileName) {
        Member member = memberRoomService.findMemberByEmail(email);
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);

        if (deleted) {
            log.info("GCS 파일 삭제 완료 - email: {}, bucket: {}, fileName: {}",
                    email, bucketName, fileName);
        } else {
            log.error("GCS 파일 삭제 실패 - email: {}, bucket: {}, fileName: {}",
                    email, bucketName, fileName);
            throw new RuntimeException("파일 삭제 실패: " + fileName);
        }
    }
}
