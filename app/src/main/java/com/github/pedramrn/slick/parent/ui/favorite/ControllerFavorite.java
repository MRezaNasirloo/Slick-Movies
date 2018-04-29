package com.github.pedramrn.slick.parent.ui.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerFavoriteBinding;
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.auth.ControllerAuth;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerFavorite extends FragmentBase implements ViewFavorite, Navigator, Retryable, OnItemClickListener {

    @Inject
    Provider<PresenterFavorite> provider;
    @Presenter
    PresenterFavorite presenter;
    private UpdatingGroup updatingFavorite;
    private PublishSubject<Object> triggerRefresh = PublishSubject.create();
    private GroupAdapter adapter;
    private RecyclerView rc;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        App.componentMain().inject(this);
        PresenterFavorite_Slick.bind(this);
        ControllerFavoriteBinding binding = ControllerFavoriteBinding.inflate(inflater, container, false);

        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentByTag("FRAGMENT_LOG_IN");
        if (fragment == null) {
            fm.beginTransaction().replace(R.id.container_child, ControllerAuth.newInstance(false)).commit();
        }
        // binding.collapsingToolbar.setTitle(true);
        // binding.collapsingToolbar.setTitleEnabled(true);
        // binding.toolbar.setTitle("Favorites");
        // setToolbar(binding.toolbar);

        adapter = new GroupAdapter();
        updatingFavorite = new UpdatingGroup();
        adapter.add(updatingFavorite);

        rc = binding.recyclerViewFavorite;
        rc.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), VERTICAL, false));
        rc.setAdapter(adapter);
        rc.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen
                .item_margin_long)));
        adapter.setOnItemClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // if (!isBeingDestroyed()) { rc.setAdapter(null); }
        rc.setAdapter(null);
        adapter.setOnItemClickListener(null);
        rc = null;
        adapter = null;
        Log.d(TAG, "onDestroyView() called");
    }

    @Override
    public void updateFavorites(List<Item> favorites) {
        updatingFavorite.update(favorites);
    }

    @Override
    public Observable<Object> triggerRefresh() {
        return triggerRefresh;
    }

    @Override
    public void onRetry(String tag) {
        triggerRefresh.onNext(tag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private static final String TAG = ControllerFavorite.class.getSimpleName();

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(ControllerFavorite.this, null, adapter.getAdapterPosition(item), view);
    }

    @NonNull
    @Override
    public SnackbarManager snackbarManager() {
        throw new NotImplementedException("Sorry!!!");
    }
}
