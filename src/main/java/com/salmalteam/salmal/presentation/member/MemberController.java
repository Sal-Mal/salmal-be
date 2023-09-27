package com.salmalteam.salmal.presentation.member;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.presentation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/{member-id}")
    @Login
    public MyPageResponse findMyPage(@PathVariable("member-id") final Long memberId){
        return memberService.findMyPage(memberId);
    }
}
