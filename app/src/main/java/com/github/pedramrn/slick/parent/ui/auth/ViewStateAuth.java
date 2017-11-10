package com.github.pedramrn.slick.parent.ui.auth;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-28
 */

@AutoValue
public abstract class ViewStateAuth {

    public abstract boolean loading();

    /**
     * @return the logged in user, and returns null if the user has not logged in.
     */
    @Nullable
    public abstract UserApp user();

    /**
     * @return the Google login not to be mistaken with firebase google login, it should be used to login the user in firebase
     */
    @Nullable
    public abstract GugleSignInResult googleSignInResult();

    @Nullable
    public abstract Boolean signedOut();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateAuth.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder user(UserApp user);

        public abstract Builder signedOut(Boolean signedOut);

        public abstract Builder googleSignInResult(GugleSignInResult googleSignInResult);

        public abstract Builder loading(boolean loading);

        public abstract ViewStateAuth build();
    }
}
