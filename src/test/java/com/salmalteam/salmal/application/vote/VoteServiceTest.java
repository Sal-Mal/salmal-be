package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.domain.vote.report.VoteReportRepository;
import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.exception.member.MemberException;
import com.salmalteam.salmal.exception.member.MemberExceptionType;
import com.salmalteam.salmal.exception.vote.VoteException;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    VoteService voteService;
    @Mock
    MemberService memberService;
    @Mock
    VoteRepository voteRepository;
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
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(multipartFile);

            // when
            voteService.register(memberPayLoad, voteCreateRequest);

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
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final String voteEvaluationTypeStr = "LIKE";
            final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluationTypeStr);
            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> voteService.evaluate(memberPayLoad, voteId, voteEvaluationType))
                    .isInstanceOf(VoteException.class);
        }

        @Test
        void 이미_동일한_타입의_투표를_이행한_적이_있다면_예외를_발생시킨다() {
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final String voteEvaluationTypeStr = "LIKE";
            final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluationTypeStr);

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", Member.of("LLLLLLL", "닉네임", "KAKAO", true))));
            given(voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(any(), any(), any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> voteService.evaluate(memberPayLoad, voteId, voteEvaluationType))
                    .isInstanceOf(VoteException.class);
        }
    }

    @Nested
    class 투표_북마킹_테스트 {

        @Test
        void 북마크_할_투표가_존재하지_않는다면_예외를_발생시킨다() {
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final Boolean isBookmarked = true;
            final VoteBookmarkRequest voteBookmarkRequest = new VoteBookmarkRequest(isBookmarked);

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> voteService.bookmark(memberPayLoad, voteId, voteBookmarkRequest))
                    .isInstanceOf(VoteException.class);
        }

    }

    @Nested
    class 투표_신고_테스트{

        @Test
        void 존재하지_않는_회원의_요청일경우_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            given(memberService.findMemberById(eq(memberId))).willThrow(new MemberException(MemberExceptionType.NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> voteService.report(memberPayLoad, voteId))
                    .isInstanceOf(MemberException.class);
        }

        @Test
        void 신고할_투표가_존재하지_않는다면_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> voteService.report(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);
        }

        @Test
        void 이미_해당_투표를_신고했을_경우_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", Member.of("LLLLLLL", "닉네임", "KAKAO", true))));
            given(voteReportRepository.existsByVoteAndReporter(any(), any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> voteService.report(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);
        }
    }

    @Nested
    class 투표_댓글_생성_테스트{

        @Test
        void 존재하지_않는_회원의_요청일경우_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final String content = "댓글입니다";
            final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);

            given(memberService.findMemberById(eq(memberId))).willThrow(new MemberException(MemberExceptionType.NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> voteService.comment(memberPayLoad, voteId, voteCommentCreateRequest))
                    .isInstanceOf(MemberException.class);
        }

        @Test
        void 댓글을_추가할_투표가_존재하지_않는다면_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final String content = "댓글입니다";
            final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.of("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> voteService.comment(memberPayLoad, voteId, voteCommentCreateRequest))
                    .isInstanceOf(VoteException.class);
        }

    }

    @Nested
    class 투표_조회_테스트{
        @Test
        void 투표가_존재하지_않으면_오류를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            given(voteRepository.existsById(eq(voteId))).willReturn(false);

            // when & then
            assertThatThrownBy(() -> voteService.search(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);
        }
    }

}