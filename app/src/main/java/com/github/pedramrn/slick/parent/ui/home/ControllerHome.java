package com.github.pedramrn.slick.parent.ui.home;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList_Slick;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.PresenterSearch_Slick;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-20
 */

public class ControllerHome extends ControllerBase implements ViewHome {

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;

    private UpdatingGroup progressiveUpcoming;

    private SearchViewImpl searchView;

    private PublishSubject<String> onRetry = PublishSubject.create();

    private static final String trending = "Trending";
    private static final String popular = "Popular";
    private static final String upcoming = "Upcoming";
    private RecyclerViewCardListTrending recyclerViewCardListTrending;
    private RecyclerViewCardListPopular recyclerViewCardListPopular;
    private GroupAdapter adapterUpcoming;
    private RecyclerView recyclerViewUpcoming;
    private ScrollingPagerIndicator pageIndicator;
    private final Uri uri;
    private boolean handled;

    public ControllerHome(@Nullable Bundle args) {
        super(args);
        uri = getArgs().getParcelable("URI");
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Log.d(TAG, "onCreateView");
        App.componentMain().inject(this);
        PresenterHome_Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        adapterUpcoming = new GroupAdapter();
        progressiveUpcoming = new UpdatingGroup();

        final Context context = getApplicationContext();

        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.row_card, 12);
        recyclerViewCardListTrending = binding.recyclerViewTrending;
        recyclerViewCardListPopular = binding.recyclerViewPopular;

        String instanceId = getInstanceId();
        recyclerViewCardListPopular.onBind(instanceId);
        recyclerViewCardListTrending.onBind(instanceId + 1);

        recyclerViewCardListTrending.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListPopular.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListTrending.setRouter(getRouter());
        recyclerViewCardListPopular.setRouter(getRouter());
        recyclerViewCardListPopular.setRecycledViewPool(recycledViewPool);
        recyclerViewCardListTrending.setRecycledViewPool(recycledViewPool);

        SnapHelper snapHelperStartTrending = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelperStartPopular = new GravitySnapHelper(Gravity.START);
        snapHelperStartTrending.attachToRecyclerView(recyclerViewCardListTrending);
        snapHelperStartPopular.attachToRecyclerView(recyclerViewCardListPopular);

        adapterUpcoming.add(progressiveUpcoming);


        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        searchView.onBind(instanceId);
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

        recyclerViewUpcoming = binding.recyclerViewUpcoming;
        recyclerViewUpcoming.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layout);
        recyclerViewUpcoming.setAdapter(adapterUpcoming);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerViewUpcoming);

        pageIndicator = binding.pageIndicator;
        pageIndicator.attachToRecyclerView(recyclerViewUpcoming);

        //setup headers
        setupHeader(binding.headerUpcoming, upcoming);
        setupHeader(binding.headerTrending, trending);
        setupHeader(binding.headerPopular, popular);

        handleIntent(uri);

        return binding.getRoot();
    }

    private void handleIntent(Uri uri) {
        Log.d(TAG, "handleIntent() called with: uri = [" + uri + "]");
        if (uri != null && !handled) {
            handled = true;
            List<String> pathSegments = uri.getPathSegments();
            String path = pathSegments.size() > 0 ? pathSegments.get(0) : null;
            if ("title".equalsIgnoreCase(path)) {
                if (pathSegments.size() > 1) {
                    String imdbId = pathSegments.get(1);
                    ControllerDetails.start(getRouter(), imdbId, null);
                }
            } else if ("name".equalsIgnoreCase(path)) {
                // TODO: 2018-04-09 Launch Controller People
            }
        }
    }

    private void setupHeader(RowCardHeaderBinding header, String title, @Nullable Consumer<Object> clickListener) {
        if (clickListener == null) {
            header.button.setVisibility(View.INVISIBLE);
        } /*else {
            RxView.clicks(header.button).throttleLast(1, TimeUnit.SECONDS).subscribe(clickListener);
        }*/
        header.textViewTitle.setText(title);
    }

    private void setupHeader(RowCardHeaderBinding header, String title) {
        setupHeader(header, title, null);
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        recyclerViewCardListTrending.onSaveViewState(view, outState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onSaveViewState(view, outState, PresenterCardList.POPULAR);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        recyclerViewCardListTrending.onRestoreViewState(view, savedViewState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onRestoreViewState(view, savedViewState, PresenterCardList.POPULAR);
    }

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        Log.d(TAG, "onDestroyView() called");
        super.onDestroyView(view);
        recyclerViewUpcoming.setAdapter(null);
        adapterUpcoming.setOnItemClickListener(null);
        recyclerViewCardListTrending = null;
        recyclerViewCardListPopular = null;
        recyclerViewUpcoming = null;
        progressiveUpcoming = null;
        adapterUpcoming = null;
        pageIndicator = null;
        searchView = null;
    }

    @Override
    public boolean handleBack() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
            return true;
        }
        return super.handleBack();
    }

    @Override
    public Observable<Object> retryUpcoming() {
        return onRetry.filter(PresenterHome.UPCOMING::equals).cast(Object.class);
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item).action(ControllerHome.this, null, adapter
                .getAdapterPosition(item)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Activity activity = getActivity();
        String instanceId = getInstanceId();
        PresenterCardList_Slick.onDestroy(instanceId, activity);
        PresenterCardList_Slick.onDestroy(instanceId + 1, activity);
        PresenterSearch_Slick.onDestroy(instanceId, activity);
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("HANDLED", handled);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        handled = savedInstanceState.getBoolean("HANDLED", false);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
