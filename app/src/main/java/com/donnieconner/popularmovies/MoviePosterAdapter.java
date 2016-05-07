package com.donnieconner.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An ArrayAdapter that handles generating image views for movie posters
 */
public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {

    public static final String MOVIE_EXTRA = "com.donnieconner.popularmovies.MOVIE_EXTRA";

    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    private List<Movie> mMovies;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public Context mContext;

        public ViewHolder(View v) {
            super(v);
            mContext = v.getContext();
            mView = v;
        }
    }

    public MoviePosterAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie_poster, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        ImageView imageView = (ImageView) holder.mView
                .findViewById(R.id.grid_item_movie_poster_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMovieDetailView(holder.mContext, movie);
            }
        });

        Log.v(LOG_TAG, "Position " + position);
        Log.v(LOG_TAG, "Attempting to load poster from url " + movie.getPosterUrl());
        Picasso.with(holder.mView.getContext())
                .load(movie.getPosterUrl())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void openMovieDetailView(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);

        context.startActivity(intent);
    }

}
