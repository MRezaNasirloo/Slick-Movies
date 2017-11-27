package com.github.pedramrn.slick.parent.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerListBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.state.ViewStateList;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerList extends ControllerElm<ViewStateList> implements ViewList {

    @Inject
    Provider<PresenterList> provider;
    @Presenter
    PresenterList presenter;
    private final ArrayList<ItemViewListParcelable> data;
    private final String title;
    private GroupAdapter adapter;
    private UpdatingGroup adapterItems;

    @Override
    public ArrayList<ItemViewListParcelable> data() {
        return data;
    }

    public ControllerList(@NonNull Bundle args) {
        super(args);
        data =  args.getParcelableArrayList("DATA");
        title = args.getString("TITLE");
    }

    public ControllerList(@NonNull String title, @NonNull ArrayList<ItemViewListParcelable> itemViews) {
        this(new BundleBuilder(new Bundle())
                .putString("TITLE", title)
                .putParcelableArrayList("DATA", itemViews)
                .build()
        );
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Navigator2.bind(this);
        App.componentMain().inject(this);
        ControllerList_Slick.bind(this);
        ControllerListBinding binding = ControllerListBinding.inflate(inflater, container, false);

        binding.toolbar.setTitle(title);
        setToolbar(binding.toolbar).setupButton(binding.toolbar, true);

        adapter = new GroupAdapter();
        adapterItems = new UpdatingGroup();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                if (item instanceof OnItemAction) {
                    ((OnItemAction) item).action(ControllerList.this, null, adapter.getAdapterPosition(item));
                }
            }
        });

        // Section section = new Section(new ItemCardHeader(0, "All Casts"));
        // section.add(adapterItems);

        adapter.add(adapterItems);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.getItemAnimator().setMoveDuration(0);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    private static final String TAG = ControllerList.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
    }

    @Override
    public void onNext(ViewStateList state) {
        Log.d(TAG, "onNext() called with: state = [" + state + "]");
        adapterItems.update(state.items());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    public static void start(Router router, String title, ArrayList<ItemViewListParcelable> itemViews) {
        router.pushController(RouterTransaction.with(new ControllerList(title, itemViews))
                                      .pushChangeHandler(new HorizontalChangeHandler())
                                      .popChangeHandler(new HorizontalChangeHandler()));
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        adapter.setOnItemClickListener(null);
        adapter.clear();
        super.onDestroyView(view);
    }
}
