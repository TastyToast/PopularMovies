package com.donnieconner.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An Grabs movie data from the movies db API
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MainActivity mActivity;
    private Boolean mInitialLoad = true;

    public FetchMoviesTask(MainActivity activity) {
        this.mActivity = activity;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        mActivity.showProgressBar();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            // Construct the url for the MoviesDB query
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendEncodedPath(params[0])
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Getting url " + url);

            // Create the request to TheMovieDB and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null) {
                // Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null) {
                // Add new lines for readability
                String newLine = line + "\n";
                buffer.append(newLine);
            }

            if(buffer.length() == 0) {
                // The string is empty
                return null;
            }
            moviesJsonStr = buffer.toString();
            Log.v(LOG_TAG,moviesJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to fetch data from the API", e);
            return null;
        } finally {
            // Close the connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        // This will only happen if there's an error parsing the JSON string
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        //mActivity.hideProgressBar();

        if (mInitialLoad) {
            mActivity.renderPosterGrid(movies);
        } else {
            mActivity.updateMoviePosterAdapter(movies);
        }

        mInitialLoad = false;
    }

    private List<Movie> getMovieDataFromJson(String moviesJsonStr) throws JSONException {
        // JSON Objects to extract
        final String OVERVIEW = "overview";
        final String ORIGINAL_TITLE = "original_title";
        final String ID = "id";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String RESULTS = "results";
        final String RATING = "vote_average";

        JSONObject movieJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray(RESULTS);

        List<Movie> results = new ArrayList<>();
        Log.v(LOG_TAG, "moviesArray length is " + moviesArray.length());
        for (int i = 0; i < moviesArray.length(); i++) {
            String poster;
            String title;
            String id;
            String overview;
            String releaseDate;
            String rating;

            // Get JSON object representing movie
            JSONObject movie = moviesArray.getJSONObject(i);

            title = movie.getString(ORIGINAL_TITLE);
            poster = movie.getString(POSTER_PATH);
            id = movie.getString(ID);
            overview = movie.getString(OVERVIEW);
            releaseDate = movie.getString(RELEASE_DATE);
            rating = movie.getString(RATING);
            results.add(i, new Movie(title, poster, id, overview, rating, releaseDate));
        }

        return results;
    }
}
