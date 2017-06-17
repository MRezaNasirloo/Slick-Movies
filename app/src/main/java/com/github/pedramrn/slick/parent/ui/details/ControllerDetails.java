package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastList;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastRow;
import com.github.pedramrn.slick.parent.ui.details.item.ItemDetailsBasic;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-28
 */

public class ControllerDetails extends Controller implements Observer<ViewStateDetails> {

    @Inject
    Provider<PresenterDetails> provider;
    @Presenter
    PresenterDetails presenter;

    private int pos;
    private MovieBoxOffice movieBoxOffice;
    private Disposable disposable;
    private ControllerDetailsBinding binding;
    private Section progressiveSection;
    private GroupAdapter adapterCasts;
    private GroupAdapter adapterMain;

    public ControllerDetails(MovieBoxOffice movieBoxOffice, int position) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movieBoxOffice)
                .putInt("POS", position).build());
    }

    public ControllerDetails(@Nullable Bundle args) {
        super(args);
        pos = getArgs().getInt("POS");
        movieBoxOffice = getArgs().getParcelable("ITEM");
    }

    private static final String TAG = ControllerDetails.class.getSimpleName();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        binding = ControllerDetailsBinding.inflate(inflater, container, false);
        Context context = getApplicationContext();
        Picasso.with(context).load(movieBoxOffice.poster()).noFade().into(binding.imageViewHeader);

        adapterMain = new GroupAdapter();
        adapterCasts = new GroupAdapter();

        ItemCastList itemCastList = new ItemCastList(adapterCasts);
        adapterMain.add(new ItemDetailsBasic(movieBoxOffice));//Summery from omdb
        adapterMain.add(itemCastList);//Casts from tmdb

        if (progressiveSection == null) {
            ItemCastProgressive progressiveItem = new ItemCastProgressive();
            progressiveSection = new Section();
            progressiveSection.add(progressiveItem);
            progressiveSection.add(progressiveItem);
            progressiveSection.add(progressiveItem);
            progressiveSection.add(progressiveItem);
            progressiveSection.add(progressiveItem);
        }
        adapterCasts.add(progressiveSection);

        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        presenter.updateStream().subscribe(this);
        presenter.getMovieDetails(movieBoxOffice.tmdb());

        return binding.getRoot();
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ViewStateDetails viewStateDetails) {
        final Movie movie = viewStateDetails.movieDetails();
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterCasts.remove(progressiveSection);
        final List<Cast> casts = movie.casts();
        final List<ItemCastRow> itemCastRows = Observable.fromIterable(casts).map(new Function<Cast, ItemCastRow>() {
            @Override
            public ItemCastRow apply(@io.reactivex.annotations.NonNull Cast cast) throws Exception {
                return new ItemCastRow(cast);
            }
        }).toList(casts.size()).blockingGet();
        adapterCasts.addAll(itemCastRows);
        adapterCasts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                // getRouter().pushController(RouterTransaction.with(ProfileController((itemCastRows.get(item.getPosition(item))))));
                Toast.makeText(ControllerDetails.this.getActivity(),
                        String.format(Locale.ENGLISH, "You clicked %s", ((ItemCastRow) item).getCast().name()), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

    }

    @Override
    public void onComplete() {

    }
}
