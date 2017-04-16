package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class RecyclerViewBoxOffice extends RecyclerView.Adapter<RecyclerViewBoxOffice.ViewHolder> implements Consumer<List<MovieItem>> {

    private List<MovieItem> movieItems = Collections.emptyList();
    private final Disposable disposable;

    public RecyclerViewBoxOffice(Observable<List<MovieItem>> movieItems) {
        disposable = movieItems.subscribe(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBoxOfficeBinding binding = RowBoxOfficeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setVariable(BR.vm, movieItems.get(position));
    }

    @Override
    public long getItemId(int position) {
        return movieItems.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    @Override
    public void accept(@NonNull List<MovieItem> movieItems) throws Exception {
        this.movieItems = movieItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RowBoxOfficeBinding binding;

        public ViewHolder(View itemView, RowBoxOfficeBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }

    @BindingAdapter("imageUrl")
    public static void bindImageUrl(ImageView imageView, String url) {
        //        Glide.with(holder.textViewTitle.getContext().getApplicationContext()).load(movieItems.get(position).poster()).into(holder.imageView);
        Picasso.with(imageView.getContext().getApplicationContext()).load(url).into(imageView);
    }
}
