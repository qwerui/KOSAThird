package com.kosa.tm.googleCloud;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.google.api.ResourceProto.resource;

@Service
public class GoogleCloudStorageService {

    private final Storage storage;

    public GoogleCloudStorageService() throws IOException {
        // 서비스 계정 JSON 키 파일 경로를 명시
        // 파일을 클래스패스에서 로드
        ClassPathResource resource = new ClassPathResource("custom-autumn-433823-b3-2deb9141e7bd.json");
        this.storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream()))
                .build()
                .getService();
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        String bucketName = "ellie_bucket98"; // GCS 버킷 이름


        System.out.println("GOOGLE BUCKET NAME : " + bucketName);

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getInputStream()
        );

        return blobInfo.getMediaLink(); // 파일의 URL 반환
    }
}

