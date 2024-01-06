package com.salmalteam.salmal.application.comment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salmalteam.salmal.domain.member.application.MemberService;
import com.salmalteam.salmal.domain.comment.application.CommentService;
import com.salmalteam.salmal.domain.comment.entity.Comment;
import com.salmalteam.salmal.domain.comment.entity.CommentRepository;
import com.salmalteam.salmal.domain.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.domain.comment.entity.report.CommentReportRepository;
import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.vote.entity.Vote;
import com.salmalteam.salmal.domain.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.domain.comment.exception.CommentException;
import com.salmalteam.salmal.domain.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.domain.comment.exception.report.CommentReportException;
import com.salmalteam.salmal.domain.member.exception.MemberException;
import com.salmalteam.salmal.domain.member.exception.MemberExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    MemberService memberService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentLikeRepository commentLikeRepository;
    @Mock
    CommentReportRepository commentReportRepository;

    @Nested
    class 댓글_수정_테스트{

        @Test
        void 요청_회원이_존재하지_않는다면_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;
            final String content = "수정할 댓글입니다";
            final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);

            given(memberService.findMemberById(any())).willThrow(new MemberException(MemberExceptionType.NOT_FOUND));
            // when & then
            assertThatThrownBy(() -> commentService.updateComment(memberPayLoad, commentId, voteCommentUpdateRequest))
                    .isInstanceOf(MemberException.class);
        }
        @Test
        void 수정할_댓글이_존재하지_않는다면_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;
            final String content = "수정할 댓글입니다";
            final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);
            final Member member = Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true);

            given(memberService.findMemberById(any())).willReturn(member);
            given(commentRepository.findById(any())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.updateComment(memberPayLoad, commentId, voteCommentUpdateRequest))
                    .isInstanceOf(CommentException.class);

        }
    }

    @Nested
    class 댓글_좋아요_테스트{

        @Test
        void 좋아요할_댓글이_존재하지_않으면_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            // when & then
            assertThatThrownBy(() -> commentService.likeComment(memberPayLoad, commentId))
                    .isInstanceOf(CommentException.class);

            verify(memberService).findMemberById(eq(memberId));
        }

        @Test
        void 이미_좋아요를_한_경우_예외가_발생한다(){

            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
            final Vote vote = Vote.of("imageUrl", member);
            final Comment comment = Comment.of("댓글", vote, member);

            given(memberService.findMemberById(any())).willReturn(member);
            given(commentRepository.findById(any())).willReturn(Optional.of(comment));
            given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> commentService.likeComment(memberPayLoad, commentId))
                    .isInstanceOf(CommentLikeException.class);

        }

    }

    @Nested
    class 댓글_좋아요_취소_테스트{

        @Test
        void 좋아요_취소할_댓글이_존재하지_않으면_예외가_발생한다(){

            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            // when & then
            assertThatThrownBy(() -> commentService.unLikeComment(memberPayLoad, commentId))
                    .isInstanceOf(CommentException.class);

            verify(memberService).findMemberById(eq(memberId));
        }

        @Test
        void 좋아요를_한_적이_없을_경우_예외가_발생한다(){

            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
            final Vote vote = Vote.of("imageUrl", member);
            final Comment comment = Comment.of("댓글", vote, member);

            given(memberService.findMemberById(any())).willReturn(member);
            given(commentRepository.findById(any())).willReturn(Optional.of(comment));
            given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> commentService.unLikeComment(memberPayLoad, commentId))
                    .isInstanceOf(CommentLikeException.class);

        }
    }

    @Nested
    class 댓글_신고_테스트{

        @Test
        void 신고할_댓글이_존재하지_않을_경우_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            // when & then
            assertThatThrownBy(() -> commentService.report(memberPayLoad, commentId))
                    .isInstanceOf(CommentException.class);

            verify(memberService).findMemberById(eq(memberId));
        }

        @Test
        void 이미_신고를_한_경우_예외가_발생한다(){
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final Long commentId = 1L;

            final Member member = Member.createActivatedMember("ss", "닉네임", "kakao", true);
            final Vote vote = Vote.of("imageUrl", member);
            final Comment comment = Comment.of("내용", vote, member);
            given(memberService.findMemberById(eq(memberId))).willReturn(member);
            given(commentRepository.findById(eq(commentId))).willReturn(Optional.of(comment));
            given(commentReportRepository.existsByCommentAndReporter(any(),any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> commentService.report(memberPayLoad, commentId))
                    .isInstanceOf(CommentReportException.class);

        }


    }
}