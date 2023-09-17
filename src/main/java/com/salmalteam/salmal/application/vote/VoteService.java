package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.image.ImageFile;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;
    private final ImageUploader imageUploader;
    private final String voteImagePath;

    public VoteService(final MemberService memberService,
                       final VoteRepository voteRepository,
                       final ImageUploader imageUploader,
                       @Value("${image.path.vote}") String voteImagePath){
        this.memberService = memberService;
        this.voteRepository = voteRepository;
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
}
