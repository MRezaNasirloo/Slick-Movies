package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.trakt.CommentTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.UserTrakt;
import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.UserDomain;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
public class MapperCommentTraktCommentDomain implements Function<CommentTrakt, CommentDomain> {

    @Inject
    public MapperCommentTraktCommentDomain() {
    }

    @Override
    public CommentDomain apply(@NonNull CommentTrakt ct) throws Exception {
        UserTrakt user = ct.user();
        UserDomain userDomain = UserDomain.builder().id(user.id()).name(user.name()).username(user.username()).build();
        return CommentDomain.create(
                ct.id(),
                ct.comment(),
                ct.spoiler(),
                ct.review(),
                ct.parentId(),
                ct.replies(),
                ct.likes(),
                ct.userRating(),
                userDomain,
                ct.createdAt(),
                ct.updatedAt()
        );
    }
}
