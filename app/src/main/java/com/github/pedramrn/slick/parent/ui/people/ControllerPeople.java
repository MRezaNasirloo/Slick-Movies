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

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerPeopleBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.custom.ImageViewCircular;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.people.item.ItemPersonDetails;
import com.github.pedramrn.slick.parent.ui.people.model.Person;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerPeople extends ControllerElm<ViewStatePeople> implements ViewPeople, AppBarLayout.OnOffsetChangedListener {

    @Inject
    Provider<PresenterPeople> provider;
    @Presenter
    PresenterPeople presenter;

    private final String transitionName;
    private final Person person;
    private GroupAdapter adapter;
    private int maxScroll;
    private ControllerPeopleBinding binding;
    private int deltaHeight;

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
        setToolbar(binding.toolbar).setupButton(true);
        binding.toolbar.setTitle("");
        RecyclerView recyclerView = binding.recyclerView;
        adapter = new GroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        binding.imageViewHeader.loadBlur(person.profileThumbnail());
        binding.imageViewHeader.loadBlur(person.profileMedium());
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
    public void onNext(ViewStatePeople viewStatePeople) {
        PersonDetails personDetails = viewStatePeople.personDetails();
        setHeader(personDetails);
        int itemCount = adapter.getItemCount();
        if (itemCount == 0 && personDetails != null) {
            adapter.add(0, new ItemPersonDetails(personDetails));
        }

        renderError(viewStatePeople.errorPersonDetails());
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
}
