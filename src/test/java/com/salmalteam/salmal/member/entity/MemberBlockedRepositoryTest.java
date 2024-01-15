package com.salmalteam.salmal.member.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.salmalteam.salmal.config.DataJpaTestConfig;

@DataJpaTest
@Import(DataJpaTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberBlockedRepositoryTest {

	@Autowired
	MemberBlockedRepository blockedRepository;
	@Autowired
	MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		memberRepository.save(Member.createActivatedMember("test1", "testNickName1", "APPLE", true));
		memberRepository.save(Member.createActivatedMember("test2", "testNickName2", "APPLE", true));
		memberRepository.save(Member.createActivatedMember("test3", "testNickName3", "KAKAO", false));
		memberRepository.save(Member.createActivatedMember("test3", "testNickName4", "KAKAO", false));
	}

	@Test
	@DisplayName("회원 아이디를 받아 차단한 회원의 아이디 리스트를 조회해야 한다.")
	void findBlockedMembers() {
		//given
		List<Member> members = memberRepository.findAll();
		Member member1 = members.get(0);
		Member member2 = members.get(1);
		Member member3 = members.get(2);
		Member member4 = members.get(3);

		blockedRepository.save(MemberBlocked.of(member1, member2));
		blockedRepository.save(MemberBlocked.of(member1, member3));
		blockedRepository.save(MemberBlocked.of(member1, member4));
		blockedRepository.save(MemberBlocked.of(member2, member3));

		//when
		List<Long> blockedMembers = blockedRepository.findTargetMemberIdByMemberId(member1.getId());

		//then
		assertThat(blockedMembers).hasSize(3);

	}
}