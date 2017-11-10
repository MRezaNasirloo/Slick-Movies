package com.github.pedramrn.slick.parent.ui.favorite;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerFavoriteBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerFavorite extends ControllerBase implements ViewFavorite {

    @Inject
    Provider<PresenterFavorite> provider;
    @Presenter
    PresenterFavorite presenter;
    private UpdatingGroup updatingFavorite;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: 2017-07-22 Inject dependencies
        App.componentMain().inject(this);
        ControllerFavorite_Slick.bind(this);
        ControllerFavoriteBinding binding = ControllerFavoriteBinding.inflate(inflater, container, false);

        GroupAdapter adapter = new GroupAdapter();
        updatingFavorite = new UpdatingGroup();
        adapter.add(updatingFavorite);

        binding.recyclerViewFavorite.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateFavorites(List<Item> favorites) {
        updatingFavorite.update(favorites);
    }
}
