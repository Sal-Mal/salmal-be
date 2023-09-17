package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.application.vote.VoteService;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.presentation.Login;
import com.salmalteam.salmal.presentation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void registerVote(@LoginMember MemberPayLoad memberPayLoad, @ModelAttribute @Valid final VoteCreateRequest voteCreateRequest){
        voteService.register(memberPayLoad, voteCreateRequest);
    }

}
