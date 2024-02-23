package com.salmalteam.salmal.comment.entity;

public enum CommentType {
    COMMENT, REPLY;

    public boolean isComment() {
        return this.equals(COMMENT);
    }
}
