package com.github.pedramrn.slick.parent.ui.details.model;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.item.ItemComment;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class Comment extends AutoBase implements ItemView {
    public abstract Integer id();

    public abstract String comment();

    public abstract Boolean spoiler();

    public abstract Boolean review();

    public abstract Integer parentId();

    public abstract Integer replies();

    public abstract Integer likes();

    @Nullable
    public abstract Integer userRating();

    public abstract User user();

    public abstract String createdAt();

    public abstract String updatedAt();

    public abstract Builder toBuilder();

    @Override
    public Item render(String tag) {
        return new ItemComment(uniqueId(), this, tag);
    }

    @Override
    public long itemId() {
        return uniqueId();
    }

    public static Comment create(Integer id, String comment, Boolean spoiler, Boolean review, Integer parentId, Integer replies, Integer likes,
                                 Integer userRating, User user, String createdAt, String updatedAt) {
        return builder()
                .id(id)
                .uniqueId(id)
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
        return new AutoValue_Comment.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {
        public abstract Builder id(Integer id);

        public abstract Builder uniqueId(Integer id);

        public abstract Builder comment(String comment);

        public abstract Builder spoiler(Boolean spoiler);

        public abstract Builder review(Boolean review);

        public abstract Builder parentId(Integer parentId);

        public abstract Builder replies(Integer replies);

        public abstract Builder likes(Integer likes);

        public abstract Builder userRating(Integer userRating);

        public abstract Builder user(User user);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder updatedAt(String updatedAt);

        public abstract Comment build();
    }
}
