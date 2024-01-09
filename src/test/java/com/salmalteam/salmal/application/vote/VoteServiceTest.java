package com.salmalteam.salmal.application.vote;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.salmalteam.salmal.image.entity.ImageUploader;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.application.VoteService;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;
import com.salmalteam.salmal.vote.entity.VoteBookMarkRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.vote.entity.report.VoteReportRepository;
import com.salmalteam.salmal.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;
import com.salmalteam.salmal.vote.exception.VoteException;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkException;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;

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
            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
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

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true))));
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

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> voteService.bookmark(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);
        }

        @Test
        void 이미_북마크한_투표일_경우_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            final Member member = Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true);

            given(memberService.findMemberById(eq(memberId))).willReturn(member);
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", member)));
            given(voteBookMarkRepository.existsByVoteAndBookmaker(any(), any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> voteService.bookmark(memberPayLoad, voteId))
                    .isInstanceOf(VoteBookmarkException.class);

        }

    }

    @Nested
    class 북마크_취소_테스트{
        @Test
        void 북마크를_취소할_투표가_존재하지_않는다면_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            // when & then
            assertThatThrownBy(() -> voteService.cancelBookmark(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);

            verify(memberService, times(1)).findMemberById(any());
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

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
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

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
            given(voteRepository.findById(eq(voteId))).willReturn(Optional.ofNullable(Vote.of("imageUrl", Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true))));
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

            given(memberService.findMemberById(eq(memberId))).willReturn(Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true));
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

    @Nested
    class 투표_댓글_목록_조회_테스트{
        @Test
        void 댓글_목록을_조회할_투표가_존재하지_않으면_예외를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;
            given(voteRepository.existsById(any())).willReturn(false);

            // when & then 
            assertThatThrownBy(() -> voteService.search(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);
        }
    }

    @Nested
    class 투표_평가_취소_테스트{
        @Test
        void 투표가_존재하지_않으면_오류를_발생시킨다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long voteId = 1L;

            // when & then
            assertThatThrownBy(() -> voteService.cancelEvaluation(memberPayLoad, voteId))
                    .isInstanceOf(VoteException.class);

            verify(memberService, times(1)).findMemberById(any());
        }
    }

}