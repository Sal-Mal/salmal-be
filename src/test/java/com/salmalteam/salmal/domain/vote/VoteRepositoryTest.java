package com.salmalteam.salmal.domain.vote;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import com.salmalteam.salmal.domain.comment.entity.CommentRepository;
import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.member.entity.MemberRepository;
import com.salmalteam.salmal.domain.vote.entity.Vote;
import com.salmalteam.salmal.domain.vote.entity.VoteBookMark;
import com.salmalteam.salmal.domain.vote.entity.VoteBookMarkRepository;
import com.salmalteam.salmal.domain.vote.entity.VoteRepository;
import com.salmalteam.salmal.domain.vote.entity.evaluation.VoteEvaluation;
import com.salmalteam.salmal.domain.vote.entity.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.domain.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.domain.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.domain.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.domain.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.presentation.http.vote.SearchTypeConstant;
import com.salmalteam.salmal.support.RepositoryTest;

class VoteRepositoryTest extends RepositoryTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VoteBookMarkRepository voteBookMarkRepository;

    @Autowired
    VoteEvaluationRepository voteEvaluationRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DirtiesContext
    void 투표_조회_테스트(){

        // given
        final Long voteId = 1L;
        final Long memberId = 1L;
        final Member member = Member.createActivatedMember("pro", "닉네임1", "kakao", true);
        final Vote vote = Vote.of("imageUrl", member);
        final VoteEvaluation voteEvaluation = VoteEvaluation.of(vote, member, VoteEvaluationType.LIKE);
        final VoteBookMark voteBookMark = VoteBookMark.of(member, vote);

        memberRepository.save(member);
        voteRepository.save(vote);
        voteEvaluationRepository.save(voteEvaluation);
        voteBookMarkRepository.save(voteBookMark);

        // when
        final VoteResponse voteResponse = voteRepository.search(voteId, memberId);

        // then
        Assertions.assertAll(
                () -> assertThat(voteResponse.getId()).isEqualTo(voteId),
                () -> assertThat(voteResponse.getCommentCount()).isEqualTo(0),
                () -> assertThat(voteResponse.getMemberId()).isEqualTo(memberId),
                () -> assertThat(voteResponse.getStatus()).isEqualTo("LIKE"),
                () -> assertThat(voteResponse.isBookmarked()).isEqualTo(true)
        );

    }

    @Nested
    class 평가_테스트{
        @Test
        @DirtiesContext
        void 싫어요_1_좋아요_1인_상태에서_좋아요를_추가(){
            // given
            final Long voteId = 1L;
            final Member member = Member.createActivatedMember("pro", "닉네임1", "kakao", true);
            final Vote vote = Vote.of("imageUrl", member);

            memberRepository.save(member);
            voteRepository.save(vote);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(voteId);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(voteId);

            // when
            voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(voteId);

            // then
            final Vote findVote = voteRepository.findById(voteId).orElseThrow();

            Assertions.assertAll(
                    () -> assertThat(findVote.getEvaluationCount()).isEqualTo(3),
                    () -> assertThat(findVote.getDislikeCount()).isEqualTo(1),
                    () -> assertThat(findVote.getLikeCount()).isEqualTo(2),
                    () -> assertThat(findVote.getLikeRatio()).isEqualTo(BigDecimal.valueOf(0.67)),
                    () -> assertThat(findVote.getDislikeRatio()).isEqualTo(BigDecimal.valueOf(0.33))
            );
        }
    }

    @Nested
    class 투표_목록_조회_테스트{

        @Test
        @DirtiesContext
        void 좋아요_평가_목록_조회_테스트(){
            // given
            final Long memberId = 1L;
            final Member memberA = Member.createActivatedMember("pro1", "닉네임1", "kakao", true);
            final Member memberB = Member.createActivatedMember("pro2", "닉네임2", "kakao", true);
            final Member memberC = Member.createActivatedMember("pro3", "닉네임3", "kakao", true);

            final Vote voteA = Vote.of("imageUrl", memberA);
            final Vote voteB = Vote.of("imageUrl", memberA);
            final Vote voteC = Vote.of("imageUrl", memberA);
            final Vote voteD = Vote.of("imageUrl", memberA);
            VoteEvaluation voteEvaluationA = VoteEvaluation.of(voteA, memberA, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationB = VoteEvaluation.of(voteB, memberA, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationC = VoteEvaluation.of(voteC, memberA, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationD = VoteEvaluation.of(voteD, memberA, VoteEvaluationType.LIKE);

            VoteEvaluation voteEvaluationF = VoteEvaluation.of(voteB, memberB, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationG = VoteEvaluation.of(voteC, memberB, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationH = VoteEvaluation.of(voteD, memberB, VoteEvaluationType.LIKE);

            VoteEvaluation voteEvaluationI = VoteEvaluation.of(voteA, memberC, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationJ = VoteEvaluation.of(voteB, memberC, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationK = VoteEvaluation.of(voteC, memberC, VoteEvaluationType.LIKE);
            VoteEvaluation voteEvaluationL = VoteEvaluation.of(voteD, memberC, VoteEvaluationType.LIKE);

            final VotePageRequest votePageRequest = new VotePageRequest(3L, 3, 3, "BEST");

            memberRepository.save(memberA);
            memberRepository.save(memberB);
            memberRepository.save(memberC);
            voteRepository.save(voteA);
            voteRepository.save(voteB);
            voteRepository.save(voteC);
            voteRepository.save(voteD);
            voteEvaluationRepository.save(voteEvaluationA);
            voteEvaluationRepository.save(voteEvaluationB);
            voteEvaluationRepository.save(voteEvaluationC);
            voteEvaluationRepository.save(voteEvaluationD);
            voteEvaluationRepository.save(voteEvaluationF);
            voteEvaluationRepository.save(voteEvaluationG);
            voteEvaluationRepository.save(voteEvaluationH);
            voteEvaluationRepository.save(voteEvaluationI);
            voteEvaluationRepository.save(voteEvaluationJ);
            voteEvaluationRepository.save(voteEvaluationK);
            voteEvaluationRepository.save(voteEvaluationL);

            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(1L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(1L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(2L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(2L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(2L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(3L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(3L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(3L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(4L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(4L);
            voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(4L);

            // when
            final VotePageResponse votePageResponse = voteRepository.searchList(memberId, votePageRequest, SearchTypeConstant.BEST);

            // then
            List<VoteResponse> votes = votePageResponse.getVotes();

            Assertions.assertAll(
                    () -> assertThat(votes.size()).isEqualTo(2),
                    () -> assertThat(votes.get(0).getLikeCount()).isEqualTo(3),
                    () -> assertThat(votes.get(1).getLikeCount()).isEqualTo(2)
            );
        }
    }
}