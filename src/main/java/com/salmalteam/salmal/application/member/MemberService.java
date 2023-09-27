package com.salmalteam.salmal.application.member;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.member.MemberRepository;
import com.salmalteam.salmal.domain.member.NickName;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.exception.member.MemberException;
import com.salmalteam.salmal.exception.member.MemberExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Long findMemberIdByProviderId(final String providerId){
        final Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND));
        return member.getId();
    }

    @Transactional
    public Long save(final String provider, final SignUpRequest signUpRequest){
        validateNickNameExists(signUpRequest.getNickName());
        final Member member = memberRepository.save(Member.of(signUpRequest.getProviderId(), signUpRequest.getNickName(),
                provider, signUpRequest.getMarketingInformationConsent()));
        return member.getId();
    }

    private void validateNickNameExists(final String nickName){
        if(memberRepository.existsByNickName(NickName.from(nickName))){
            throw new MemberException(MemberExceptionType.DUPLICATED_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public Member findMemberById(final Long memberId){
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->  new MemberException(MemberExceptionType.NOT_FOUND));
        return member;
    }

    @Transactional(readOnly = true)
    public MyPageResponse findMyPage(final Long memberId){
        validateExistsById(memberId);
        return memberRepository.searchMyPage(memberId);
    }
    private void validateExistsById(final Long memberId){
        if(!memberRepository.existsById(memberId)){
            throw new MemberException(MemberExceptionType.NOT_FOUND);
        }
    }
}
