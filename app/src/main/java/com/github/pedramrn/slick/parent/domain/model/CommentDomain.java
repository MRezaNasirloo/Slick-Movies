package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class CommentDomain {
    public abstract Integer id();

    public abstract String comment();

    public abstract Boolean spoiler();

    public abstract Boolean review();

    public abstract Integer parentId();

    public abstract Integer replies();

    public abstract Integer likes();

    @Nullable
    public abstract Integer userRating();

    public abstract UserDomain user();

    public abstract String createdAt();

    public abstract String updatedAt();

    public static CommentDomain create(Integer id, String comment, Boolean spoiler, Boolean review, Integer parentId, Integer replies, Integer likes,
                                       Integer userRating, UserDomain user, String createdAt, String updatedAt) {
        return builder()
                .id(id)
                .comment(comment)
                .spoiler(spoiler)
                .review(review)
                .parentId(parentId)
                .replies(replies)
                .likes(likes)
                .userRating(userRating)
                .user(user)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_CommentDomain.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder comment(String comment);

        public abstract Builder spoiler(Boolean spoiler);

        public abstract Builder review(Boolean review);

        public abstract Builder parentId(Integer parentId);

        public abstract Builder replies(Integer replies);

        public abstract Builder likes(Integer likes);

        public abstract Builder userRating(Integer userRating);

        public abstract Builder user(UserDomain user);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder updatedAt(String updatedAt);

        public abstract CommentDomain build();
    }
}
