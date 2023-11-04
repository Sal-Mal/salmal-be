package com.salmalteam.salmal.application.vote;

import com.salmalteam.salmal.application.member.MemberDeleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteEventListener {

    private final VoteService voteService;
    @EventListener
    public void handleMemberDeleteEvent(MemberDeleteEvent memberDeleteEvent){
        Long memberId = memberDeleteEvent.getMemberId();
        log.info("이벤트 호출");
        voteService.deleteAll(memberId);
    }
}
