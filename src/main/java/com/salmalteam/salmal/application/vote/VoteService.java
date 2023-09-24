package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.comment.CommentService;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.image.ImageFile;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.domain.vote.bookmark.VoteBookMark;
import com.salmalteam.salmal.domain.vote.bookmark.VoteBookMarkRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluation;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.domain.vote.report.VoteReport;
import com.salmalteam.salmal.domain.vote.report.VoteReportRepository;
import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.exception.vote.VoteException;
import com.salmalteam.salmal.exception.vote.VoteExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final VoteEvaluationRepository voteEvaluationRepository;
    private final VoteBookMarkRepository voteBookMarkRepository;
    private final VoteReportRepository voteReportRepository;
    private final CommentService commentService;
    private final ImageUploader imageUploader;
    private final String voteImagePath;

    public VoteService(final MemberService memberService,
                       final VoteRepository voteRepository,
                       final VoteEvaluationRepository voteEvaluationRepository,
                       final VoteBookMarkRepository voteBookMarkRepository,
                       final VoteReportRepository voteReportRepository,
                       final CommentService commentService,
                       final ImageUploader imageUploader,
                       @Value("${image.path.vote}") String voteImagePath){
        this.memberService = memberService;
        this.voteRepository = voteRepository;
        this.voteEvaluationRepository = voteEvaluationRepository;
        this.voteBookMarkRepository = voteBookMarkRepository;
        this.voteReportRepository = voteReportRepository;
        this.commentService = commentService;
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

    private void validateEvaluationVoteDuplicated(final Member member,final Vote vote, final VoteEvaluationType voteEvaluationType) {
        if(voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(member, vote, voteEvaluationType)){
            throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_EVALUATION);
        }
    }

    @Transactional
    public void bookmark(final MemberPayLoad memberPayLoad, final Long voteId, final VoteBookmarkRequest voteBookmarkRequest){

        final Member member = memberService.findMemberById(memberPayLoad.getId());
        final Vote vote = getVoteById(voteId);
        final Boolean isBookmarked = voteBookmarkRequest.getIsBookmarked();

        final VoteBookMark voteBookMark = voteBookMarkRepository.findByVoteAndBookmaker(vote, member)
                .orElse(VoteBookMark.of(member, vote, isBookmarked));

        voteBookMark.updateBookmark(isBookmarked);
        voteBookMarkRepository.save(voteBookMark);
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


}
