package com.github.pedramrn.slick.parent.ui.people;


import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.people.mapper.MapperPersonDetailsDomainPersonDetails;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.github.pedramrn.slick.parent.ui.people.router.RouterPersonImpl;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

import static com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeoplePartials.ErrorPerson;
import static com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeoplePartials.Person;
import static com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeoplePartials.PersonCredits;

/**
 * A simple Presenter
 */
public class PresenterPeople extends SlickPresenterUni<ViewPeople, ViewStatePeople> {

    private final RouterPersonImpl routerPerson;
    private final MapperPersonDetailsDomainPersonDetails mapper;

    @Inject
    public PresenterPeople(
            RouterPersonImpl routerPerson,
            MapperPersonDetailsDomainPersonDetails mapper,
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
        this.routerPerson = routerPerson;
        this.mapper = mapper;
    }

    @Override
    protected void start(ViewPeople view) {

        Observable<PartialViewState<ViewStatePeople>> person = routerPerson.person(view.personId())
                .map(mapper)
                .map((Function<PersonDetails, PartialViewState<ViewStatePeople>>) Person::new)
                .onErrorReturn(ErrorPerson::new)
                .subscribeOn(io);

        Observable<PartialViewState<ViewStatePeople>> personCredits = routerPerson.works(view.personId())
                .map(mapper)
                .map((Function<PersonDetails, PartialViewState<ViewStatePeople>>) PersonCredits::new)
                .onErrorReturn(ErrorPerson::new)
                .subscribeOn(io);


        reduce(
                ViewStatePeople.builder()
                        .moviesCast(Collections.<Item>emptyList())
                        .moviesCrew(Collections.<Item>emptyList())
                        .tvShowsCast(Collections.<Item>emptyList())
                        .tvShowsCrew(Collections.<Item>emptyList())
                        .build(),
                merge(person, personCredits, Observable.<PartialViewState<ViewStatePeople>>never())
        )
                .subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStatePeople state, @NonNull ViewPeople view) {
        view.render(state);
    }

}
