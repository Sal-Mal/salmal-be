package com.salmalteam.salmal.vote.entity.evaluation;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.salmalteam.salmal.config.DataJpaTestConfig;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.entity.MemberRepository;
import com.salmalteam.salmal.vote.dto.response.MemberVoteParticipantsDto;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class VoteEvaluationRepositoryTest {

	@Autowired
	VoteEvaluationCustomImpl voteEvaluationCustom;
	@Autowired
	VoteRepository voteRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	VoteEvaluationRepository voteEvaluationRepository;

	@Test
	@DisplayName("투표에 참여한 회원정보들을 조회해야한다.")
	void findVoteMemberVoteParticipants() {
		//given
		Member activatedMember = Member.createActivatedMember("provider110", "nickName4", "KAKAO", true);
		memberRepository.save(activatedMember);
		Vote vote = Vote.of("imageUrl", activatedMember);
		voteRepository.save(vote);
		voteEvaluationRepository.save(VoteEvaluation.of(vote, memberRepository.save(Member.createActivatedMember("provider111", "nickName1", "KAKAO", true)), VoteEvaluationType.LIKE));
		voteEvaluationRepository.save(VoteEvaluation.of(vote, memberRepository.save(Member.createActivatedMember("provider112", "nickName2", "KAKAO", true)), VoteEvaluationType.LIKE));
		voteEvaluationRepository.save(VoteEvaluation.of(vote, memberRepository.save(Member.createActivatedMember("provider113", "nickName3", "KAKAO", true)), VoteEvaluationType.LIKE));

		Long voteId = vote.getId();

		//when
		List<MemberVoteParticipantsDto> actual = voteEvaluationCustom.findVoteMemberVoteParticipants(voteId);

		//then
		assertThat(actual).extracting(MemberVoteParticipantsDto::getNickname)
			.containsOnly("nickName1","nickName2","nickName3");

		assertThat(actual).extracting(MemberVoteParticipantsDto::getProfileImageUrl)
			.containsOnly("https://salmal-image.s3.ap-northeast-2.amazonaws.com/default.png");


	}
}