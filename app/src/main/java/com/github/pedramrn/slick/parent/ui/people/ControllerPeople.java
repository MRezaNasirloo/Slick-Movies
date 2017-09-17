package com.github.pedramrn.slick.parent.ui.people;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerPeopleBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.custom.ImageViewCircular;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.item.ItemCreditsProgressive;
import com.github.pedramrn.slick.parent.ui.people.model.Person;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 *
 */
public class ControllerPeople extends ControllerElm<ViewStatePeople>
        implements ViewPeople, AppBarLayout.OnOffsetChangedListener, OnItemClickListener {

    @Inject
    Provider<PresenterPeople> provider;
    @Presenter
    PresenterPeople presenter;

    private final String transitionName;
    private final Person person;

    private final String TV_SHOWS = "TV Shows";
    private final String MOVIES = "Movies";

    private GroupAdapter adapter;
    private GroupAdapter adapterMovies;
    private GroupAdapter adapterTvShows;

    private ControllerPeopleBinding binding;
    private int maxScroll;
    private int deltaHeight;
    private ViewStatePeople state;

    public ControllerPeople(Person person, String transitionName) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", person)
                .putString("TRANSITION_NAME", transitionName)
                .build());
    }

    public ControllerPeople(@Nullable Bundle args) {
        super(args);
        transitionName = getArgs().getString("TRANSITION_NAME");
        person = getArgs().getParcelable("ITEM");
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        ControllerPeople_Slick.bind(this);
        binding = ControllerPeopleBinding.inflate(inflater, container, false);

        setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        binding.toolbar.setTitle("");

        RxView.clicks(binding.imageViewProfile)
                .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        PersonDetails ps = state.personDetails();
                        if (ps != null) {
                            ArrayList<String> images = new ArrayList<>(ps.images());
                            images.remove(ps.profilePicId());
                            images.add(0, ps.profilePicId());
                            ControllerImage.start(getRouter(), ps.name(), images);
                        }
                    }
                });

        adapter = new GroupAdapter();

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapterMovies = new GroupAdapter();
        adapterMovies.add(new ItemCreditsProgressive());
        adapterMovies.add(new ItemCreditsProgressive());
        adapterMovies.add(new ItemCreditsProgressive());

        ItemCardList movies = new ItemCardList(getActivity(), adapterMovies, MOVIES);
        Section sectionMovies = new Section(new ItemCardHeader(0, "Movies", null));
        sectionMovies.add(movies);

        adapterTvShows = new GroupAdapter();
        adapterTvShows.add(new ItemCreditsProgressive());
        adapterTvShows.add(new ItemCreditsProgressive());
        adapterTvShows.add(new ItemCreditsProgressive());
        ItemCardList tvShows = new ItemCardList(getActivity(), adapterTvShows, TV_SHOWS);

        Section sectionTvShows = new Section(new ItemCardHeader(0, "TV Shows", null));
        sectionTvShows.add(tvShows);

        adapter.add(sectionMovies);
        adapter.add(sectionTvShows);

        adapter.setOnItemClickListener(this);
        adapterMovies.setOnItemClickListener(this);

        binding.imageViewHeader.loadBlur(person.profileThumbnail());
        binding.imageViewHeader.loadBlurNP(person.profileMedium());
        binding.imageViewProfile.load(person.profileThumbnail());
        binding.imageViewProfile.loadNP(person.profileMedium());
        binding.textViewName.setText(person.name());
        binding.appbar.addOnOffsetChangedListener(this);
        presenter.updateStream().subscribe(this);
        return binding.getRoot();
    }

    @Override
    public int personId() {
        return person.id();
    }

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
    }

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public void onNext(ViewStatePeople state) {
        this.state = state;
        PersonDetails personDetails = state.personDetails();
        setHeader(personDetails);
        Item bio = state.itemBio();
        if (bio != null && adapter.getAdapterPosition(bio) == -1) {
            adapter.add(2, bio);
        }

        if (!state.moviesCast().isEmpty() || !state.moviesCrew().isEmpty()) {
            adapterMovies.clear();
            adapterMovies.addAll(state.moviesCast());
            adapterMovies.addAll(state.moviesCrew());
        }

        if (!state.tvShowsCast().isEmpty() || !state.tvShowsCrew().isEmpty()) {
            adapterTvShows.clear();
            adapterTvShows.addAll(state.tvShowsCast());
            adapterTvShows.addAll(state.tvShowsCrew());
        }

        renderError(state.errorPersonDetails());
    }

    private void setHeader(PersonDetails personDetails) {
        if (personDetails == null) return;
        binding.textViewName.setText(personDetails.nameBorn());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete() called");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScroll == 0) {
            maxScroll = appBarLayout.getTotalScrollRange();
            deltaHeight = binding.toolbar.getMeasuredHeight() +
                    binding.textViewName.getMeasuredHeight() +
                    binding.imageViewProfile.getMeasuredHeight() / 4;
        }
        int percentage = Math.abs(verticalOffset) * 100 / maxScroll;

        ImageViewCircular view = binding.imageViewProfile;

        if (percentage <= 60) {
            float value = Math.abs(percentage - 100) / 100f;
            view.setScaleX(value);
            view.setScaleY(value);
        }
        if (Math.abs(verticalOffset - deltaHeight) >= maxScroll) {
            view.setTranslationY(maxScroll + verticalOffset - deltaHeight);
        }

    }

    private static final String TAG = ControllerPeople.class.getSimpleName();

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(this, null, adapterMovies.getAdapterPosition(item));
    }
}
