package com.github.pedramrn.slick.parent.ui.auth.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-03
 *
 *         GoogleSignInResult is already exited.
 */

@AutoValue
public abstract class GugleSignInResult {
    public abstract boolean isSuccess();

    @Nullable
    public abstract String idToken();

    public static Builder builder() {
        return new AutoValue_GugleSignInResult.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder isSuccess(boolean isSuccess);

        public abstract Builder idToken(String idToken);

        public abstract GugleSignInResult build();
    }
}
