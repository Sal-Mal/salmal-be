package com.salmalteam.salmal.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {

    @Column(name = "image_url")
    private String imageUrl;

    private MemberImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public static MemberImage of(String imageURL){
        return new MemberImage(imageURL);
    }

}
