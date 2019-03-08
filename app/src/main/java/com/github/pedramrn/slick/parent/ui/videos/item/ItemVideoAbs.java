package com.github.pedramrn.slick.parent.ui.videos.item;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.xwray.groupie.databinding.BindableItem;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 * Created on: 2018-07-14
 */
abstract class ItemVideoAbs<T extends ViewDataBinding> extends BindableItem<T> implements OnItemAction {
    protected final Video video;
    protected Context applicationContext;

    public ItemVideoAbs(long id, Video video) {
        super(id);
        this.video = video;
    }

    private static void playYoutubeVideo(@NonNull Context context, @NonNull String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void action(
            @NonNull Navigator navigator, Retryable retryable,
            @Nullable Object payload, int position, @NonNull View view
    ) {
        playYoutubeVideo(applicationContext, video.key());
    }
}
