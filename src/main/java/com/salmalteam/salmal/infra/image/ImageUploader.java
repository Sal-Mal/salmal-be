package com.salmalteam.salmal.infra.image;

import com.salmalteam.salmal.domain.image.ImageFile;

public interface ImageUploader {
    String uploadImage(ImageFile imageFile);
}
