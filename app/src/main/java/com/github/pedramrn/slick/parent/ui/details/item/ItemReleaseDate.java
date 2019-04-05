package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowReleaseDatesBinding;
import com.github.pedramrn.slick.parent.domain.model.ReleaseDate;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.databinding.BindableItem;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class ItemReleaseDate extends BindableItem<RowReleaseDatesBinding> implements OnItemAction {

    private static final String TAG = ItemReleaseDate.class.getSimpleName();

    private PrettyTime pretty = new PrettyTime(new Locale("us"));

    public final ReleaseDate releaseDate;

    public ItemReleaseDate(long id, ReleaseDate releaseDate) {
        super(id);
        this.releaseDate = releaseDate;
    }

    @Override
    public int getLayout() {
        return R.layout.row_release_dates;
    }

    @Override
    public void bind(@NonNull RowReleaseDatesBinding viewBinding, int position) {
        try {
            viewBinding.title.setText(releaseDate.type().name());
            Date date = DateUtils.toDateISO(releaseDate.date());
            viewBinding.date.setText(pretty.format(date) + "  " + DateUtils.format_MMM_dd_yyyy(date));
            ImageView buttonNotification = viewBinding.buttonNotification;
            if (releaseDate.isNotifEnable()) {
                Context context = viewBinding.getRoot().getContext();
                buttonNotification.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                buttonNotification.setImageResource(R.drawable.ic_notifications_active_black_24dp);
            } else {
                buttonNotification.setColorFilter(null);
                buttonNotification.setImageResource(R.drawable.ic_notifications_none_black_24dp);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemReleaseDate that = (ItemReleaseDate) o;

        return releaseDate.equals(that.releaseDate);
    }

    @Override
    public int hashCode() {
        return releaseDate.hashCode();
    }

    @Override
    public void action(
            @NonNull Navigator navigator,
            Retryable retryable,
            @Nullable Object payload,
            int position,
            @NonNull View view
    ) {
        Log.d(TAG, "action: called");
    }
}
