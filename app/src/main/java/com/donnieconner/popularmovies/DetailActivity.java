package com.donnieconner.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(MoviePosterAdapter.MOVIE_EXTRA);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mMovie.getTitle());
        } else {
            Log.e(LOG_TAG, "Action bar is not found");
        }

        // Load toolbar background image
        ImageView toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        Picasso.with(this).load(mMovie.getPosterUrl()).into(toolbarImage);

        // Load poster image
        ImageView posterImage = (ImageView) findViewById(R.id.detail_movie_poster_image);
        Picasso.with(this).load(mMovie.getPosterUrl()).into(posterImage);

        // Set release date
        TextView dateTextView = (TextView) findViewById(R.id.detail_movie_date);
        if (dateTextView != null) {
            dateTextView.setText(mMovie.getReleaseDate());
        }

        // Set overview text
        TextView overviewTextView = (TextView) findViewById(R.id.detail_movie_overview);
        if (overviewTextView != null) {
            overviewTextView.setText(mMovie.getOverview());
        }

        // Set rating
        TextView ratingTextView = (TextView) findViewById(R.id.detail_movie_rating);
        if (ratingTextView != null) {
            String ratingFormat = getResources().getString(R.string.movie_rating);
            String rating = String.format(ratingFormat, mMovie.getRating());

            ratingTextView.setText(rating);
        }
    }
}
