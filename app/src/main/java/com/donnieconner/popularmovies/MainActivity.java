package com.donnieconner.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MoviePosterAdapter mMoviePosterAdapter;
    private ContentLoadingProgressBar mProgressBar;
    private List<Movie> mMovies;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
        fetchMoviesTask.execute("popular");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popular) {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
            fetchMoviesTask.execute("popular");
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_top_rated) {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
            fetchMoviesTask.execute("top_rated");
        }

        return super.onOptionsItemSelected(item);
    }

    public void renderPosterGrid(List<Movie> movies) {
        mMovies = movies;
        mMoviePosterAdapter = new MoviePosterAdapter(mMovies);

//        GridView gridView = (GridView) findViewById(R.id.poster_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.poster_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        if(mRecyclerView != null) {
            mRecyclerView.setAdapter(mMoviePosterAdapter);
        } else {
            Log.e(LOG_TAG, "Could not find Poster layout GridView");
        }

    }

    public void updateMoviePosterAdapter(List<Movie> movies) {
        mMovies = movies;
        mMoviePosterAdapter = new MoviePosterAdapter(mMovies);
    }

    public void showProgressBar() {
        mProgressBar.show();
    }

    public void hideProgressBar() {
        mProgressBar.hide();
    }
}
