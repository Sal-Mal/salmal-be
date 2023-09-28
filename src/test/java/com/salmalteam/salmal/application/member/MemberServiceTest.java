package com.salmalteam.salmal.application.member;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.member.MemberRepository;
import com.salmalteam.salmal.domain.member.block.MemberBlockedRepository;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.exception.member.MemberException;
import com.salmalteam.salmal.exception.member.block.MemberBlockedException;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberBlockedRepository memberBlockedRepository;

    @Nested
    class 회원_저장_테스트{
        @Test
        void 이미_존재하는_닉네임의_경우_예외가_발생한다(){
            // given
            final String provider = "KAKAO";
            final String providerId = "providerId";
            final String nickName = "닉네임";
            final Boolean marketingInformationConsent = true;
            final SignUpRequest signUpRequest = new SignUpRequest(providerId, nickName, marketingInformationConsent);
            given(memberRepository.existsByNickName(any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.save(provider, signUpRequest)).isInstanceOf(MemberException.class);
        }
    }

    @Nested
    class providerId_로_회원_조회_테스트{
        @Test
        void 회원이_존재하지_않으면_예외가_발생한다(){

            // given
            final String providerId = "providerId";
            given(memberRepository.findByProviderId(eq(providerId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.findMemberIdByProviderId(providerId)).isInstanceOf(MemberException.class);
        }
    }

    @Nested
    class Id_로_회원_조회_테스트{
        @Test
        void 회원이_존재하지_않으면_예외가_발생한다(){
            // given
            final Long id = 1L;
            given(memberRepository.findById(eq(id))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.findMemberById(id))
                    .isInstanceOf(MemberException.class);
        }
    }

    @Nested
    class 마이페이지_조회_테스트{
        @Test
        void 마이페이지를_조회할_회원이_존재하지_않으면_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            given(memberRepository.existsById(eq(memberId))).willReturn(false);

            // when & then
            assertThatThrownBy(() -> memberService.findMyPage(memberId))
                    .isInstanceOf(MemberException.class);
        }
    }

    @Nested
    class 회원_차단_테스트{
        @Test
        void 이미_차단한_회원이면_예외가_발생한다(){

            // given
            final Long memberId = 1L;
            final Long targetMemberId = 2L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);

            given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(Member.of("kk", "닉네임 A", "kakao", true)));
            given(memberRepository.findById(eq(targetMemberId))).willReturn(Optional.of(Member.of("ksk", "닉네임 B", "kakao", true)));
            given(memberBlockedRepository.existsByBlockerAndTarget(any(), any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.block(memberPayLoad, targetMemberId))
                    .isInstanceOf(MemberBlockedException.class);

        }
    }

    @Nested
    class 회원_차단_취소_테스트{
        @Test
        void 차단한_적이_없는_회원이라면_예외가_발생한다(){

            // given
            final Long memberId = 1L;
            final Long targetMemberId = 2L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);

            given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(Member.of("kk", "닉네임 A", "kakao", true)));
            given(memberRepository.findById(eq(targetMemberId))).willReturn(Optional.of(Member.of("ksk", "닉네임 B", "kakao", true)));
            given(memberBlockedRepository.existsByBlockerAndTarget(any(), any())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> memberService.cancelBlocking(memberPayLoad, targetMemberId))
                    .isInstanceOf(MemberBlockedException.class);
        }
    }
}