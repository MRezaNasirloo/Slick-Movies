package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.slick.Presenter;
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

public class ControllerBoxOffice extends ControllerBase implements ViewBoxOffice {
    private static final String TAG = ControllerBoxOffice.class.getSimpleName();

    @Inject
    Provider<PresenterBoxOffice> provider;
    @Presenter
    PresenterBoxOffice presenter;

    private UpdatingGroup updatingGroup;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull final ViewGroup container) {
        componentMain().inject(this);
        ControllerBoxOffice_Slick.bind(this);
        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);

        binding.toolbar.setTitle("Box Office");
        setToolbar(binding.toolbar);

        GroupAdapter adapter = new GroupAdapter();
        updatingGroup = new UpdatingGroup();
        adapter.add(updatingGroup);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private boolean isLoadingNext() {
        return false;
    }

    @Override
    public void update(List<Item> items) {
        updatingGroup.update(items);
    }

    @Override
    public Observable<Integer> onLoadMore() {
        return Observable.never();
    }

    @Override
    public Observable<Object> onRetry() {
        return retry;
    }

    @Override
    public Observable<Object> onErrorDismissed() {
        return errorDismissed;
    }

    @Override
    public int pageSize() {
        return 5;
    }


    @Override
    public void error(String message) {

    }
}
