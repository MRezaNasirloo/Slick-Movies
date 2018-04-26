package com.github.pedramrn.slick.parent.ui.videos.item;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public class ItemVideo extends Item<RowVideoBinding> implements OnItemAction {

    private final Video video;
    private Context applicationContext;

    public ItemVideo(long id, Video video) {
        super(id);
        this.video = video;
    }

    @Override
    public int getLayout() {
        return R.layout.row_video;
    }

    @Override
    public void bind(RowVideoBinding viewBinding, int position) {
        applicationContext = viewBinding.getRoot().getContext().getApplicationContext();
        viewBinding.layoutShimmer.stopShimmerAnimation();
        viewBinding.imageViewThumbnail.load(video.thumbnail());
        viewBinding.textViewName.setBackground(null);
        viewBinding.textViewChannelDateViews.setBackground(null);
        viewBinding.textViewName.setText(video.name());
        viewBinding.textViewChannelDateViews.setText(String.format("Type: %s", video.type()));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position, @NonNull View view) {
        playYoutubeVideo(applicationContext, video.key());
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
}
