package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolder> implements Observer<List<MovieBoxOffice>> {

    private static final String TAG = AdapterBoxOffice.class.getSimpleName();
    private final CompositeDisposable disposable;
    private static ImageLoader imageLoader;
    private final Resources resources;
    private List<MovieBoxOffice> movieBoxOfficeItems = Collections.emptyList();
    private final PublishSubject<Pair<MovieBoxOffice, Integer>> command;

    public AdapterBoxOffice(CompositeDisposable disposable, ViewModelBoxOffice viewModelBoxOffice, ImageLoader imageLoader, Resources resources) {
        this.disposable = disposable;
        this.imageLoader = imageLoader;
        this.resources = resources;
        viewModelBoxOffice.boxOfficeList().subscribe(this);
        command = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBoxOfficeBinding binding = RowBoxOfficeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        //        binding.imageView.setParallaxStyles(new VerticalMovingStyle(0.1f));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String transitionName = resources.getString(R.string.transition_poster, position);

        holder.binding.setVariable(BR.vm, movieBoxOfficeItems.get(position));
        holder.binding.setRank(movieBoxOfficeItems.get(position).rank(position));
        //        holder.binding.textViewTitle.setText(movieBoxOfficeItems.getFull(position).name());
        imageLoader.with(holder.binding.imageView.getContext().getApplicationContext())
                .load(movieBoxOfficeItems.get(position).posterMedium()).into(holder.binding.imageView);
        holder.binding.imageView.setTransitionName(transitionName);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Pair<MovieBoxOffice, Integer> pair = new Pair<>(movieBoxOfficeItems.get(pos), pos);
                command.onNext(pair);
            }
        });
    }

    public PublishSubject<Pair<MovieBoxOffice, Integer>> streamCommand() {
        return command;
    }

    @Override
    public long getItemId(int position) {
        return movieBoxOfficeItems.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return movieBoxOfficeItems.size();
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(final List<MovieBoxOffice> newMovies) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return movieBoxOfficeItems.size();
            }

            @Override
            public int getNewListSize() {
                return newMovies.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(movieBoxOfficeItems.get(oldItemPosition).imdb(), newMovies.get(newItemPosition).imdb());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return movieBoxOfficeItems.get(oldItemPosition).hashCode() == newMovies.get(newItemPosition).hashCode();
            }
        });
        this.movieBoxOfficeItems = newMovies;
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
        //        Glide.with(holder.textViewTitle.getContext().getApplicationContext()).load(movieBoxOfficeItems.getFull(position).poster()).into(holder.imageView);
        imageLoader.with(imageView.getContext().getApplicationContext()).load(url).into(imageView);
    }


}
