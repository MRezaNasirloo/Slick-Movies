package com.github.pedramrn.slick.parent.ui.details.item;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowHeaderBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemHeader extends Item<RowHeaderBinding> implements Consumer<Object> {

    private Controller controller;
    private final MovieBasic movie;
    private final String transitionName;
    private final RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
    private CompositeDisposable compositeDisposable;

    private String releaseDate;
    private final String genres;
    private final SpannableStringBuilder voteAveSpannedTmdb;
    private final SpannableStringBuilder voteAveSpanned;

    public ItemHeader(Controller controller, MovieBasic movie, String transitionName) {
        super(1000);
        this.controller = controller;
        this.movie = movie;
        this.transitionName = transitionName;

        String voteAveTmdb = String.valueOf(movie.voteAverageTmdb());
        voteAveSpannedTmdb = new SpannableStringBuilder(voteAveTmdb).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Float voteAverageTrakt = movie.voteAverageTrakt();
        String voteAve = String.format(Locale.ENGLISH, "%.1f", voteAverageTrakt == null ? 0.0f : voteAverageTrakt);
            voteAveSpanned = new SpannableStringBuilder(voteAve).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        genres = Observable.fromIterable(movie.genres())
                .take(4)
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2)
                            throws Exception {
                        return s + " | " + s2;
                    }
                }).blockingGet();
        try {
            releaseDate = DateUtils.format_MMM_dd_yyyy(DateUtils.toDate(movie.releaseDate()));
        } catch (ParseException | NullPointerException e) {
            releaseDate = movie.releaseDate();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.row_header;
    }

    private static final String TAG = ItemHeader.class.getSimpleName();

    @Override
    public void bind(final RowHeaderBinding viewBinding, int position) {
        long before = System.currentTimeMillis();
        Log.d(TAG, "bind: called " );
        viewBinding.textViewTitle.setText(movie.title());
        // TODO: 2017-06-18 use recycler view for this
        viewBinding.textViewGenre.setText(genres);
        viewBinding.textViewRelease.setText(releaseDate);

        viewBinding.textViewScoreTmdb.setText(voteAveSpannedTmdb);
        viewBinding.textViewRuntime.setText(movie.runtimePretty());
        viewBinding.imageViewIcon.load(movie.thumbnailPoster());
        Disposable disposable = RxView.clicks(viewBinding.imageViewIcon)
                .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(this);

        UtilsRx.add(compositeDisposable, disposable);

        if (movie.voteAverageTrakt() != null) {
            viewBinding.textViewScoreTrakt.setBackground(null);
            viewBinding.textViewScoreTrakt.setText(voteAveSpanned);
            viewBinding.textViewCertification.setText(movie.certification());
        } else {
            viewBinding.textViewCertification.setText("...");
            viewBinding.textViewScoreTrakt.setText("     ");
            viewBinding.textViewScoreTrakt.setBackgroundResource(R.drawable.line);
        }
        long took = System.currentTimeMillis() - before;
        Log.e(TAG, "bind: took " + took + " ms");
    }

    @Override
    public void unbind(ViewHolder<RowHeaderBinding> holder) {
        Log.d(TAG, "unbind: called ");
        UtilsRx.dispose(compositeDisposable);
        super.unbind(holder);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    public void onDestroyView() {
        UtilsRx.dispose(compositeDisposable);
        controller = null;
    }

    @Override
    public void accept(Object o) throws Exception {
        if (movie instanceof Movie) {
            ControllerImage.start(controller.getRouter(), ItemHeader.this.movie.title(),
                                  ((ArrayList<String>) ((Movie) movie).images().posters())
            );
        }
    }
}
