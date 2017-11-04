package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.UserCommentDomain;
import com.github.pedramrn.slick.parent.ui.details.model.Comment;
import com.github.pedramrn.slick.parent.ui.details.model.UserComment;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
public class MapperCommentDomainComment implements Function<CommentDomain, Comment> {

    @Inject
    public MapperCommentDomainComment() {
    }

    @Override
    public Comment apply(@NonNull CommentDomain ct) throws Exception {
        UserCommentDomain userDomain = ct.user();
        UserComment user = UserComment.builder().id(userDomain.id()).name(userDomain.name()).username(userDomain.username()).build();
        return Comment.create(
                ct.id(),
                ct.comment(),
                ct.spoiler(),
                ct.review(),
                ct.parentId(),
                ct.replies(),
                ct.likes(),
                ct.userRating(),
                user,
                ct.createdAt(),
                ct.updatedAt()
        );
    }
}
