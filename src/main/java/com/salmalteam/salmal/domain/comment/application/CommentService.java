package com.salmalteam.salmal.domain.comment.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.domain.member.application.MemberService;
import com.salmalteam.salmal.domain.comment.entity.Comment;
import com.salmalteam.salmal.domain.comment.entity.CommentRepository;
import com.salmalteam.salmal.domain.comment.entity.CommentType;
import com.salmalteam.salmal.domain.comment.entity.like.CommentLike;
import com.salmalteam.salmal.domain.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.domain.comment.entity.report.CommentReport;
import com.salmalteam.salmal.domain.comment.entity.report.CommentReportRepository;
import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.vote.entity.Vote;
import com.salmalteam.salmal.domain.vote.entity.VoteRepository;
import com.salmalteam.salmal.domain.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.domain.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.domain.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.domain.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.domain.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyResponse;
import com.salmalteam.salmal.domain.comment.exception.CommentException;
import com.salmalteam.salmal.domain.comment.exception.CommentExceptionType;
import com.salmalteam.salmal.domain.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.domain.comment.exception.like.CommentLikeExceptionType;
import com.salmalteam.salmal.domain.comment.exception.report.CommentReportException;
import com.salmalteam.salmal.domain.comment.exception.report.CommentReportExceptionType;
import com.salmalteam.salmal.domain.auth.infrastructure.dto.MemberPayLoad;

import lombok.RequiredArgsConstructor;

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
        if(!commentRepository.existsById(commentId)){
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

    /**
     * 회원 삭제 이벤트 : 댓글의 대댓글 개수 감소
     */
    @Transactional
    public void decreaseReplyCountByMemberDelete(final Long memberId) {

        // 회원이 작성한 대댓글 목록 조회
        final List<Comment> replies = commentRepository.findALlByCommenter_idAndCommentType(memberId, CommentType.REPLY);

        // parent_id 기준으로 Comment 묶기
        Map<Comment, List<Comment>> commentListMap = replies.stream()
                .collect(Collectors.groupingBy(Comment::getParentComment));

        // 대댓글 개수 감소
        commentListMap.forEach((comment, replyList) -> {
            comment.decreaseReplyCount(replyList.size());
        });

    }

//    /**
//     * 회원 삭제 이벤트 : 댓글의 좋아요 개수 감소
//     */
//    @Transactional
//    public void decreaseLikeCountByMemberDelete(final Long memberId){
//        // 회원이 작성한 좋아요 목록 조회
//        final List<CommentLike> commentLikes = commentLikeRepository.findAllByLiker_Id(memberId);
//
//        // vote 기준으로 좋아요 묶기
//        Map<Comment, List<CommentLike>> commentListMap = commentLikes.stream()
//                .collect(Collectors.groupingBy(CommentLike::getComment));
//
//        // 좋아요 개수 감소
//        commentListMap.forEach((comment, commentLikeList) -> {
//            comment.decreaseLikeCount(commentLikeList.size());
//        });
//    }

}
