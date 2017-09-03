package com.github.pedramrn.slick.parent.ui.people;

import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.people.mapper.MapperPersonDetailsDomainPersonDetails;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.github.pedramrn.slick.parent.ui.people.router.RouterPersonImpl;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeoplePartials;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterPeople extends PresenterBase<ViewPeople, ViewStatePeople> {

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
                .map(new Function<PersonDetails, PartialViewState<ViewStatePeople>>() {
                    @Override
                    public PartialViewState<ViewStatePeople> apply(@NonNull PersonDetails personDetails) throws Exception {
                        return new ViewStatePeoplePartials.Person(personDetails);
                    }
                }).onErrorReturn(new Function<Throwable, PartialViewState<ViewStatePeople>>() {
                    @Override
                    public PartialViewState<ViewStatePeople> apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStatePeoplePartials.ErrorPerson(throwable);
                    }
                })
                .subscribeOn(io);


        reduce(ViewStatePeople.builder().build(), merge(person, Observable.<PartialViewState<ViewStatePeople>>never())).subscribe(this)
        ;
    }

}
