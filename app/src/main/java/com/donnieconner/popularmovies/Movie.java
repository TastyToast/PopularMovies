package com.donnieconner.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model representing a Movie
 */
public class Movie implements Parcelable {
    private String title;
    private String posterUrl;
    private String id;
    private String overview;
    private String rating;
    private String releaseDate; //TODO: This should be a date object

    public Movie(String title, String posterUrl, String id, String overview, String rating,
                 String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.id = id;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        id = in.readString();
        overview = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    // Model getters and setters

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        final String posterImageBaseURL = "http://image.tmdb.org/t/p/w500";

        return posterImageBaseURL + posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getRating() {
        return rating;
    }

    public String toString() {
        return title + "--" + posterUrl + "--" + id + "--" + overview + "--" + releaseDate;
    }

    // Overrides

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(id);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
