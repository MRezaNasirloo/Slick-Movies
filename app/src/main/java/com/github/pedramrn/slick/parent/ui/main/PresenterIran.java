package com.github.pedramrn.slick.parent.ui.main;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiIpLocation;
import com.github.pedramrn.slick.parent.datasource.network.models.IpLocation;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2018-06-21
 */
public class PresenterIran extends SlickPresenterUni<ViewIran, StateIran> {
    private static final String TAG = PresenterIran.class.getSimpleName();
    private final ApiIpLocation apiIpLocation;

    @Inject
    public PresenterIran(
            ApiIpLocation apiIpLocation,
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
        this.apiIpLocation = apiIpLocation;
    }

    @Override
    protected void start(@NonNull ViewIran view) {
        System.out.println("PresenterIran.start");
        Observable<PartialViewState<StateIran>> location = apiIpLocation.location().subscribeOn(io)
                .map((Function<IpLocation, PartialViewState<StateIran>>) ipLocation -> state -> state.toBuilder()
                        .ipLocation(ipLocation)
                        .build())
                .onErrorReturn(throwable -> state -> state.toBuilder()
                        .ipLocation(IpLocation.create(throwable.getMessage(), "", ""))
                        .build());

        Observable<PartialViewState<StateIran>> seen = command(ViewIran::messageDismissed)
                .map(o -> state -> state.toBuilder()
                        .seen(true)
                        .build());

        IpLocation ipLocation = IpLocation.create("NOT_REQUESTED", "NOPE", "NOPE");
        subscribe(StateIran.create(ipLocation, false), merge(location, seen));
    }

    @Override
    protected void render(@NonNull StateIran state, @NonNull ViewIran view) {
        System.out.println("state = [" + state + "]");
        IpLocation ipLocation = state.ipLocation();
        if (!state.seen() && "success".equalsIgnoreCase(ipLocation.status()) &&
                ("IR".equalsIgnoreCase(ipLocation.countryCode()) ||
                        "IRAN".equalsIgnoreCase(ipLocation.country()))) {
            view.showWarningIran();
        }
    }

}
