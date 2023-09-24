package com.salmalteam.salmal.application.comment;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.comment.CommentRepository;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.exception.comment.CommentException;
import com.salmalteam.salmal.exception.member.MemberException;
import com.salmalteam.salmal.exception.member.MemberExceptionType;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    MemberService memberService;
    @Mock
    CommentRepository commentRepository;

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
            final Member member = Member.of("LLLLLLL", "닉네임", "KAKAO", true);

            given(memberService.findMemberById(any())).willReturn(member);
            given(commentRepository.findById(any())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.updateComment(memberPayLoad, commentId, voteCommentUpdateRequest))
                    .isInstanceOf(CommentException.class);

        }
    }
}