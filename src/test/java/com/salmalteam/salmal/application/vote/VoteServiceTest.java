package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.ImageUploader;
import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.VoteRepository;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    VoteService voteService;
    @Mock
    MemberService memberService;
    @Mock
    VoteRepository voteRepository;
    @Mock
    ImageUploader imageUploader;

    @Nested
    class 투표_업로드_테스트{
        @Test
        void 이미지_업로더를_통해_업로드후_요청에_해당하는_회원을_찾은_뒤_저장한다() throws IOException {
            // given
            final Long memberId = 1L;
            final String name = "imageFile";
            final String fileName = "testImage.jpg";
            final String filePath = "src/test/resources/testImages/".concat(fileName);
            final FileInputStream fileInputStream = new FileInputStream(filePath);
            final String contentType = "image/jpeg";
            final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
            final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(multipartFile);

            // when
            voteService.register(memberPayLoad, voteCreateRequest);

            // then
            verify(imageUploader, times(1)).uploadImage(any());
            verify(memberService, times(1)).findMemberById(any());
            verify(voteRepository, times(1)).save(any());
        }
    }

}