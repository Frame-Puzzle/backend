package com.frazzle.main.global.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;

import static com.frazzle.main.global.utils.FileUtil.convertMultiPartFileToFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class AwsService {

    @Value("${cloud.aws.s3.bucket}")
    private String name;

    private final AmazonS3 s3Client;

    // 사용자의 loginUserId를 통해 파일명을 바꾸고 S3에 업로드
    public String uploadFile(MultipartFile file, String loginUserId) {
        try {
            File fileObj = convertMultiPartFileToFile(file);

            String originalFilename = file.getOriginalFilename();

            //확장자 저장
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');

            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            //loginUserId로 이름 설정
            String uniqueFileName = loginUserId + extension;

            s3Client.putObject(new PutObjectRequest(name, uniqueFileName, fileObj));
            fileObj.delete();
            return uniqueFileName;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAILED_CONVERT_FILE);
        }
    }

    public String getProfileUrl(String userUrl) {
        URL url = s3Client.getUrl(name, userUrl);
        return "" + url;
    }
}
