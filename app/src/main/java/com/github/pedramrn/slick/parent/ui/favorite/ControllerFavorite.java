package com.github.pedramrn.slick.parent.ui.favorite;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerFavoriteBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.auth.ControllerAuth;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerFavorite extends ControllerBase implements ViewFavorite, Navigator, Retryable {

    @Inject
    Provider<PresenterFavorite> provider;
    @Presenter
    PresenterFavorite presenter;
    private UpdatingGroup updatingFavorite;
    private PublishSubject<Object> triggerRefresh = PublishSubject.create();


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        ControllerFavorite_Slick.bind(this);
        ControllerFavoriteBinding binding = ControllerFavoriteBinding.inflate(inflater, container, false);

        ViewGroup containerChild = binding.container.findViewById(R.id.container_child);
        Router childRouter = getChildRouter(containerChild).setPopsLastView(false);
        if (!childRouter.hasRootController()) {
            ControllerAuth controllerAuth = new ControllerAuth(false);
            childRouter.setRoot(RouterTransaction.with(controllerAuth));
        }
        // binding.collapsingToolbar.setTitle(true);
        // binding.collapsingToolbar.setTitleEnabled(true);
        // binding.toolbar.setTitle("Favorites");
        // setToolbar(binding.toolbar);

        GroupAdapter adapter = new GroupAdapter();
        updatingFavorite = new UpdatingGroup();
        adapter.add(updatingFavorite);

        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(adapter);
        binding.recyclerViewFavorite.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen.item_margin_long)));
        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item).action(ControllerFavorite.this, null, adapter.getAdapterPosition(item)));
        return binding.getRoot();
    }

    @Override
    public void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            throwable.printStackTrace();
        }
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
}
