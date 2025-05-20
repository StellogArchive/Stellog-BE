package org.example.stellog.global.gcs;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class GCSController {
    private final GCSService gcsService;

    @PostMapping()
    public RspTemplate<String> upload(@AuthenticatedEmail String email, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = gcsService.uploadFile(email, file);
            return new RspTemplate<>(HttpStatus.OK,
                    "이미지가 성공적으로 업로드 되었습니다.",
                    fileName);
        } catch (Exception e) {
            return new RspTemplate<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "이미지 업로드를 실패하였습니다.: " + e.getMessage(),
                    null
            );
        }
    }

    @DeleteMapping()
    public RspTemplate<String> delete(@AuthenticatedEmail String email, @RequestParam("fileName") String fileName) {
        try {
            gcsService.deleteFile(email, fileName);
            return new RspTemplate<>(HttpStatus.OK,
                    "이미지가 성공적으로 삭제되었습니다.",
                    fileName);
        } catch (Exception e) {
            return new RspTemplate<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "이미지 삭제를 실패하였습니다.: " + e.getMessage(),
                    null
            );
        }
    }
}
