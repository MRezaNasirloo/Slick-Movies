package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
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
 *         Created on: 2017-04-13
 */

public class ControllerBoxOffice extends ControllerBase implements ViewBoxOffice, Retryable {
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
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull final ViewGroup container) {
        componentMain().inject(this);
        PresenterBoxOffice_Slick.bind(this);
        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);

        binding.toolbar.setTitle("Box Office");
        setToolbar(binding.toolbar);

        adapter = new GroupAdapter();
        updatingGroup = new UpdatingGroup();
        adapter.add(updatingGroup);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        recyclerView = binding.recyclerView;
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item).action(ControllerBoxOffice.this, null, adapter.getAdapterPosition(item)));

        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        adapter.setOnItemClickListener(null);
        // recyclerView.setAdapter(null);
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
}
