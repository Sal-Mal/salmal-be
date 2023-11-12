package com.salmalteam.salmal.application.member;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.comment.CommentService;
import com.salmalteam.salmal.application.vote.VoteService;
import com.salmalteam.salmal.domain.image.ImageFile;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.member.MemberRepository;
import com.salmalteam.salmal.domain.member.NickName;
import com.salmalteam.salmal.domain.member.block.MemberBlocked;
import com.salmalteam.salmal.domain.member.block.MemberBlockedRepository;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.dto.request.member.MemberImageUpdateRequest;
import com.salmalteam.salmal.dto.request.member.MyPageUpdateRequest;
import com.salmalteam.salmal.dto.request.member.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberVotePageRequest;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.dto.response.member.vote.MemberBookmarkVotePageResponse;
import com.salmalteam.salmal.dto.response.member.vote.MemberEvaluationVotePageResponse;
import com.salmalteam.salmal.dto.response.member.vote.MemberVotePageResponse;
import com.salmalteam.salmal.exception.member.MemberException;
import com.salmalteam.salmal.exception.member.MemberExceptionType;
import com.salmalteam.salmal.exception.member.block.MemberBlockedException;
import com.salmalteam.salmal.exception.member.block.MemberBlockedExceptionType;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberBlockedRepository memberBlockedRepository;
    private final ImageUploader imageUploader;
    private final String memberImagePath;
    private final VoteRepository voteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MemberService(final MemberRepository memberRepository,
                         final MemberBlockedRepository memberBlockedRepository,
                         final ImageUploader imageUploader,
                         @Value("${image.path.member}") final String memberImagePath,
                         final VoteRepository voteRepository,
                         final ApplicationEventPublisher eventPublisher) {
        this.memberRepository = memberRepository;
        this.memberBlockedRepository = memberBlockedRepository;
        this.imageUploader = imageUploader;
        this.memberImagePath = memberImagePath;
        this.voteRepository = voteRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Long findMemberIdByProviderId(final String providerId) {
        final Member member = memberRepository.findByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND));
        return member.getId();
    }

    @Transactional
    public Long save(final String provider, final SignUpRequest signUpRequest) {
        validateNickNameExists(signUpRequest.getNickName());
        final Member member = memberRepository.save(Member.of(signUpRequest.getProviderId(), signUpRequest.getNickName(),
                provider, signUpRequest.getMarketingInformationConsent()));
        return member.getId();
    }

    /**
     * TODO: S3 스토리지에 올라가있는 회원 데이터(이미지) 삭제
     */
    public void delete(final MemberPayLoad memberPayLoad, final Long memberId){

        final Member member = findMemberById(memberId);
        validateDeleteAuthority(memberId, memberPayLoad.getId());

        eventPublisher.publishEvent(MemberDeleteEvent.of(member.getId()));
        memberRepository.delete(member);
    }

    private void validateDeleteAuthority(final Long memberId, final Long requesterId){
        if(memberId != requesterId){
            throw new MemberException(MemberExceptionType.FORBIDDEN_DELETE);
        }
    }


    @Transactional(readOnly = true)
    public MyPageResponse findMyPage(final Long memberId) {
        validateExistsById(memberId);
        return memberRepository.searchMyPage(memberId);
    }

    @Transactional
    public void block(final MemberPayLoad memberPayLoad, final Long memberId) {

        final Member blocker = findMemberById(memberPayLoad.getId());
        final Member target = findMemberById(memberId);
        final MemberBlocked blockedMember = MemberBlocked.of(blocker, target);

        validateDuplicateMemberBlocked(blocker, target);

        memberBlockedRepository.save(blockedMember);
    }

    private void validateDuplicateMemberBlocked(final Member blocker, final Member target) {
        if (memberBlockedRepository.existsByBlockerAndTarget(blocker, target)) {
            throw new MemberBlockedException(MemberBlockedExceptionType.DUPLICATED_MEMBER_BLOCKED);
        }
    }


    @Transactional
    public void cancelBlocking(final MemberPayLoad memberPayLoad, final Long memberId) {

        final Member blocker = findMemberById(memberPayLoad.getId());
        final Member target = findMemberById(memberId);

        validateMemberBlockedExists(blocker, target);

        memberBlockedRepository.deleteByBlockerAndTarget(blocker, target);
    }

    private void validateMemberBlockedExists(final Member blocker, final Member target) {
        if (!memberBlockedRepository.existsByBlockerAndTarget(blocker, target)) {
            throw new MemberBlockedException(MemberBlockedExceptionType.NOT_FOUND);
        }
    }

    @Transactional
    public void updateMyPage(final MemberPayLoad memberPayLoad, final Long memberId, final MyPageUpdateRequest myPageUpdateRequest) {

        final Member member = findMemberById(memberPayLoad.getId());
        final Member targetMember = findMemberById(memberId);

        validateUpdateAuthority(member, targetMember);
        validateNickNameChangeValidity(myPageUpdateRequest.getNickName(), member.getNickName().getValue());

        member.updateMyPage(myPageUpdateRequest.getNickName(), myPageUpdateRequest.getIntroduction());
        memberRepository.save(member);
    }

    private void validateNickNameChangeValidity(final String requestNickName, final String currentNickName){
        if(!requestNickName.equals(currentNickName)){
            validateNickNameExists(requestNickName);
        }
    }

    @Transactional
    public void updateImage(final MemberPayLoad memberPayLoad, final Long memberId, final MemberImageUpdateRequest memberImageUpdateRequest){

        final Member member = findMemberById(memberPayLoad.getId());
        final Member targetMember = findMemberById(memberId);

        validateUpdateAuthority(member, targetMember);

        final ImageFile imageFile = ImageFile.of(memberImageUpdateRequest.getImageFile(), memberImagePath);
        final String imageUrl = imageUploader.uploadImage(imageFile);

        member.updateImage(imageUrl);
        memberRepository.save(member);
    }

    private void validateNickNameExists(final String nickName) {
        if (memberRepository.existsByNickName(NickName.from(nickName))) {
            throw new MemberException(MemberExceptionType.DUPLICATED_NICKNAME);
        }
    }

    private void validateUpdateAuthority(final Member requester, final Member target) {
        if (!requester.equals(target)) {
            throw new MemberException(MemberExceptionType.FORBIDDEN_UPDATE);
        }
    }

    @Transactional(readOnly = true)
    public MemberBlockedPageResponse searchBlockedMembers(final MemberPayLoad memberPayLoad, final Long memberId, final MemberBlockedPageRequest memberBlockedPageRequest){

        final Member member = findMemberById(memberPayLoad.getId());
        final Member targetMember = findMemberById(memberId);

        validateSearchAuthority(member, targetMember);

        return memberBlockedRepository.searchList(memberId, memberBlockedPageRequest);
    }

    private void validateSearchAuthority(final Member requester, final Member target) {
        if (!requester.equals(target)) {
            throw new MemberBlockedException(MemberBlockedExceptionType.FORBIDDEN_SEARCH);
        }
    }

    @Transactional(readOnly = true)
    public MemberVotePageResponse searchMemberVotes(final MemberPayLoad memberPayLoad, final Long memberId, final MemberVotePageRequest memberVotePageRequest){

        validateExistsById(memberId);

        return voteRepository.searchMemberVoteList(memberId, memberVotePageRequest);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND));
        return member;
    }

    @Transactional(readOnly = true)
    public MemberEvaluationVotePageResponse searchMemberEvaluatedVotes(final MemberPayLoad memberPayLoad, final Long memberId, final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest){

        validateExistsById(memberId);

        return voteRepository.searchMemberEvaluationVoteList(memberId, memberEvaluationVotePageRequest);
    }

    @Transactional(readOnly = true)
    public MemberBookmarkVotePageResponse searchMemberBookmarkedVotes(final MemberPayLoad memberPayLoad, final Long memberId, final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest){

        validateExistsById(memberId);

        return voteRepository.searchMemberBookmarkVoteList(memberId, memberBookmarkVotePageRequest);
    }


    private void validateExistsById(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberException(MemberExceptionType.NOT_FOUND);
        }
    }

}
