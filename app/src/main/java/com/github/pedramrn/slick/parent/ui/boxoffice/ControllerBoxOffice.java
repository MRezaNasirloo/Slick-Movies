package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
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

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding.inflate;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-04-13
 */

public class ControllerBoxOffice extends FragmentBase implements ViewBoxOffice, Retryable, OnItemClickListener {
    private static final String TAG = ControllerBoxOffice.class.getSimpleName();

    @Inject
    Provider<PresenterBoxOffice> provider;
    @Presenter
    PresenterBoxOffice presenter;

    private UpdatingGroup updatingGroup;
    private GroupAdapter adapter;
    private RecyclerView recyclerView;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle bundle) {
        componentMain().inject(this);
        PresenterBoxOffice_Slick.bind(this);
        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);

        binding.toolbar.setTitle("Box Office");
        setToolbar(binding.toolbar);

        adapter = new GroupAdapter();
        updatingGroup = new UpdatingGroup();
        adapter.add(updatingGroup);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        recyclerView = binding.recyclerView;
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // if (!isBeingDestroyed()) { recyclerView.setAdapter(null); }
        recyclerView.setAdapter(null);
        adapter.setOnItemClickListener(null);
        recyclerView = null;
        updatingGroup = null;
        adapter = null;
    }

    @Override
    public void update(List<Item> items) {
        updatingGroup.update(items);
    }

    @Override
    public Observable<Object> onRetry() {
        return retry;
    }

    @Override
    public void onRetry(String tag) {
        retry.onNext(tag);
    }

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(ControllerBoxOffice.this, this, null, adapter.getAdapterPosition(item), view);
    }
}
