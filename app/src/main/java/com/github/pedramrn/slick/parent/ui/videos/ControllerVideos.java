package com.github.pedramrn.slick.parent.ui.videos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerVideosBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
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

import static com.github.pedramrn.slick.parent.ui.videos.state.Error.VIDEOS;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerVideos extends FragmentBase implements ViewVideos, OnItemClickListener {

    private static final String TAG = ControllerVideos.class.getSimpleName();

    @Inject
    Provider<PresenterVideos> provider;
    @Presenter
    PresenterVideos presenter;

    private String transitionName;
    private MovieBasic movie;
    private UpdatingGroup adapterProgressive;

    private GroupAdapter adapter;
    private ControllerVideosBinding binding;

    public static ControllerVideos newInstance(@NonNull MovieBasic movie, String transitionName) {
        Bundle args = new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movie)
                .putString("TRANSITION_NAME", transitionName)
                .build();

        ControllerVideos fragment = new ControllerVideos();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        transitionName = bundle.getString("TRANSITION_NAME");
        movie = bundle.getParcelable("ITEM");
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle) {
        App.componentMain().inject(this);
        PresenterVideos_Slick.bind(this);
        binding = ControllerVideosBinding.inflate(inflater, container, false);
        setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        binding.toolbar.setTitle(String.format("Videos: %s", movie.title()));
        adapter = new GroupAdapter();
        adapterProgressive = new UpdatingGroup();
        adapter.add(adapterProgressive);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager
                .VERTICAL,
                false));
        binding.recyclerView.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen
                .item_decoration_margin)));
        binding.recyclerView.getItemAnimator().setChangeDuration(0);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // binding.recyclerView.setAdapter(null);
        adapter.setOnItemClickListener(null);
        adapterProgressive = null;
        adapter = null;
        binding = null;
    }

    @Override
    public MovieBasic movie() {
        return movie;
    }

    @Override
    public void update(List<Item> videos) {
        adapterProgressive.update(videos);
    }

    @Override
    public void error(short code) {
        snackbarManager().show(ErrorHandler.message(getContext().getApplicationContext(), code));
    }

    @NonNull
    @Override
    public Observable<Object> onErrorDismissed() {
        return errorDismissed;
    }

    @NonNull
    @Override
    public Observable<Object> onRetry() {
        return retry;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(ControllerVideos.this, this, VIDEOS, adapter.getAdapterPosition(item), view);
    }

    @Override
    public void onRetry(String tag) {
        retry.onNext(tag);
    }
}
