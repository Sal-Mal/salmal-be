package com.salmalteam.salmal.application.member;

import lombok.Getter;

@Getter
public class MemberDeleteEvent {
    private final Long memberId;
    private MemberDeleteEvent(final Long memberId){
        this.memberId = memberId;
    }
    public static MemberDeleteEvent of(final Long memberId){
        return new MemberDeleteEvent(memberId);
    }
}
