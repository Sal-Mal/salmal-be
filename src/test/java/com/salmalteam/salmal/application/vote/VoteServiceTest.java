package com.salmalteam.salmal.application.vote;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.salmalteam.salmal.auth.entity.AuthPayload;
import com.salmalteam.salmal.image.application.ImageUploader;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;
import com.salmalteam.salmal.presentation.http.vote.SearchTypeConstant;
import com.salmalteam.salmal.vote.application.VoteService;
import com.salmalteam.salmal.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteBookMarkRepository;
import com.salmalteam.salmal.vote.entity.VoteRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.vote.entity.report.VoteReportRepository;
import com.salmalteam.salmal.vote.exception.VoteException;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkException;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

	@InjectMocks
	VoteService voteService;
	@Mock
	MemberService memberService;
	@Mock
	VoteRepository voteRepository;

	@Mock
	VoteBookMarkRepository voteBookMarkRepository;
	@Mock
	VoteEvaluationRepository voteEvaluationRepository;
	@Mock
	VoteReportRepository voteReportRepository;
	@Mock
	ImageUploader imageUploader;

	@Nested
	class 투표_업로드_테스트 {
		@Test
		void 이미지_업로더를_통해_업로드후_요청에_해당하는_회원을_찾은_뒤_저장한다() throws IOException {
			// given
			final Long memberId = 1L;
			final String name = "imageFile";
			final String fileName = "testImage.jpg";
			final String filePath = "src/test/resources/testImages/".concat(fileName);
			final FileInputStream fileInputStream = new FileInputStream(filePath);
			final String contentType = "image/jpeg";
			final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(multipartFile);

			// when
			voteService.register(authPayload, voteCreateRequest);

			// then
			verify(imageUploader, times(1)).uploadImage(any());
			verify(memberService, times(1)).findMemberById(any());
			verify(voteRepository, times(1)).save(any());
		}
	}

	@Nested
	class 투표_평가_테스트 {

		@Test
		void 평가를_할_투표가_존재하지_않는다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			final String voteEvaluationTypeStr = "LIKE";
			final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluationTypeStr);
			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> voteService.evaluate(authPayload, voteId, voteEvaluationType))
				.isInstanceOf(VoteException.class);
		}

		@Test
		void 이미_동일한_타입의_투표를_이행한_적이_있다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			final String voteEvaluationTypeStr = "LIKE";
			final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluationTypeStr);

			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(
				Vote.of("imageUrl", Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true))));
			given(
				voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(any(), any(), any())).willReturn(
				true);

			// when & then
			assertThatThrownBy(() -> voteService.evaluate(authPayload, voteId, voteEvaluationType))
				.isInstanceOf(VoteException.class);
		}
	}

	@Nested
	class 투표_북마킹_테스트 {

		@Test
		void 북마크_할_투표가_존재하지_않는다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> voteService.bookmark(authPayload, voteId))
				.isInstanceOf(VoteException.class);
		}

		@Test
		void 이미_북마크한_투표일_경우_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			final Member member = Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true);

			given(memberService.findMemberById(eq(memberId))).willReturn(member);
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", member)));
			given(voteBookMarkRepository.existsByVoteAndBookmaker(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> voteService.bookmark(authPayload, voteId))
				.isInstanceOf(VoteBookmarkException.class);

		}

	}

	@Nested
	class 북마크_취소_테스트 {
		@Test
		void 북마크를_취소할_투표가_존재하지_않는다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			// when & then
			assertThatThrownBy(() -> voteService.cancelBookmark(authPayload, voteId))
				.isInstanceOf(VoteException.class);

			verify(memberService, times(1)).findMemberById(any());
		}
	}

	@Nested
	class 투표_신고_테스트 {

		@Test
		void 존재하지_않는_회원의_요청일경우_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			given(memberService.findMemberById(eq(memberId))).willThrow(
				new MemberException(MemberExceptionType.NOT_FOUND));

			// when & then
			assertThatThrownBy(() -> voteService.report(authPayload, voteId))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 신고할_투표가_존재하지_않는다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> voteService.report(authPayload, voteId))
				.isInstanceOf(VoteException.class);
		}

		@Test
		void 이미_해당_투표를_신고했을_경우_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(
				Vote.of("imageUrl", Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true))));
			given(voteReportRepository.existsByVoteAndReporter(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> voteService.report(authPayload, voteId))
				.isInstanceOf(VoteException.class);
		}
	}

	@Nested
	class 투표_댓글_생성_테스트 {

		@Test
		void 존재하지_않는_회원의_요청일경우_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			final String content = "댓글입니다";
			final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);

			given(memberService.findMemberById(eq(memberId))).willThrow(
				new MemberException(MemberExceptionType.NOT_FOUND));

			// when & then
			assertThatThrownBy(() -> voteService.comment(authPayload, voteId, voteCommentCreateRequest))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 댓글을_추가할_투표가_존재하지_않는다면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			final String content = "댓글입니다";
			final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);

			given(memberService.findMemberById(eq(memberId))).willReturn(
				Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
			given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> voteService.comment(authPayload, voteId, voteCommentCreateRequest))
				.isInstanceOf(VoteException.class);
		}

	}

	@Nested
	class 투표_조회_테스트 {
		@Test
		void 투표가_존재하지_않으면_오류를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			given(voteRepository.existsById(eq(voteId))).willReturn(false);

			// when & then
			assertThatThrownBy(() -> voteService.search(authPayload, voteId))
				.isInstanceOf(VoteException.class);
		}

		@Test
		@DisplayName("투표 리스트가 조회되어야 한다.")
		void searchList() {
			//given
			final Long memberId = 105L;
			final AuthPayload authPayload = AuthPayload.from(memberId);

			VotePageRequest pageRequest = new VotePageRequest(21L, 100, 8, "HOME");
			List<VoteResponse> voteResponses = new ArrayList<>();
			voteResponses.add(createVoteResponse(1L, 100L, "testUrl.com", "testNickName1", "memberImageUrl", 1, 20035,
				20000, 50));
			voteResponses.add(createVoteResponse(2L, 23123L, "testUrl.com", "testNickName2", "memberImageUrl", 2, 20035,

				20000, 50));
			voteResponses.add(
				createVoteResponse(3L, 410232140L, "testUrl.com", "testNickName3", "memberImageUrl", 6, 20035,
					20000, 50));
			voteResponses.add(
				createVoteResponse(4L, 101231250L, "testUrl.com", "testNickName4", "memberImageUrl", 5, 20035,
					20000, 50));
			voteResponses.add(
				createVoteResponse(5L, 102130L, "testUrl.com", "testNickName5", "memberImageUrl", 5, 20035,
					20000, 50));
			voteResponses.add(createVoteResponse(6L, 41200L, "testUrl.com", "testNickName6", "memberImageUrl", 4, 20035,
				20000, 50));
			voteResponses.add(createVoteResponse(7L, 2100L, "testUrl.com", "testNickName7", "memberImageUrl", 10, 20035,
				20000, 50));
			voteResponses.add(
				createVoteResponse(8L, 13215006L, "testUrl.com", "testNickName8", "memberImageUrl", 10, 20035,
					20000, 50));

			VotePageResponse votePageResponse = VotePageResponse.of(true, voteResponses);

			given(memberService.findBlockedMembers(anyLong())).willReturn(Collections.emptyList());
			given(voteRepository.searchList(memberId, pageRequest, SearchTypeConstant.HOME)).willReturn(
				votePageResponse);

			//when
			VotePageResponse actual = voteService.searchList(authPayload, pageRequest, SearchTypeConstant.HOME);


			//then
			assertThat(actual.getVotes()).hasSize(8);

			then(memberService)
				.should(times(1)).
				findBlockedMembers(anyLong());
			then(voteRepository)
				.should(times(1))
				.searchList(anyLong(), any(VotePageRequest.class), any(SearchTypeConstant.class));
		}

		@Test
		@DisplayName("투표 조회 시 차단한 회원의 투표는 조회되지 않는다.")
		void searchListExcludeBlockedMembers() throws Exception {
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);

			VotePageRequest pageRequest = new VotePageRequest(21L, 100, 8, "HOME");
			List<VoteResponse> voteResponses = new ArrayList<>();
			voteResponses.add(createVoteResponse(1L, 100L, "testUrl.com", "testNickName1", "memberImageUrl", 1, 20035,
				20000, 50));
			voteResponses.add(createVoteResponse(2L, 23123L, "testUrl.com", "testNickName2", "memberImageUrl", 2, 20035,
				20000, 50));
			voteResponses.add(
				createVoteResponse(3L, 410232140L, "testUrl.com", "testNickName3", "memberImageUrl", 6, 20035,
					20000, 50));
			voteResponses.add(
				createVoteResponse(4L, 101231250L, "testUrl.com", "testNickName4", "memberImageUrl", 5, 20035,
					20000, 50));
			voteResponses.add(
				createVoteResponse(5L, 102130L, "testUrl.com", "testNickName5", "memberImageUrl", 5, 20035,
					20000, 50));
			voteResponses.add(createVoteResponse(6L, 7L, "testUrl.com", "testNickName6", "memberImageUrl", 4, 20035,
				20000, 50));
			voteResponses.add(createVoteResponse(7L, 7L, "testUrl.com", "testNickName7", "memberImageUrl", 10, 20035,
				20000, 50));
			voteResponses.add(
				createVoteResponse(8L, 13215006L, "testUrl.com", "testNickName8", "memberImageUrl", 10, 20035,
					20000, 50));

			VotePageResponse votePageResponse = VotePageResponse.of(true, voteResponses);

			ArrayList<Long> ids = new ArrayList<>();
			ids.add(101231250L);
			ids.add(7L);

			given(memberService.findBlockedMembers(anyLong()))
				.willReturn(ids);
			given(voteRepository.searchList(memberId, pageRequest, SearchTypeConstant.HOME))
				.willReturn(votePageResponse);

			//when
			VotePageResponse actual = voteService.searchList(authPayload, pageRequest, SearchTypeConstant.HOME);

			//then
			assertThat(actual.getVotes()).hasSize(5);

			then(memberService)
				.should(times(1)).
				findBlockedMembers(anyLong());
			then(voteRepository)
				.should(times(1))
				.searchList(anyLong(), any(VotePageRequest.class), any(SearchTypeConstant.class));

		}

		private VoteResponse createVoteResponse(long id, long memberId, String imageUrl, String testNickName,
			String memberIamgeUrl, int commentCount, int likeCount, int disLikeCount, int totalEvaluationCnt) {
			return new VoteResponse(id, memberId, imageUrl, testNickName, memberIamgeUrl, commentCount, likeCount,
				disLikeCount, totalEvaluationCnt, BigDecimal.valueOf(50), BigDecimal.valueOf(50), LocalDateTime.now(),
				false, "NONE");
		}
	}

	@Nested
	class 투표_댓글_목록_조회_테스트 {
		@Test
		void 댓글_목록을_조회할_투표가_존재하지_않으면_예외를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;
			given(voteRepository.existsById(any())).willReturn(false);

			// when & then
			assertThatThrownBy(() -> voteService.search(authPayload, voteId))
				.isInstanceOf(VoteException.class);
		}
	}

	@Nested
	class 투표_평가_취소_테스트 {
		@Test
		void 투표가_존재하지_않으면_오류를_발생시킨다() {
			// given
			final Long memberId = 1L;
			final AuthPayload authPayload = AuthPayload.from(memberId);
			final Long voteId = 1L;

			// when & then
			assertThatThrownBy(() -> voteService.cancelEvaluation(authPayload, voteId))
				.isInstanceOf(VoteException.class);

			verify(memberService, times(1)).findMemberById(any());
		}
	}

}