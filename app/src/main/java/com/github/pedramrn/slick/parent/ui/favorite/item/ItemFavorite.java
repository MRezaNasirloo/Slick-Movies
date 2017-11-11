package com.github.pedramrn.slick.parent.ui.favorite.item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowFavoriteMovieTvBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.Item;

import java.text.ParseException;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemFavorite extends Item<RowFavoriteMovieTvBinding> implements OnItemAction {
    private static final String TAG = ItemFavorite.class.getSimpleName();

    //static members
    private static ImageSpan imageSpan;
    private static int gray;
    private static int blackish;
    private static ForegroundColorSpan colorSpanGray;
    private static ForegroundColorSpan colorSpanBlackish;
    private static AbsoluteSizeSpan sizeSpan_12;
    private static AbsoluteSizeSpan sizeSpan_14;
    private static AbsoluteSizeSpan sizeSpan_16;
    private static AbsoluteSizeSpan sizeSpan_18;
    private static AbsoluteSizeSpan sizeSpan_20;
    private static int size_12;
    private static int size_14;
    private static int size_16;
    private static int size_18;
    private static int size_20;
    private static SpannableString spannedScoreMax;


    private final MovieBasic movie;
    private final String transitionName;
    private SpannableStringBuilder info;


    public ItemFavorite(Integer uniqueId, MovieBasic movie, String transitionName) {
        super(uniqueId);
        this.movie = movie;
        this.transitionName = transitionName;
    }

    @Override
    public int getLayout() {
        return R.layout.row_favorite_movie_tv;
    }


    @Override
    public void bind(final RowFavoriteMovieTvBinding viewBinding, int position) {
        long before = System.currentTimeMillis();
        init(viewBinding.getRoot().getContext());
        viewBinding.textViewFavoriteInfo.setText(info);
        viewBinding.imageViewFavoritePoster.load(movie.thumbnailTinyPoster());
        long took = System.currentTimeMillis() - before;
        Log.e(TAG, "bind fav item at position: " + position + " took: " + took + " ms");
    }

    private void init(Context context) {
        context = context.getApplicationContext();
        if (imageSpan == null) {
            Resources resources = context.getResources();

            Drawable drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_tmdb_logo_stacked_black, null);
            int iconSizeTmdb = resources.getDimensionPixelSize(R.dimen.icon_size_tmdb);
            drawable.setBounds(0, 0, iconSizeTmdb, iconSizeTmdb);
            imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);

            gray = resources.getColor(R.color.color_gray_4);
            blackish = resources.getColor(R.color.color_blackish);

            size_12 = resources.getDimensionPixelSize(R.dimen.text_size_12sp);
            size_14 = resources.getDimensionPixelSize(R.dimen.text_size_14sp);
            size_16 = resources.getDimensionPixelSize(R.dimen.text_size_16sp);
            size_18 = resources.getDimensionPixelSize(R.dimen.text_size_18sp);
            size_20 = resources.getDimensionPixelSize(R.dimen.text_size_20sp);

            spannedScoreMax = new SpannableString("/10   ");
            spannedScoreMax.setSpan(new ForegroundColorSpan(gray), 0, spannedScoreMax.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedScoreMax.setSpan(new AbsoluteSizeSpan(size_12), 0, spannedScoreMax.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedScoreMax.setSpan(imageSpan, spannedScoreMax.length() - 1, spannedScoreMax.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (info == null) {
            String genres = Observable.fromIterable(movie.genres())
                    .take(4)
                    .reduce((s, s2) -> s + " | " + s2).blockingGet();

            String releaseDate;
            try {
                releaseDate = " (" + DateUtils.format_yyyy(DateUtils.toDate(movie.releaseDate())) + ")";
            } catch (ParseException | NullPointerException e) {
                releaseDate = null;
            }
            SpannableString spannedTitle = new SpannableString(movie.title() + releaseDate);
            SpannableString spannedGenres = new SpannableString("\n" + genres);
            SpannableString spannedScore = new SpannableString("\n\n" + String.valueOf(movie.voteAverageTmdb()));

            spannedTitle.setSpan(new ForegroundColorSpan(blackish), 0, spannedTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedTitle.setSpan(new AbsoluteSizeSpan(size_20), 0, spannedTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedGenres.setSpan(new ForegroundColorSpan(gray), 0, spannedGenres.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedGenres.setSpan(new AbsoluteSizeSpan(size_12), 0, spannedGenres.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedScore.setSpan(new ForegroundColorSpan(blackish), 0, spannedScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannedScore.setSpan(new AbsoluteSizeSpan(size_20), 0, spannedScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            info = new SpannableStringBuilder(spannedTitle)
                    .append(spannedGenres)
                    .append(spannedScore)
                    .append(spannedScoreMax)
            ;
        }
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        navigator.getRouter().pushController(RouterTransaction.with(new ControllerDetails(movie, transitionName))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler())
        );
    }
}
