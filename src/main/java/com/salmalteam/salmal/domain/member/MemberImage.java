package com.salmalteam.salmal.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {
    private static final String MEMBER_IMAGE_URL = "https://salmal-image.s3.ap-northeast-2.amazonaws.com/member/default.JPG";
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private MemberImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public static MemberImage of(String imageURL){
        return new MemberImage(imageURL);
    }

    public static MemberImage initMemberImage(){
        return new MemberImage(MEMBER_IMAGE_URL);
    }

    public static String getMemberImageUrl(){
        return MEMBER_IMAGE_URL;
    }

}
