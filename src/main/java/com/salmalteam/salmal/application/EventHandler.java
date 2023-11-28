package com.salmalteam.salmal.application;

import com.salmalteam.salmal.application.comment.CommentDeleteEvent;
import com.salmalteam.salmal.application.comment.CommentService;
import com.salmalteam.salmal.application.member.MemberDeleteEvent;
import com.salmalteam.salmal.application.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandler {
    private final VoteService voteService;
    private final CommentService commentService;

    @EventListener
    public void handleMemberDeleteEvent(MemberDeleteEvent memberDeleteEvent){
        Long memberId = memberDeleteEvent.getMemberId();
        voteService.decreaseEvaluationCountByMemberDelete(memberId);
        voteService.decreaseCommentCountByMemberDelete(memberId);
    }
}
