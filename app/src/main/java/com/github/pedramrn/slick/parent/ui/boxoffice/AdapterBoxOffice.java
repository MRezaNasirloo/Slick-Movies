package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolder> implements Observer<List<MovieItem>> {
    private static final String TAG = AdapterBoxOffice.class.getSimpleName();
    private final CompositeDisposable disposable;
    private static ImageLoader imageLoader;
    private List<MovieItem> movieItems = Collections.emptyList();

    public AdapterBoxOffice(CompositeDisposable disposable, ViewModelBoxOffice viewModelBoxOffice, ImageLoader imageLoader) {
        this.disposable = disposable;
        this.imageLoader = imageLoader;
        viewModelBoxOffice.boxOfficeList().subscribe(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBoxOfficeBinding binding = RowBoxOfficeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.setVariable(BR.vm, movieItems.get(position));
        holder.binding.setRank(movieItems.get(position).rank(position));
        holder.binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.binding.getRoot().getContext(), holder.getAdapterPosition() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(final List<MovieItem> newMovieItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return movieItems.size();
            }

            @Override
            public int getNewListSize() {
                return newMovieItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(movieItems.get(oldItemPosition).imdb(), newMovieItems.get(newItemPosition).imdb());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return movieItems.get(oldItemPosition).hashCode() == newMovieItems.get(newItemPosition).hashCode();
            }
        });
        this.movieItems = newMovieItems;
        diffResult.dispatchUpdatesTo(this);

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RowBoxOfficeBinding binding;

        public ViewHolder(RowBoxOfficeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @BindingAdapter("imageUrl")
    public static void bindImageUrl(ImageView imageView, String url) {
        //        Glide.with(holder.textViewTitle.getContext().getApplicationContext()).load(movieItems.get(position).poster()).into(holder.imageView);
        imageLoader.with(imageView.getContext().getApplicationContext()).load(url).into(imageView);
    }


}
