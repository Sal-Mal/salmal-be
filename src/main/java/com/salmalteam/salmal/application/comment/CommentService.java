package com.salmalteam.salmal.application.comment;

import com.salmalteam.salmal.application.EventHandler;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.comment.CommentType;
import com.salmalteam.salmal.domain.comment.like.CommentLike;
import com.salmalteam.salmal.domain.comment.like.CommentLikeRepository;
import com.salmalteam.salmal.domain.comment.report.CommentReport;
import com.salmalteam.salmal.domain.comment.report.CommentReportRepository;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.comment.CommentRepository;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.comment.CommentReplyCreateRequest;
import com.salmalteam.salmal.dto.request.comment.ReplyPageRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.dto.response.comment.CommentResponse;
import com.salmalteam.salmal.dto.response.comment.ReplyPageResponse;
import com.salmalteam.salmal.dto.response.comment.ReplyResponse;
import com.salmalteam.salmal.exception.comment.CommentException;
import com.salmalteam.salmal.exception.comment.CommentExceptionType;
import com.salmalteam.salmal.exception.comment.like.CommentLikeException;
import com.salmalteam.salmal.exception.comment.like.CommentLikeExceptionType;
import com.salmalteam.salmal.exception.comment.report.CommentReportException;
import com.salmalteam.salmal.exception.comment.report.CommentReportExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public void save(final String content, final Vote vote, final Member member) {
        final Comment comment = Comment.of(content, vote, member);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(final MemberPayLoad memberPayLoad, final Long commentId){
        final Comment comment = getCommentById(commentId);
        validateDeleteAuthority(comment.getCommenter().getId(), memberPayLoad.getId());
        
        switch (comment.getCommentType()){
            case COMMENT:
                commentRepository.deleteAllRepliesByParentCommentId(commentId);
                voteRepository.decreaseCommentCount(comment.getVote().getId());
            case REPLY:
                commentRepository.decreaseReplyCount(commentId);
        }
        commentRepository.delete(comment);
    }

    private void validateDeleteAuthority(final Long commenterId, final Long requesterId){
        if(commenterId != requesterId){
            throw new CommentException(CommentExceptionType.FORBIDDEN_DELETE);
        }
    }

    @Transactional
    public void replyComment(final MemberPayLoad memberPayLoad, final Long commentId, final CommentReplyCreateRequest commentReplyCreateRequest){

        final Member member = memberService.findMemberById(memberPayLoad.getId());

        final Comment comment = getCommentById(commentId);
        final Comment reply = Comment.ofReply(commentReplyCreateRequest.getContent(), comment, member);

        commentRepository.save(reply);
        commentRepository.increaseReplyCount(commentId);
    }

    @Transactional(readOnly = true)
    public ReplyPageResponse searchReplies(final MemberPayLoad memberPayLoad, final Long commentId, final ReplyPageRequest replyPageRequest){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        validateCommentExist(commentId);

        return commentRepository.searchReplies(commentId, member.getId(), replyPageRequest);
    }

    @Transactional(readOnly = true)
    public List<ReplyResponse> searchAllReplies(final MemberPayLoad memberPayLoad, final Long commentId){
        final Member member = memberService.findMemberById(memberPayLoad.getId());
        validateCommentExist(commentId);

        return commentRepository.searchAllReplies(commentId, member.getId());
    }

    private void validateCommentExist(final Long commentId){
        if(commentRepository.existsById(commentId)){
            throw new CommentException(CommentExceptionType.NOT_FOUND);
        }
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

    @Transactional(readOnly = true)
    public List<CommentResponse> searchAllList(final Long voteId, final MemberPayLoad memberPayLoad){
        final Long memberId = memberPayLoad.getId();
        return commentRepository.searchAllList(voteId, memberId);
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

    @Transactional
    public void report(final MemberPayLoad memberPayLoad, final Long commentId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Comment comment = getCommentById(commentId);
        final CommentReport commentReport = CommentReport.of(comment, member);

        validateCommentAlreadyReport(comment, member);

        commentReportRepository.save(commentReport);
    }

    private void validateCommentAlreadyReport(final Comment comment, final Member member) {
        if(commentReportRepository.existsByCommentAndReporter(comment, member)){
            throw new CommentReportException(CommentReportExceptionType.DUPLICATED_REPORT);
        }
    }

    private Comment getCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND));
    }

    @Transactional
    public void deleteAllCommentsByVoteId(final Long voteId) {
        final List<Comment> allByVoteId = commentRepository.findAllByVote_Id(voteId);
        final List<Long> commentIdsForDel = allByVoteId.stream().map(Comment::getId).collect(Collectors.toList());

        commentRepository.deleteAllRepliesByIdIn(commentIdsForDel);
        commentRepository.deleteAllCommentsByIdIn(commentIdsForDel);
    }

    @Transactional
    public void deleteAllCommentsByMemberId(final Long memberId){
        commentRepository.deleteAllCommentsByCommenterId(memberId, CommentType.REPLY);
        commentRepository.deleteAllCommentsByCommenterId(memberId, CommentType.COMMENT);
    }
}
