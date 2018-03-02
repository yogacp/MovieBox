package com.moviebox.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moviebox.app.R;
import com.moviebox.app.ui.detail.DetailActivity;
import com.moviebox.app.utils.AppConstants;
import com.moviebox.app.vo.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 01/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> mMovies = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mTypeAdapter;

    public MovieAdapter(List<Movie> movies, Context context, int type){
        mContext = context;
        mTypeAdapter = type;
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = mLayoutInflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Picasso.with(mContext)
                .load(AppConstants.BASE_URL_IMAGE+movie.getPosterPath())
                .into(holder.mMovieCover);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(AppConstants.INTENT_TAG.TAG_DATA, movie);
            intent.putExtra(AppConstants.INTENT_TAG.TAG_SOURCE,mTypeAdapter);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mMovieCover;
        public ViewHolder(View itemView) {
            super(itemView);
            mMovieCover = (ImageView) itemView.findViewById(R.id.item_movie_cover);
        }
    }

}
