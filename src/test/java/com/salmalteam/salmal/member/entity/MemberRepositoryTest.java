package com.salmalteam.salmal.member.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.salmalteam.salmal.config.DataJpaTestConfig;
import com.salmalteam.salmal.member.dto.response.MyPageV2Response;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	VoteRepository voteRepository;
	@Autowired
	MemberBlockedRepository memberBlockedRepository;

	@Test
	@DisplayName("회원 프로필 정보가 조회되어야 한다.")
	void searchMyPageV2() {
		//given
		Member searchMember = Member.createActivatedMember("provider110", "nickName4", "KAKAO", true);
		memberRepository.save(searchMember);
		Vote vote1 = Vote.of("imageUrl", searchMember);
		voteRepository.save(vote1);
		Long searchMemberId = searchMember.getId();
		Long id = vote1.getId();

		voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(id);
		voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(id);
		voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(id);

		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));

		//when
		MyPageV2Response actual = memberRepository.searchMyPageV2(1L, searchMemberId);

		//then
		assertThat(actual.getTotalVoteCount()).isEqualTo(5);
		assertThat(actual.getNickName()).isEqualTo("nickName4");
		assertThat(actual.getImageUrl()).isEqualTo("https://salmal-image.s3.ap-northeast-2.amazonaws.com/default.png");
		assertThat(actual.isBlocked()).isEqualTo(false);
		assertThat(actual.getDisLikeCount()).isEqualTo(2);
		assertThat(actual.getLikeCount()).isEqualTo(1);
	}

	@Test
	@DisplayName("회원 프로필 정보가 조회되어야 한다(차단)")
	void searchMyPageV2_blocked() {
		//given
		Member member = Member.createActivatedMember("provider110", "member", "KAKAO", true);
		Member searchMember = Member.createActivatedMember("provider110", "searchMember", "KAKAO", true);
		memberRepository.save(member);
		memberRepository.save(searchMember);
		memberBlockedRepository.save(MemberBlocked.of(member, searchMember));

		Vote vote1 = Vote.of("imageUrl", searchMember);
		voteRepository.save(vote1);

		Long memberId = member.getId();
		Long searchMemberId = searchMember.getId();
		Long voteId = vote1.getId();

		voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(voteId);
		voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(voteId);

		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));
		voteRepository.save(Vote.of("imageUrl", searchMember));

		//when
		MyPageV2Response actual = memberRepository.searchMyPageV2(memberId, searchMemberId);

		//then
		assertThat(actual.getTotalVoteCount()).isEqualTo(5);
		assertThat(actual.getNickName()).isEqualTo("searchMember");
		assertThat(actual.getImageUrl()).isEqualTo("https://salmal-image.s3.ap-northeast-2.amazonaws.com/default.png");
		assertThat(actual.isBlocked()).isEqualTo(true);
		assertThat(actual.getLikeCount()).isEqualTo(1);
		assertThat(actual.getDisLikeCount()).isEqualTo(1);
	}
}