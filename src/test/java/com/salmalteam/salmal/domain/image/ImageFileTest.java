package com.salmalteam.salmal.domain.image;

import com.salmalteam.salmal.image.entity.ImageFile;
import com.salmalteam.salmal.image.exception.ImageException;
import com.salmalteam.salmal.image.exception.ImageExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ImageFileTest {

    @ParameterizedTest
    @CsvSource(value = {"testImage.gif", "testImage.bmp"})
    void 지원하는_형식이_아니라면_예외를_발생시킨다(final String fileName) throws IOException {
        // given
        final String path = "vote";
        final String name = "image";
        final String filePath = "src/test/resources/testImages/"+fileName;
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        final String contentType = "image/gif";
        final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

        // when & then
        assertThatThrownBy(() -> ImageFile.of(multipartFile, path))
                .isInstanceOf(ImageException.class)
                .hasFieldOrPropertyWithValue("exceptionType", ImageExceptionType.NOT_SUPPORT_FILE_EXTENSION);
    }

    @ParameterizedTest
    @CsvSource(value = {"testImage.jpg", "testImage.jpeg"})
    void 지원하는_형식의_경우_문제없이_작동한다(final String fileName) throws IOException {
        // given
        final String path = "vote";
        final String name = "image";
        final String filePath = "src/test/resources/testImages/" + fileName;
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        final String contentType = "image/gif";
        final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

        ImageFile.of(multipartFile, path);
    }

        @Test
    void 확장자가_존재하지_않으면_예외를_발생시킨다() throws Exception{
        // given
        final String path = "vote";
        final String name = "image";
        final String fileName = "testImage";
        final String filePath = "src/test/resources/testImages/"+fileName;
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        final String contentType = "image/gif";
        final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

        // when & then
        assertThatThrownBy(() -> ImageFile.of(multipartFile, path))
                .isInstanceOf(ImageException.class)
                .hasFieldOrPropertyWithValue("exceptionType", ImageExceptionType.NO_FILE_EXTENSION);
    }

    @Test
    void 이미지_파일이_아니라면_예외를_발생시킨다() throws Exception{
        // given
        final String path = "vote";
        final String name = "image";
        final String fileName = "notImageFile.txt";
        final String filePath = "src/test/resources/testImages/"+fileName;
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        final String contentType = "image/gif";
        final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

        // when & then
        assertThatThrownBy(() -> ImageFile.of(multipartFile, path))
                .isInstanceOf(ImageException.class)
                .hasFieldOrPropertyWithValue("exceptionType", ImageExceptionType.NOT_IMAGE_FILE);
    }



}