package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.image.ImageFile;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluation;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteEvaluateRequest;
import com.salmalteam.salmal.exception.vote.VoteException;
import com.salmalteam.salmal.exception.vote.VoteExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final VoteEvaluationRepository voteEvaluationRepository;
    private final ImageUploader imageUploader;
    private final String voteImagePath;

    public VoteService(final MemberService memberService,
                       final VoteRepository voteRepository,
                       final VoteEvaluationRepository voteEvaluationRepository,
                       final ImageUploader imageUploader,
                       @Value("${image.path.vote}") String voteImagePath){
        this.memberService = memberService;
        this.voteRepository = voteRepository;
        this.voteEvaluationRepository = voteEvaluationRepository;
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

        voteEvaluationRepository.save(VoteEvaluation.of(vote, member, voteEvaluationType));
    }

    private Vote getVoteById(final Long voteId){
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VoteExceptionType.NOT_FOUND));
    }

    private void validateEvaluationVoteDuplicated(final Member member,final Vote vote, final VoteEvaluationType voteEvaluationType) {
        if(voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(member, vote, voteEvaluationType)){
            throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_EVALUATION);
        }
    }
}
