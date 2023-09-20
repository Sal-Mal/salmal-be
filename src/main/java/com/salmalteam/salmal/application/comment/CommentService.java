package com.salmalteam.salmal.application.comment;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.comment.Comment;
import com.salmalteam.salmal.domain.vote.comment.CommentRepository;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.exception.comment.CommentException;
import com.salmalteam.salmal.exception.comment.CommentExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(final String content, final Vote vote, final Member member){
        final Comment comment = Comment.of(content, vote, member);
        commentRepository.save(comment);
    }
    @Transactional
    public void updateComment(final MemberPayLoad memberPayLoad, final Long commentId, final VoteCommentUpdateRequest voteCommentUpdateRequest) {

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Comment comment = getCommentById(commentId);
        final String content = voteCommentUpdateRequest.getContent();
        validateAuthority(comment, member);

        comment.updateContent(content);

        commentRepository.save(comment);
    }

    private Comment getCommentById(final Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND));
    }

    private void validateAuthority(final Comment comment, final Member member) {
        if (comment.getCommenter().getId() != member.getId()){
            throw new CommentException(CommentExceptionType.FORBIDDEN_UPDATE);
        }
    }
}
