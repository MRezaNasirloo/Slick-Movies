package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowHeaderBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-16
 */

public class ItemHeader extends Item<RowHeaderBinding> implements Consumer<Object> {

    private final WeakReference<Navigator> controller;
    private final MovieBasic movie;
    private final String transitionName;
    private final RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
    private CompositeDisposable compositeDisposable;

    private String releaseDate;
    private final String genres;
    private final SpannableStringBuilder voteAveSpannedTmdb;
    private final SpannableStringBuilder voteAveSpanned;

    public ItemHeader(Navigator navigator, MovieBasic movie, String transitionName) {
        super(1000);
        this.controller = new WeakReference<>(navigator);
        this.movie = movie;
        this.transitionName = transitionName;

        String voteAveTmdb = String.valueOf(movie.voteAverageTmdb() == null ? "" : movie.voteAverageTmdb());
        voteAveSpannedTmdb = new SpannableStringBuilder(voteAveTmdb).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Float voteAverageTrakt = movie.voteAverageTrakt();
        String voteAve = String.format(Locale.ENGLISH, "%.1f", voteAverageTrakt == null ? 0.0f : voteAverageTrakt);
        voteAveSpanned = new SpannableStringBuilder(voteAve).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        genres = Observable.fromIterable(movie.genres())
                .take(4)
                .reduce((s, s2) -> s + " | " + s2).blockingGet();
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
        Context context = viewBinding.getRoot().getContext();
        Resources resources = context.getResources();
        int size = resources.getDimensionPixelSize(R.dimen.size_logo_details);
        long before = System.currentTimeMillis();
        Log.d(TAG, "bind: called ");
        // TODO: 2017-06-18 use recycler view for this
        // viewBinding.textViewGenre.setText(genres);
        viewBinding.textViewRelease.setText(releaseDate);

        Drawable logoTmdb = ResourcesCompat.getDrawable(resources, R.drawable.ic_tmdb_logo_stacked_black, null);
        Drawable logoTrakt = ResourcesCompat.getDrawable(resources, R.drawable.ic_trakt_logo, null);
        logoTmdb.setBounds(0, 0, size, size);
        logoTrakt.setBounds(0, 0, size, size);
        viewBinding.textViewScoreTmdb.setCompoundDrawablesRelative(logoTmdb, null, null, null);
        viewBinding.textViewScoreTrakt.setCompoundDrawablesRelative(logoTrakt, null, null, null);
        // viewBinding.textViewScoreTmdb.setText(voteAveSpannedTmdb);
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
        } if (movie.voteAverageTmdb() != null) {
            viewBinding.textViewScoreTmdb.setBackground(null);
            viewBinding.textViewScoreTmdb.setText(voteAveSpannedTmdb);
        } else {
            viewBinding.textViewScoreTmdb.setText("     ");
            viewBinding.textViewScoreTmdb.setBackgroundResource(R.drawable.line);
        }
        if (movie.title() != null) {
            viewBinding.textViewTitle.setBackground(null);
            viewBinding.textViewTitle.setText(movie.title());
        } else {
            viewBinding.textViewTitle.setBackgroundResource(R.drawable.line);
            viewBinding.textViewTitle.setText("      ");
        }
        if (movie.genres() != null && !movie.genres().isEmpty()) {
            viewBinding.textViewGenre.setBackground(null);
            viewBinding.textViewGenre.setText(genres);
        } else {
            viewBinding.textViewGenre.setText("     ");
            viewBinding.textViewGenre.setBackgroundResource(R.drawable.line);

        }
        if (movie.releaseDate() != null) {
            viewBinding.textViewRelease.setBackground(null);
            viewBinding.textViewRelease.setText(releaseDate);
        } else {
            viewBinding.textViewRelease.setBackgroundResource(R.drawable.line);
            viewBinding.textViewRelease.setText("   ");
        }
        if (movie.runtimePretty() != null) {
            viewBinding.textViewRuntime.setBackground(null);
            viewBinding.textViewRuntime.setText(movie.runtimePretty());
        } else {
            viewBinding.textViewRuntime.setBackgroundResource(R.drawable.line);
            viewBinding.textViewRuntime.setText("   ");
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
    }

    @Override
    public void accept(Object o) {
        if (movie instanceof Movie && !((Movie) movie).images().posters().isEmpty()) {
            Navigator controller = this.controller.get();
            if (controller == null) { return; }
            // FIXME: 2018-04-25 start screen
            // ControllerImage.start(controller.getRouter(), ItemHeader.this.movie.title(),
            //         ((ArrayList<String>) ((Movie) movie).images().posters()));
        }
    }
}
