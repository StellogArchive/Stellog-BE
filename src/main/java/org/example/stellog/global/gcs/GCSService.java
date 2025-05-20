package org.example.stellog.global.gcs;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCSService {
    private final Storage storage;
    private final MemberRoomService memberRoomService;

    @Value("${spring.cloud.gcp.storage.bucket.name}")
    private String bucketName;

    public String uploadFile(String email, MultipartFile file) throws IOException {
        Member member = memberRoomService.findMemberByEmail(email);
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getBytes());

        return fileName; // 저장된 파일명 반환
    }

    public void deleteFile(String email, String fileName) {
        Member member = memberRoomService.findMemberByEmail(email);
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new RuntimeException("파일 삭제 실패: " + fileName);
        }
    }
}
