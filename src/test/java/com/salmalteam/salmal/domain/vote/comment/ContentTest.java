package com.salmalteam.salmal.domain.vote.comment;

import com.salmalteam.salmal.domain.comment.Content;
import com.salmalteam.salmal.exception.comment.CommentException;
import com.salmalteam.salmal.exception.comment.CommentExceptionType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ContentTest {
    @ParameterizedTest
    @CsvSource(value = {"글"})
    void 댓글_내용이_없거나_100글자_이상이면_예외가_발생한다(String content){
        // when & then
        content = content.repeat(101);
        String finalContent = content;
        assertThatThrownBy(() -> Content.of(finalContent))
                .isInstanceOf(CommentException.class)
                .hasFieldOrPropertyWithValue("exceptionType", CommentExceptionType.INVALID_COMMENT_LENGTH);
    }
}