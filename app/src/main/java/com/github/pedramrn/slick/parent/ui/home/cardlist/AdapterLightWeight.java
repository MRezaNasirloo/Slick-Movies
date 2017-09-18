package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.OnItemLongClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class AdapterLightWeight extends RecyclerView.Adapter<ViewHolder> {

    public static final String TAG = AdapterLightWeight.class.getSimpleName();
    List<Item> items = new ArrayList<>(10);
    PublishSubject<List<Item>> updateStream = PublishSubject.create();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public AdapterLightWeight() {
        updateStream.concatMap(new Function<List<Item>, Observable<Pair<DiffUtil.DiffResult, List<Item>>>>() {
                    @Override
                    public Observable<Pair<DiffUtil.DiffResult, List<Item>>> apply(@NonNull List<Item> items) throws Exception {
                        Log.d(TAG, "diffCal on: " + Thread.currentThread().getName());
                        return Observable.just(Pair.create(DiffUtil.calculateDiff(new MyCallback(items)), items));
                    }
                }).subscribe(new Consumer<Pair<DiffUtil.DiffResult, List<Item>>>() {
            @Override
            public void accept(Pair<DiffUtil.DiffResult, List<Item>> diffResult) throws Exception {
                items.clear();
                items.addAll(diffResult.second);
                diffResult.first.dispatchUpdatesTo(AdapterLightWeight.this);
            }
        });
    }

    @Override
    public ViewHolder<? extends ViewDataBinding> onCreateViewHolder(ViewGroup parent, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
        return new ViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        item.bind(viewHolder.binding, position);
        viewHolder.bind(item, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        Item contentItem = holder.getItem();
        contentItem.unbind(holder);
    }

    @Override
    public boolean onFailedToRecycleView(ViewHolder holder) {
        Item contentItem = holder.getItem();
        return contentItem.isRecyclable();
    }

    @Override
    public int getItemViewType(int position) {
        Item contentItem = getItem(position);
        if (contentItem == null) { throw new RuntimeException("Invalid position " + position); }
        return getItem(position).getLayout();
    }

    public Item getItem(int position) {
        return items.get(position);
    }


    public Item getItem(ViewHolder holder) {
        return holder.getItem();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Optionally register an {@link OnItemClickListener} that listens to click at the root of
     * each Item where {@link Item#isClickable()} returns true
     *
     * @param onItemClickListener The click listener to set
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Optionally register an {@link OnItemLongClickListener} that listens to long click at the root of
     * each Item where {@link Item#isLongClickable()} returns true
     *
     * @param onItemLongClickListener The long click listener to set
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void update(@Nullable final List<Item> newItems) {
        if (newItems == null) {
            return;
        }
        updateStream.onNext(newItems);
    }

    public int getAdapterPosition(Item item) {
        return items.indexOf(item);
    }

    public void showLoading() {
        items.add(new ItemError(-1, "Loading", "Scroll for more"));
    }

    private class MyCallback extends DiffUtil.Callback {
        private final List<Item> newItems;

        public MyCallback(List<Item> newItems) {this.newItems = newItems;}

        @Override
        public int getOldListSize() {
            return items.size();
        }

        @Override
        public int getNewListSize() {
            return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Item itemCardOld = items.get(oldItemPosition);
            Item itemCardNew = newItems.get(newItemPosition);

            return itemCardOld.getId() == itemCardNew.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Item itemCardOld = items.get(oldItemPosition);
            Item itemCardNew = newItems.get(newItemPosition);

            return itemCardOld.equals(itemCardNew);
        }
    }
}
