package com.salmalteam.salmal.vote.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
