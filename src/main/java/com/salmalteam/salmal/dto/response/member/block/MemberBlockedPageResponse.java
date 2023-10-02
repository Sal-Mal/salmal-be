package com.salmalteam.salmal.dto.response.member.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberBlockedPageResponse {

    private boolean hasNext;
    private List<MemberBlockedResponse> blockedMembers;

    private MemberBlockedPageResponse(boolean hasNext, List<MemberBlockedResponse> blockedMembers) {
        this.hasNext = hasNext;
        this.blockedMembers = blockedMembers;
    }
    public static MemberBlockedPageResponse of(boolean hasNext, List<MemberBlockedResponse> blockedMembers){
        return new MemberBlockedPageResponse(hasNext, blockedMembers);
    }

}
