package com.salmalteam.salmal.member.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("회원 프로필 정보가 조회되어야 한다.")
	void searchMyPageV2() throws Exception {
	    //given
		Member activatedMember = Member.createActivatedMember("provider110", "nickName4", "KAKAO", true);
		memberRepository.save(activatedMember);
		Vote vote1 = Vote.of("imageUrl", activatedMember);
		voteRepository.save(vote1);
		voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(vote1.getId());
		voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(vote1.getId());

		voteRepository.save(Vote.of("imageUrl", activatedMember));
		voteRepository.save(Vote.of("imageUrl", activatedMember));
		voteRepository.save(Vote.of("imageUrl", activatedMember));
		voteRepository.save(Vote.of("imageUrl", activatedMember));

		//when
		MyPageV2Response actual = memberRepository.searchMyPageV2(1L, 2L);

	    //then
		assertThat(actual.getTotalVoteCount()).isEqualTo(5);
		assertThat(actual.getNickName()).isEqualTo("nickName4");
		assertThat(actual.getImageUrl()).isEqualTo("https://salmal-image.s3.ap-northeast-2.amazonaws.com/default.png");
		assertThat(actual.getDisLikeCount()).isEqualTo(1);
		assertThat(actual.getLikeCount()).isEqualTo(1);
	}
}