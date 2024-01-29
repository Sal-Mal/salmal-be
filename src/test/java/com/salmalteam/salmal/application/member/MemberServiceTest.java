package com.salmalteam.salmal.application.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.FileInputStream;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.entity.MemberRepository;
import com.salmalteam.salmal.member.entity.MemberBlockedRepository;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.member.dto.request.MemberImageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.MyPageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;
import com.salmalteam.salmal.member.exception.block.MemberBlockedException;
import com.salmalteam.salmal.auth.entity.AuthPayload;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	MemberService memberService;
	@Mock
	MemberRepository memberRepository;
	@Mock
	MemberBlockedRepository memberBlockedRepository;

	@Nested
	class 회원_저장_테스트 {
		@Test
		void 이미_존재하는_닉네임의_경우_예외가_발생한다() {
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
	class providerId_로_회원_조회_테스트 {
		@Test
		void 회원이_존재하지_않으면_예외가_발생한다() {

			// given
			final String providerId = "providerId";
			given(memberRepository.findByProviderId(eq(providerId))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> memberService.findMemberIdByProviderId(providerId)).isInstanceOf(
				MemberException.class);
		}
	}

	@Nested
	class Id_로_회원_조회_테스트 {
		@Test
		void 회원이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long id = 1L;
			given(memberRepository.findById(eq(id))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> memberService.findMemberById(id))
				.isInstanceOf(MemberException.class);
		}
	}

	@Nested
	class 마이페이지_조회_테스트 {
		@Test
		void 마이페이지를_조회할_회원이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			given(memberRepository.existsById(eq(memberId))).willReturn(false);

			// when & then
			assertThatThrownBy(() -> memberService.findMyPage(memberId))
				.isInstanceOf(MemberException.class);
		}
	}

	@Nested
	@Disabled
	class 회원_차단_테스트 {
		@Test
		void 이미_차단한_회원이면_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long targetMemberId = 2L;
			final AuthPayload authPayload = AuthPayload.from(memberId);

			given(memberRepository.findById(eq(memberId))).willReturn(
				Optional.of(Member.createActivatedMember("kk", "닉네임 A", "kakao", true)));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(
				Optional.of(Member.createActivatedMember("ksk", "닉네임 B", "kakao", true)));
			given(memberBlockedRepository.existsByBlockerAndTarget(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> memberService.block(authPayload, targetMemberId))
				.isInstanceOf(MemberBlockedException.class);

		}

		@Test
		void 차단하려는_회원이_본인이라면_예외가_발생한다(){

			// given
			final Long memberId = 1L;
			final Long targetMemberId = 1L;
			final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);

			given(memberRepository.findById(eq(memberId))).willReturn(
					Optional.of(Member.createActivatedMember("kk", "닉네임 A", "kakao", true)));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(
					Optional.of(Member.createActivatedMember("kk", "닉네임 A", "kakao", true)));

			// when & then
			assertThatThrownBy(() -> memberService.block(memberPayLoad, targetMemberId))
					.isInstanceOf(MemberBlockedException.class);

		}
	}

	@Nested
	class 회원_차단_취소_테스트 {
		@Test
		void 차단한_적이_없는_회원이라면_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long targetMemberId = 2L;
			final AuthPayload authPayload = AuthPayload.from(memberId);

			given(memberRepository.findById(eq(memberId))).willReturn(
				Optional.of(Member.createActivatedMember("kk", "닉네임 A", "kakao", true)));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(
				Optional.of(Member.createActivatedMember("ksk", "닉네임 B", "kakao", true)));
			given(memberBlockedRepository.existsByBlockerAndTarget(any(), any())).willReturn(false);

			// when & then
			assertThatThrownBy(() -> memberService.cancelBlocking(authPayload, targetMemberId))
				.isInstanceOf(MemberBlockedException.class);
		}
	}

	@Nested
	class 회원_차단_목록_조회_테스트 {
		@Test
		void 요청자가_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long targetMemberId = 2L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final MemberBlockedPageRequest memberBlockedPageRequest = MemberBlockedPageRequest.of(1L, 3);

			// when & then
			assertThatThrownBy(
				() -> memberService.searchBlockedMembers(authPayload, targetMemberId, memberBlockedPageRequest))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 차단_목록을_조회할_회원이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long targetMemberId = 2L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final MemberBlockedPageRequest memberBlockedPageRequest = MemberBlockedPageRequest.of(1L, 3);
			final Member member = Member.createActivatedMember("LL", "닉네임", "kakao", true);

			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(member));

			// when & then
			assertThatThrownBy(
				() -> memberService.searchBlockedMembers(authPayload, targetMemberId, memberBlockedPageRequest))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 본인이_아니면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long targetMemberId = 2L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final MemberBlockedPageRequest memberBlockedPageRequest = MemberBlockedPageRequest.of(1L, 3);
			final Member memberA = Member.createActivatedMember("LL", "닉네임", "kakao", true);
			final Member memberB = Member.createActivatedMember("PP", "닉넴", "kakao", true);

			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(memberA));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(Optional.of(memberB));

			// when & then
			assertThatThrownBy(
				() -> memberService.searchBlockedMembers(authPayload, targetMemberId, memberBlockedPageRequest))
				.isInstanceOf(MemberBlockedException.class);
		}
	}

	@Nested
	class 회원_마이페이지_수정_테스트 {

		@Test
		void 회원이_존재하지_않으면_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final MyPageUpdateRequest myPageUpdateRequest = new MyPageUpdateRequest("수정할 닉네임", "수정할 한줄 소개");

			// when & then
			assertThatThrownBy(() -> memberService.updateMyPage(authPayload, targetMemberId, myPageUpdateRequest))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 수정할_회원이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final MyPageUpdateRequest myPageUpdateRequest = new MyPageUpdateRequest("수정할 닉네임", "수정할 한줄 소개");

			final Member member = Member.createActivatedMember("123", "닉네임", "kakao", true);
			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(member));

			// when & then
			assertThatThrownBy(() -> memberService.updateMyPage(authPayload, targetMemberId, myPageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.NOT_FOUND);
		}

		@Test
		void 본인이_아니면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final MyPageUpdateRequest myPageUpdateRequest = new MyPageUpdateRequest("수정할 닉네임", "수정할 한줄 소개");

			final Member memberA = Member.createActivatedMember("123", "닉네임", "kakao", true);
			final Member memberB = Member.createActivatedMember("321", "닉넴", "kakao", true);

			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(memberA));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(Optional.of(memberB));

			// when & then
			assertThatThrownBy(() -> memberService.updateMyPage(authPayload, targetMemberId, myPageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.FORBIDDEN_UPDATE);

		}

		@Test
		void 닉네임이_중복된다면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 1L;
			final MyPageUpdateRequest myPageUpdateRequest = new MyPageUpdateRequest("수정할 닉네임", "수정할 한줄 소개");

			final Member memberA = Member.createActivatedMember("123", "닉네임", "kakao", true);

			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(memberA));
			given(memberRepository.existsByNickName(any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> memberService.updateMyPage(authPayload, targetMemberId, myPageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.DUPLICATED_NICKNAME);
		}
	}

	@Nested
	class 회원_이미지_수정_테스트 {

		@Test
		void 회원이_존재하지_않으면_예외가_발생한다() throws Exception {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final String name = "imageFile";
			final String fileName = "testImage.jpg";
			final String filePath = "src/test/resources/testImages/".concat(fileName);
			final FileInputStream fileInputStream = new FileInputStream(filePath);
			final String contentType = "image/jpeg";
			final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

			final MemberImageUpdateRequest memberImageUpdateRequest = new MemberImageUpdateRequest(multipartFile);

			// when & then
			assertThatThrownBy(() -> memberService.updateImage(authPayload, targetMemberId, memberImageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.NOT_FOUND);
		}

		@Test
		void 수정할_회원이_존재하지_않으면_예외가_발생한다() throws Exception {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final String name = "imageFile";
			final String fileName = "testImage.jpg";
			final String filePath = "src/test/resources/testImages/".concat(fileName);
			final FileInputStream fileInputStream = new FileInputStream(filePath);
			final String contentType = "image/jpeg";
			final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

			final MemberImageUpdateRequest memberImageUpdateRequest = new MemberImageUpdateRequest(multipartFile);

			final Member member = Member.createActivatedMember("123", "닉네임", "kakao", true);
			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(member));

			// when & then
			assertThatThrownBy(() -> memberService.updateImage(authPayload, targetMemberId, memberImageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.NOT_FOUND);

		}

		@Test
		void 본인이_아니라면_수정할_수_없다() throws Exception {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long targetMemberId = 2L;
			final String name = "imageFile";
			final String fileName = "testImage.jpg";
			final String filePath = "src/test/resources/testImages/".concat(fileName);
			final FileInputStream fileInputStream = new FileInputStream(filePath);
			final String contentType = "image/jpeg";
			final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

			final MemberImageUpdateRequest memberImageUpdateRequest = new MemberImageUpdateRequest(multipartFile);
			final Member memberA = Member.createActivatedMember("123", "닉네임", "kakao", true);
			final Member memberB = Member.createActivatedMember("321", "닉넴", "kakao", true);
			given(memberRepository.findById(eq(memberId))).willReturn(Optional.of(memberA));
			given(memberRepository.findById(eq(targetMemberId))).willReturn(Optional.of(memberB));

			// when & then
			assertThatThrownBy(() -> memberService.updateImage(authPayload, targetMemberId, memberImageUpdateRequest))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.FORBIDDEN_UPDATE);
		}
	}

	@Nested
	class 회원_탈퇴_테스트 {
		@Test
		void 회원_탈퇴_시_활성상태가_remove_로_변경되어야한다() throws Exception {
			//given
			AuthPayload authPayload = AuthPayload.from(100L);
			Member activatedMember = Member.createActivatedMember("1111111", "tray", "Apple", true);
			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(activatedMember));

			//when
			memberService.delete(authPayload, 100L);

			//then
			then(memberRepository).should(times(1)).findById(anyLong());
		}

		@Test
		void 회원_탈퇴_시_payload_와_path_회원_아이디가_일치하지_않을_시_예외_발생() throws Exception {
			//given
			AuthPayload authPayload = AuthPayload.from(100L);
			Member activatedMember = Member.createActivatedMember("1111111", "tray", "Apple", true);
			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(activatedMember));

			//expect
			assertThatThrownBy(() -> memberService.delete(authPayload, 500L))
				.isInstanceOf(MemberException.class)
				.hasFieldOrPropertyWithValue("exceptionType", MemberExceptionType.FORBIDDEN_DELETE);
			then(memberRepository).should(times(1)).findById(anyLong());

		}
	}
}