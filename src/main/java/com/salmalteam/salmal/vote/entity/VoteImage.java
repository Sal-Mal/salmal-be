package com.salmalteam.salmal.vote.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteImage {

    @Column(name = "image_url")
    private String imageUrl;

    private VoteImage(final String imageUrl){
        this.imageUrl = imageUrl;
    }
    public static VoteImage of(final String imageUrl){
        return new VoteImage(imageUrl);
    }

}
