package com.salmalteam.salmal.infra.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.domain.image.ImageFile;
import com.salmalteam.salmal.exception.image.ImageException;
import com.salmalteam.salmal.exception.image.ImageExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class S3Client implements ImageUploader {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final String bucketPath;

    public S3Client(final AmazonS3 amazonS3,
                    @Value("${cloud.aws.s3.bucket-name}") final String bucketName,
                    @Value("${cloud.aws.s3.bucket-path}") final String bucketPath) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.bucketPath = bucketPath;
    }

    @Override
    public String uploadImage(final ImageFile imageFile) {
        final ObjectMetadata objectMetadata = getMetaData(imageFile);
        final String fullImageName = imageFile.getFullImageName();

        try{
            amazonS3.putObject(bucketName, fullImageName, imageFile.getInputStream(), objectMetadata);
        }catch(SdkClientException e){
            throw new ImageException(ImageExceptionType.IMAGE_FILE_UPLOAD_FAILED);
        }

        return bucketPath.concat(fullImageName);
    }

    private ObjectMetadata getMetaData(final ImageFile imageFile){
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageFile.getSize());
        objectMetadata.setContentType(imageFile.getContentType());
        return objectMetadata;
    }
}
