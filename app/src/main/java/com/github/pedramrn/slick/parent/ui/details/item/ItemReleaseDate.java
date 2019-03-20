package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowReleaseDatesBinding;
import com.github.pedramrn.slick.parent.domain.model.ReleaseDate;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.databinding.BindableItem;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class ItemReleaseDate extends BindableItem<RowReleaseDatesBinding> {

    private PrettyTime pretty = new PrettyTime(new Locale("us"));

    private final ReleaseDate releaseDate;

    public ItemReleaseDate(long id, ReleaseDate releaseDate) {
        super(id);
        this.releaseDate = releaseDate;
    }

    @Override
    public int getLayout() {
        return R.layout.row_release_dates;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void bind(@NonNull RowReleaseDatesBinding viewBinding, int position) {
        viewBinding.buttonNotification.setOnClickListener(v -> {
            Toast.makeText(viewBinding.title.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
        });
        viewBinding.title.setText(releaseDate.type().name());
        Date date = releaseDate.date();
        Date today = new Date();
        // long days = TimeUnit.MILLISECONDS.toDays(today.getTime() - date.getTime());
        if (today.before(date)) {
            viewBinding.date.setText(pretty.format(date) + "  " + DateUtils.format_MMM_dd_yyyy(date));
        } else {
            viewBinding.date.setText(pretty.format(date) + "  " + DateUtils.format_MMM_dd_yyyy(date));
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
}
