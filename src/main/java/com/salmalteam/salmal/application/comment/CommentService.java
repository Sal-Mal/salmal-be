package com.salmalteam.salmal.application.comment;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.comment.like.CommentLike;
import com.salmalteam.salmal.domain.comment.like.CommentLikeRepository;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.comment.CommentRepository;
import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.exception.comment.CommentException;
import com.salmalteam.salmal.exception.comment.CommentExceptionType;
import com.salmalteam.salmal.exception.comment.like.CommentLikeException;
import com.salmalteam.salmal.exception.comment.like.CommentLikeExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void save(final String content, final Vote vote, final Member member) {
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

    private void validateAuthority(final Comment comment, final Member member) {
        if (comment.getCommenter().getId() != member.getId()) {
            throw new CommentException(CommentExceptionType.FORBIDDEN_UPDATE);
        }
    }

    @Transactional(readOnly = true)
    public CommentPageResponse searchList(final Long voteId,
                                          final MemberPayLoad memberPayLoad,
                                          final CommentPageRequest commentPageRequest) {
        final Long memberId = memberPayLoad.getId();
        return commentRepository.searchList(voteId, memberId, commentPageRequest);
    }

    @Transactional
    public void likeComment(final MemberPayLoad memberPayLoad, final Long commentId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Comment comment = getCommentById(commentId);

        validateCommentAlreadyLiked(comment, member);

        final CommentLike commentLike = CommentLike.of(comment, member);

        commentLikeRepository.save(commentLike);
        commentRepository.increaseLikeCount(commentId);
    }

    private void validateCommentAlreadyLiked(final Comment comment, final Member member){
        if(commentLikeRepository.existsByCommentAndLiker(comment, member)){
            throw new CommentLikeException(CommentLikeExceptionType.DUPLICATED_LIKE);
        }
    }

    @Transactional
    public void unLikeComment(final MemberPayLoad memberPayLoad, final Long commentId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Comment comment = getCommentById(commentId);

        validateCommentNotLiked(comment, member);

        commentLikeRepository.deleteByCommentAndLiker(comment, member);
        commentRepository.decreaseLikeCount(commentId);
    }

    private void validateCommentNotLiked(final Comment comment, final Member member){
        if(!commentLikeRepository.existsByCommentAndLiker(comment, member)){
            throw new CommentLikeException(CommentLikeExceptionType.NOT_FOUND);
        }
    }


    private Comment getCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND));
    }
}
