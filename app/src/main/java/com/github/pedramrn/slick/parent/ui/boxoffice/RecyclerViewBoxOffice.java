package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class RecyclerViewBoxOffice extends RecyclerView.Adapter<RecyclerViewBoxOffice.ViewHolder> {

    private List<MovieItem> movieItems = Collections.emptyList();

    public void setMovieItems(List<MovieItem> movieItems) {
        this.movieItems = movieItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RowBoxOfficeBinding binding = RowBoxOfficeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textViewScoreImdb.setText(movieItems.get(position).scoreImdb());
        holder.textViewTitle.setText(movieItems.get(position).name());
//        Glide.with(holder.textViewTitle.getContext().getApplicationContext()).load(movieItems.get(position).poster()).into(holder.imageView);
        Picasso.with(holder.textViewTitle.getContext().getApplicationContext()).load(movieItems.get(position).poster()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewScoreImdb;
        private final TextView textViewTitle;
        private final ImageView imageView;

        public ViewHolder(View itemView, RowBoxOfficeBinding binding) {
            super(itemView);
            textViewScoreImdb = binding.textViewScoreImdb;
            textViewTitle = binding.textViewTitle;
            imageView = binding.imageView;
        }
    }
}
