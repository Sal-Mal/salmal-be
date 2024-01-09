package com.salmalteam.salmal.vote.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.salmalteam.salmal.image.application.ImageUploader;
import com.salmalteam.salmal.comment.application.CommentService;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.CommentType;
import com.salmalteam.salmal.image.entity.ImageFile;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;
import com.salmalteam.salmal.vote.entity.VoteBookMark;
import com.salmalteam.salmal.vote.entity.VoteBookMarkRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluation;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.vote.entity.report.VoteReport;
import com.salmalteam.salmal.vote.entity.report.VoteReportRepository;
import com.salmalteam.salmal.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.vote.exception.VoteException;
import com.salmalteam.salmal.vote.exception.VoteExceptionType;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkException;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkExceptionType;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.presentation.http.vote.SearchTypeConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final VoteEvaluationRepository voteEvaluationRepository;
    private final VoteBookMarkRepository voteBookMarkRepository;
    private final VoteReportRepository voteReportRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ImageUploader imageUploader;
    private final String voteImagePath;
    public VoteService(final MemberService memberService,
        final VoteRepository voteRepository,
        final VoteEvaluationRepository voteEvaluationRepository,
        final VoteBookMarkRepository voteBookMarkRepository,
        final VoteReportRepository voteReportRepository,
        final CommentService commentService,
        final CommentRepository commentRepository,
        final ImageUploader imageUploader,
        @Value("${image.path.vote}") String voteImagePath){
        this.memberService = memberService;
        this.voteRepository = voteRepository;
        this.voteEvaluationRepository = voteEvaluationRepository;
        this.voteBookMarkRepository = voteBookMarkRepository;
        this.voteReportRepository = voteReportRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.imageUploader = imageUploader;
        this.voteImagePath = voteImagePath;
    }

    @Transactional
    public void register(final MemberPayLoad memberPayLoad, final VoteCreateRequest voteCreateRequest){
        final MultipartFile multipartFile = voteCreateRequest.getImageFile();
        final String imageUrl = imageUploader.uploadImage(ImageFile.of(multipartFile, voteImagePath));
        final Member member = memberService.findMemberById(memberPayLoad.getId());
        voteRepository.save(Vote.of(imageUrl, member));
    }

    /**
     * TODO: 비동기로 S3 에 올라가있는 투표 이미지 삭제하기
     */
    @Transactional
    public void delete(final MemberPayLoad memberPayLoad, final Long voteId){
        final Vote vote = getVoteById(voteId);

        final Long writerId = vote.getMember().getId();
        final Long requesterId = memberPayLoad.getId();
        validateDeleteAuthority(writerId, requesterId);

        voteRepository.delete(vote);
    }
    private void validateDeleteAuthority(final Long writerId, final Long requesterId){
        if(writerId == null || writerId != requesterId){
            throw new VoteException(VoteExceptionType.FORBIDDEN_DELETE);
        }
    }

    /**
     * 회원 삭제 이벤트 : 투표의 평가 개수 변경
     */
    //    @Transactional
    //    public void decreaseEvaluationCountByMemberDelete(final Long memberId){
    //
    //        // 회원이 평가한 평가 목록 조회
    //        final List<VoteEvaluation> voteEvaluations = voteEvaluationRepository.findAllByEvaluator_Id(memberId);
    //
    //        // 회원 평가 모두 취소 후 Count 변경
    //        for(VoteEvaluation voteEvaluation : voteEvaluations){
    //            final Vote vote = voteEvaluation.getVote();
    //            final VoteEvaluationType voteEvaluationType = voteEvaluation.getVoteEvaluationType();
    //            if(voteEvaluationType.equals(VoteEvaluationType.LIKE)){
    //                voteRepository.updateVoteEvaluationsStatisticsForEvaluationLikeDelete(vote.getId());
    //            }else {
    //                voteRepository.updateVoteEvaluationsStatisticsForEvaluationDisLikeDelete(vote.getId());
    //            }
    //        }
    //
    //    }

    /**
     * 회원 삭제 이벤트 : 투표의 댓글 개수 변경
     */
    @Transactional
    public void decreaseCommentCountByMemberDelete(final Long memberId){
        // 회원이 작성한 댓글 목록 모두 조회
        final List<Comment> comments = commentRepository.findALlByCommenter_idAndCommentType(memberId, CommentType.COMMENT);

        // 투표 기준으로 매핑
        final Map<Vote, List<Comment>> voteCommentsMap = comments.stream()
            .collect(Collectors.groupingBy(Comment::getVote));

        // 투표 댓글 개수 변경
        voteCommentsMap.forEach((vote, commentList) -> {
            vote.decreaseCommentCount(commentList.size());
        });

    }

    @Transactional
    public void evaluate(final MemberPayLoad memberPayLoad, final Long voteId, final VoteEvaluationType voteEvaluationType){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);

        validateEvaluationVoteDuplicated(member, vote, voteEvaluationType);
        deleteExistsEvaluation(member, vote);

        switch (voteEvaluationType){
            case LIKE:
                voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(voteId);
                break;
            case DISLIKE:
                voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(voteId);
                break;
        }

        voteEvaluationRepository.save(VoteEvaluation.of(vote, member, voteEvaluationType));
    }

    private void validateEvaluationVoteDuplicated(final Member member,final Vote vote, final VoteEvaluationType voteEvaluationType) {
        if(voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(member, vote, voteEvaluationType)){
            throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_EVALUATION);
        }
    }

    @Transactional
    public void cancelEvaluation(final MemberPayLoad memberPayLoad, final Long voteId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);

        deleteExistsEvaluation(member, vote);
        voteEvaluationRepository.deleteByEvaluatorAndVote(member, vote);
    }

    private void deleteExistsEvaluation(final Member member, final Vote vote){

        final Optional<VoteEvaluation> evaluationOptional = voteEvaluationRepository.findByEvaluatorAndVote(member, vote);
        if(evaluationOptional.isPresent()){
            final VoteEvaluation voteEvaluation = evaluationOptional.get();
            final VoteEvaluationType voteEvaluationType = voteEvaluation.getVoteEvaluationType();
            final Long voteId = vote.getId();
            switch(voteEvaluationType){
                case LIKE:
                    voteRepository.updateVoteEvaluationsStatisticsForEvaluationLikeDelete(voteId);
                    break;
                case DISLIKE:
                    voteRepository.updateVoteEvaluationsStatisticsForEvaluationDisLikeDelete(voteId);
                    break;
            }
            voteEvaluationRepository.deleteByEvaluatorAndVote(member, vote);
        }
    }

    @Transactional
    public void bookmark(final MemberPayLoad memberPayLoad, final Long voteId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);

        validateBookmarkExist(vote, member);
        final VoteBookMark voteBookMark = voteBookMarkRepository.findByVoteAndBookmaker(vote, member)
            .orElse(VoteBookMark.of(member, vote));

        voteBookMarkRepository.save(voteBookMark);
    }

    private void validateBookmarkExist(final Vote vote, final Member member){
        if(voteBookMarkRepository.existsByVoteAndBookmaker(vote, member)){
            throw new VoteBookmarkException(VoteBookmarkExceptionType.DUPLICATED_BOOKMARK);
        }
    }

    @Transactional
    public void cancelBookmark(final MemberPayLoad memberPayLoad, final Long voteId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);

        voteBookMarkRepository.deleteByVoteAndBookmaker(vote, member);
    }

    @Transactional
    public void report(final MemberPayLoad memberPayLoad, final Long voteId){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);

        validateVoteReportDuplicated(vote, member);
        final VoteReport voteReport = VoteReport.of(vote, member);

        voteReportRepository.save(voteReport);
    }

    private void validateVoteReportDuplicated(final Vote vote, final Member member){
        if(voteReportRepository.existsByVoteAndReporter(vote, member)){
            throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_REPORT);
        }
    }

    @Transactional
    public void comment(final MemberPayLoad memberPayLoad, final Long voteId, final VoteCommentCreateRequest voteCommentCreateRequest){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);
        final String content = voteCommentCreateRequest.getContent();

        voteRepository.increaseCommentCount(voteId);
        commentService.save(content, vote, member);
    }

    @Transactional(readOnly = true)
    public CommentPageResponse searchComments(final Long voteId, final MemberPayLoad memberPayLoad, final CommentPageRequest commentPageRequest){
        validateVoteExist(voteId);
        return commentService.searchList(voteId, memberPayLoad, commentPageRequest);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> searchAllComments(final Long voteId, final MemberPayLoad memberPayLoad){
        validateVoteExist(voteId);
        return commentService.searchAllList(voteId, memberPayLoad);
    }

    @Transactional(readOnly = true)
    public VoteResponse search(final MemberPayLoad memberPayLoad, final Long voteId){

        final Long memberId = memberPayLoad.getId();
        validateVoteExist(voteId);

        return voteRepository.search(voteId, memberId);
    }

    private void validateVoteExist(final Long voteId){
        if(!voteRepository.existsById(voteId)){
            throw new VoteException(VoteExceptionType.NOT_FOUND);
        }
    }

    private Vote getVoteById(final Long voteId){
        return voteRepository.findById(voteId)
            .orElseThrow(() -> new VoteException(VoteExceptionType.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public VotePageResponse searchList(final MemberPayLoad memberPayLoad, final VotePageRequest votePageRequest, final SearchTypeConstant searchTypeConstant){

        final Long memberId = memberPayLoad.getId();
        return voteRepository.searchList(memberId, votePageRequest, searchTypeConstant);
    }
}
