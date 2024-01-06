package com.salmalteam.salmal.domain.image;

import com.salmalteam.salmal.domain.image.exception.ImageException;
import com.salmalteam.salmal.domain.image.exception.ImageExceptionType;

import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ImageFile {
    private final static Set<String> IMAGE_EXTENSIONS = new HashSet<>(Set.of("jpg","jpeg", "JPG", "JPEG", "png", "PNG", "heif", "HEIF"));
    private final static Set<String> MIME_TYPES = new HashSet<>(Set.of("image/jpg", "image/jpeg", "image/png", "image/heif"));
    private final static Tika tika = new Tika();
    private final static String DOT = ".";
    private final MultipartFile multipartFile;
    private final String originalFileName;
    private final String extension;
    private final String path;
    private ImageFile(final MultipartFile multipartFile, final String originalFileName, final String extension, final String path){
        this.multipartFile = multipartFile;
        this.originalFileName = originalFileName;
        this.extension = extension;
        this.path = path;
    }
    public static ImageFile of(final MultipartFile multipartFile, final String path){
        validateMimeType(multipartFile);

        final String originalFilename = multipartFile.getOriginalFilename();
        validateOriginalFileName(originalFilename);
        final String extension = extractExtension(originalFilename);
        validateExtension(extension);
        return new ImageFile(multipartFile, originalFilename, extension, path);
    }

    private static void validateMimeType(final MultipartFile multipartFile){
        try{
            final String mimeType = tika.detect(multipartFile.getInputStream());
            if(!MIME_TYPES.contains(mimeType)){
                throw new ImageException(ImageExceptionType.NOT_IMAGE_FILE);
            }
        }catch (IOException e){
            throw new ImageException(ImageExceptionType.IMAGE_FILE_READ_FAILED);
        }
    }

    private static void validateOriginalFileName(final String originalFileName){
        if(!StringUtils.hasText(originalFileName)){
            throw new ImageException(ImageExceptionType.NO_FILE_NAME);
        }
    }

    private static String extractExtension(final String originalFileName){
        int dotIndex = originalFileName.lastIndexOf(DOT);

        if (dotIndex == -1) {
            return "";
        }
        return originalFileName.substring(dotIndex + 1);
    }
    private static void validateExtension(final String extension){
        if(extension.isEmpty() || extension.isBlank()){
            throw new ImageException(ImageExceptionType.NO_FILE_EXTENSION);
        }
        if(!IMAGE_EXTENSIONS.contains(extension)){
            throw new ImageException(ImageExceptionType.NOT_SUPPORT_FILE_EXTENSION);
        }
    }

    public Long getSize(){
        return multipartFile.getSize();
    }

    public String getContentType(){
        return multipartFile.getContentType();
    }

    public InputStream getInputStream(){
        try{
            return multipartFile.getInputStream();
        } catch(IOException e){
            throw new ImageException(ImageExceptionType.INVALID_INPUT_STREAM);
        }
    }

    public String getFullImageName(){
        return String.format("%s/%s/%s", path, UUID.randomUUID(), originalFileName);
    }

}
