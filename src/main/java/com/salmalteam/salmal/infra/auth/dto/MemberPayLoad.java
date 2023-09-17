package com.salmalteam.salmal.infra.auth.dto;

import lombok.Getter;

@Getter
public class MemberPayLoad {

    private final Long id;
    private MemberPayLoad(final Long id){
        this.id = id;
    }
    public static MemberPayLoad from(final Long id){
        return new MemberPayLoad(id);
    }
}
