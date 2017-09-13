package com.github.pedramrn.slick.parent.ui.details.item;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowHeaderBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.text.ParseException;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemHeader extends Item<RowHeaderBinding> implements OnItemAction {

    private Controller controller;
    private final MovieBasic movie;
    private final String transitionName;
    private RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
    private Disposable disposable;

    public ItemHeader(Controller controller, MovieBasic movie, String transitionName) {
        super(0);
        this.controller = controller;
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
        // FIXME: 2017-07-15 release date maybe null
        try {
            viewBinding.textViewRelease.setText(DateUtils.format_MMM_dd_yyyy(DateUtils.toDate(movie.releaseDate())));
        } catch (ParseException | NullPointerException e) {
            viewBinding.textViewRelease.setText(movie.releaseDate());
        }
        String voteAveTmdb = String.valueOf(movie.voteAverageTmdb());
        SpannableStringBuilder voteAveSpannedTmdb =
                new SpannableStringBuilder(voteAveTmdb).append("/10", sizeSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewBinding.textViewScoreTmdb.setText(voteAveSpannedTmdb);
        viewBinding.textViewRuntime.setText(movie.runtimePretty());
        viewBinding.imageViewIcon.load(movie.thumbnailPoster());
        /*disposable = RxView.clicks(viewBinding.imageViewIcon)
                .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (movie instanceof Movie) {
                            ControllerImage.start(controller.getRouter(), ItemHeader.this.movie.title(),
                                                  ((ArrayList<String>) ((Movie) movie).images().posters())
                            );
                        }
                    }
                });*/

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

    @Override
    public void unbind(ViewHolder<RowHeaderBinding> holder) {
        super.unbind(holder);
        UtilsRx.dispose(disposable);
    }

    @Override
    public void action(Controller controller, int position) {
        //no-op
    }

    public void onDestroyView() {
        UtilsRx.dispose(disposable);
        controller = null;
    }
}
