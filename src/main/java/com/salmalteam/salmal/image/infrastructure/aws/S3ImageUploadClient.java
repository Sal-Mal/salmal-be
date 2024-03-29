package com.salmalteam.salmal.image.infrastructure.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.salmalteam.salmal.image.entity.ImageFile;
import com.salmalteam.salmal.image.application.ImageUploader;
import com.salmalteam.salmal.image.exception.ImageException;
import com.salmalteam.salmal.image.exception.ImageExceptionType;

@Component
public class S3ImageUploadClient implements ImageUploader {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final String bucketPath;

    public S3ImageUploadClient(final AmazonS3 amazonS3,
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
