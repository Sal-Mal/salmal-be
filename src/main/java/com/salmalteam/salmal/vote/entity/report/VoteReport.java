package com.salmalteam.salmal.vote.entity.report;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "vote_report")
public class VoteReport extends BaseCreatedTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vote_id")
	private Vote vote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id")
	private Member reporter;

	private String reason;

	private VoteReport(final Vote vote, final Member member) {
		this(vote, member, "NONE");
	}

	private VoteReport(final Vote vote, final Member member, final String reason) {
		this.vote = vote;
		this.reporter = member;
		this.reason = reason;
	}

	public static VoteReport of(final Vote vote, final Member member) {
		return new VoteReport(vote, member);
	}

	public static VoteReport createByReason(final Vote vote, final Member member, final String reason) {
		return new VoteReport(vote, member, reason);
	}
}
