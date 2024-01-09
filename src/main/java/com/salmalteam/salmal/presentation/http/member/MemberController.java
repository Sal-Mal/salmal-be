package com.salmalteam.salmal.presentation.http.member;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.dto.request.MemberImageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.MyPageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberVotePageRequest;
import com.salmalteam.salmal.member.dto.response.MyPageResponse;
import com.salmalteam.salmal.member.dto.response.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberBookmarkVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberEvaluationVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberVotePageResponse;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.auth.annotation.Login;
import com.salmalteam.salmal.auth.annotation.LoginMember;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MyPageResponse findMyPage(@PathVariable("member-id") final Long memberId) {
        return memberService.findMyPage(memberId);
    }

    @PutMapping("/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public void updateMyPage(@LoginMember MemberPayLoad memberPayLoad,
                             @PathVariable("member-id") final Long memberId,
                             @RequestBody @Valid MyPageUpdateRequest myPageUpdateRequest) {
        memberService.updateMyPage(memberPayLoad, memberId, myPageUpdateRequest);
    }

    @DeleteMapping("/{member-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void deleteMember(@LoginMember MemberPayLoad memberPayLoad,
                             @PathVariable("member-id") final Long memberId){
        memberService.delete(memberPayLoad, memberId);
    }

    @PostMapping("/{member-id}/images")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public void updateImage(@LoginMember MemberPayLoad memberPayLoad,
                            @PathVariable("member-id") final Long memberId,
                            @ModelAttribute @Valid MemberImageUpdateRequest memberImageUpdateRequest){
        memberService.updateImage(memberPayLoad, memberId, memberImageUpdateRequest);
    }

    @DeleteMapping("/{member-id}/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void deleteImage(@LoginMember MemberPayLoad memberPayLoad,
                            @PathVariable("member-id") final Long memberId){
        memberService.deleteImage(memberPayLoad, memberId);
    }

    @PostMapping("/{member-id}/blocks")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void blockMember(@LoginMember final MemberPayLoad memberPayLoad,
                            @PathVariable("member-id") final Long memberId) {
        memberService.block(memberPayLoad, memberId);
    }

    @DeleteMapping("/{member-id}/blocks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelBlocking(@LoginMember final MemberPayLoad memberPayLoad,
                               @PathVariable("member-id") final Long memberId) {
        memberService.cancelBlocking(memberPayLoad, memberId);
    }

    @GetMapping("/{member-id}/blocks")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MemberBlockedPageResponse searchBlockMembers(@LoginMember final MemberPayLoad memberPayLoad,
                                                        @PathVariable("member-id") final Long memberId,
                                                        @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                                        @RequestParam(value = "size", required = false) final Integer size){
        final MemberBlockedPageRequest memberBlockedPageRequest = MemberBlockedPageRequest.of(cursorId, size);
        return memberService.searchBlockedMembers(memberPayLoad, memberId, memberBlockedPageRequest);
    }

    @GetMapping("/{member-id}/votes")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MemberVotePageResponse searchMemberVotes(@LoginMember final MemberPayLoad memberPayLoad,
                                                    @PathVariable("member-id") final Long memberId,
                                                    @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                                    @RequestParam(value = "size", required = false) final Integer size){
        final MemberVotePageRequest memberVotePageRequest = MemberVotePageRequest.of(cursorId, size);
        return memberService.searchMemberVotes(memberPayLoad, memberId, memberVotePageRequest);
    }

    @GetMapping("/{member-id}/evaluations")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MemberEvaluationVotePageResponse searchMemberEvaluationVotes(@LoginMember final MemberPayLoad memberPayLoad,
                                                                       @PathVariable("member-id") final Long memberId,
                                                                       @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                                                       @RequestParam(value = "size", required = false) final Integer size){
        final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest = MemberEvaluationVotePageRequest.of(cursorId, size);
        return memberService.searchMemberEvaluatedVotes(memberPayLoad, memberId, memberEvaluationVotePageRequest);
    }

    @GetMapping("/{member-id}/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public MemberBookmarkVotePageResponse searchMemberBookmarkVotes(@LoginMember final MemberPayLoad memberPayLoad,
                                                                    @PathVariable("member-id") final Long memberId,
                                                                    @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                                                    @RequestParam(value = "size", required = false) final Integer size){
        final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest = MemberBookmarkVotePageRequest.of(cursorId, size);
        return memberService.searchMemberBookmarkedVotes(memberPayLoad, memberId, memberBookmarkVotePageRequest);
    }
}
