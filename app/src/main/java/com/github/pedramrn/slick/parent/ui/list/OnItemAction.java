package com.github.pedramrn.slick.parent.ui.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-05
 */

public interface OnItemAction {
    void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View view);
}
