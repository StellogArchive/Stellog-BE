package org.example.stellog.gcs.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "GCS", description = "이미지 GCS 관련 API")
public interface GCSControllerDocs {
    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 업로드합니다."
    )
    RspTemplate<String> upload(@AuthenticatedEmail String email, @RequestParam("file") MultipartFile file);

    @Operation(
            summary = "이미지 삭제",
            description = "이미지를 삭제합니다."
    )
    RspTemplate<String> delete(@AuthenticatedEmail String email, @RequestParam("fileName") String fileName);
}
