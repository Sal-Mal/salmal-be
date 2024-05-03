package com.salmalteam.salmal.presentation.http.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.dto.response.MyPageV2Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class MemberV2Controller {

	private final MemberService memberService;

	@GetMapping("/{search-member-id}")
	@ResponseStatus(HttpStatus.OK)
	public MyPageV2Response findMyPageV2(@LoginMember Long memberId,
		@PathVariable("search-member-id") final Long searchMemberId) {
		return memberService.findMemberProfile(memberId, searchMemberId);
	}

}
