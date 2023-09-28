package com.salmalteam.salmal.presentation.member;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.presentation.Login;
import com.salmalteam.salmal.presentation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MyPageResponse findMyPage(@PathVariable("member-id") final Long memberId){
        return memberService.findMyPage(memberId);
    }

    @PostMapping("/{member-id}/blocks")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void blockMember(@LoginMember final MemberPayLoad memberPayLoad,
                            @PathVariable("member-id") final Long memberId){
        memberService.block(memberPayLoad, memberId);
    }

    @DeleteMapping("/{member-id}/blocks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelBlocking(@LoginMember final MemberPayLoad memberPayLoad,
                               @PathVariable("member-id") final Long memberId){
        memberService.cancelBlocking(memberPayLoad, memberId);
    }
}
