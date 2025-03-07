package com.agarang.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.agarang.global.s3<br>
 * fileName       : S3Uploader.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  S3 Service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22         Fiat_lux            최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * AWS S3에 이미지를 업로드하고, URL을 반환합니다.
     *
     * <p>이 메서드는 업로드된 이미지에 "temp=true" 태그를 추가하여
     * 임시 파일로 관리합니다.</p>
     *
     * @param multipartFile 업로드할 이미지 파일
     * @param dirName 저장할 디렉토리 이름
     * @return 업로드된 이미지의 URL
     * @throws ResponseStatusException 파일 업로드 실패 시 예외 발생
     */
    public String upload(MultipartFile multipartFile, String dirName) {

        String fileName = dirName + "/" + createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("temp", "true"));


        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucket, fileName, inputStream, metadata).withTagging(new ObjectTagging(tags));
            amazonS3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * AWS S3에 영구적으로 저장할 이미지를 업로드합니다.
     *
     * <p>이 메서드는 임시 태그 없이 업로드됩니다.</p>
     *
     * @param multipartFile 업로드할 이미지 파일
     * @param dirName 저장할 디렉토리 이름
     * @return 업로드된 이미지의 URL
     * @throws ResponseStatusException 파일 업로드 실패 시 예외 발생
     */
    public String uploadPermanent(MultipartFile multipartFile, String dirName) {
        String fileName = dirName + "/" + createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucket, fileName, inputStream, metadata);
            amazonS3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 기존의 임시 파일을 영구 파일로 변경하는 메서드입니다.
     *
     * <p>해당 이미지의 "temp" 태그 값을 "false"로 설정합니다.</p>
     *
     * @param imageUrl 변경할 이미지의 URL
     */
    public void markImagePermanent(String imageUrl) {
        String fileName = extractFileNameFromUrlUseBucketName(imageUrl);
        GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(bucket, fileName);
        GetObjectTaggingResult getTaggingResult = amazonS3Client.getObjectTagging(getTaggingRequest);
        List<Tag> tags = getTaggingResult.getTagSet();

        boolean tagFound = false;

        for (Tag tag : tags) {
            if (tag.getKey().equals("temp")) {
                tag.setValue("false");
                tagFound = true;
                break;
            }
        }

        if (!tagFound) {
            tags.add(new Tag("temp", "false"));
        }

        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(bucket, fileName, new ObjectTagging(tags));
        amazonS3Client.setObjectTagging(setObjectTaggingRequest);
    }

    /**
     * S3에서 이미지를 삭제합니다.
     *
     * @param fileUrl 삭제할 파일의 URL
     */
    public void deleteImage(String fileUrl) {
        String fileName = extractFileNameFromUrlUseBucketName(fileUrl);

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    /**
     * 이미지 URL에서 버킷 이름을 기준으로 파일명을 추출합니다.
     *
     * @param url 이미지의 URL
     * @return S3 내부 저장 경로 (디렉토리 포함)
     */
    private String extractFileNameFromUrlUseBucketName(String url) {
        final String splitStr = "agarang/";
        return url.substring(url.lastIndexOf(splitStr) + splitStr.length());
    }

    /**
     * 랜덤한 UUID를 활용하여 고유한 파일명을 생성합니다.
     *
     * @param fileName 원본 파일명
     * @return 고유한 파일명
     */
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    /**
     * 파일명에서 확장자를 추출합니다.
     *
     * @param fileName 원본 파일명
     * @return 확장자 (.jpg, .png 등)
     * @throws ResponseStatusException 파일 확장자가 없을 경우 예외 발생
     */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException se) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일 입니다.");
        }
    }
}
