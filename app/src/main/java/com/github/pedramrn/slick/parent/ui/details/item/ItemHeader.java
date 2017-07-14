package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowHeaderBinding;
import com.github.pedramrn.slick.parent.ui.custom.OnCompleteGlide;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieCard;
import com.xwray.groupie.Item;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemHeader extends Item<RowHeaderBinding> {

    private final MovieCard movie;
    private final String transitionName;
    private RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);

    public ItemHeader(MovieCard movie, String transitionName) {
        super(0);
        this.movie = movie;
        this.transitionName = transitionName;
    }

    @Override
    public int getLayout() {
        return R.layout.row_header;
    }

    private static final String TAG = ItemHeader.class.getSimpleName();

    @Override
    public void bind(final RowHeaderBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        Log.d(TAG, "bind() called with: transitionName = [" + transitionName + "]");

        viewBinding.textViewTitle.setText(movie.title());
        // TODO: 2017-06-18 use recycler view for this
        viewBinding.textViewGenre.setText(Observable.fromIterable(movie.genres())
                .take(4)
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s + " | " + s2;
                    }
                }).blockingGet());
        viewBinding.textViewRelease.setText(movie.releaseDate());
        String voteAveTmdb = String.valueOf(movie.voteAverageTmdb());
        SpannableStringBuilder voteAveSpannedTmdb = new SpannableStringBuilder(voteAveTmdb).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewBinding.textViewScoreTmdb.setText(voteAveSpannedTmdb);
        viewBinding.textViewRuntime.setText(movie.runtimePretty());
        viewBinding.textViewCertification.setText(null);
        viewBinding.imageViewIcon.loadForSE(movie.posterThumbnail(), new OnCompleteGlide() {
            @Override
            public void onCompleteGlide() {
                viewBinding.imageViewIcon.setTransitionName(transitionName);
            }
        });

        if (movie.voteAverageTrakt() != null) {
            viewBinding.textViewScoreTrakt.setBackground(null);
            String voteAve = String.format(Locale.ENGLISH, "%.1f", movie.voteAverageTrakt());
            SpannableStringBuilder voteAveSpanned = new SpannableStringBuilder(voteAve).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewBinding.textViewScoreTrakt.setText(voteAveSpanned);
            viewBinding.textViewCertification.setText(movie.certification());
        } else {
            viewBinding.textViewCertification.setText("...");
            viewBinding.textViewScoreTrakt.setText("     ");
            viewBinding.textViewScoreTrakt.setBackgroundResource(R.drawable.line);

        }


    }
}
