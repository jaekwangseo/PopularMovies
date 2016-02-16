package com.jaekwang.popularmovies;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents movie
 * Created by Jaekwang on 2/11/2016.
 */
public class Movie {

    private final String LOG_TAG = Movie.class.getSimpleName();

    private String posterPath;
    private boolean adult;
    private Date releaseDate;
    private int movieDBID;
    private String originalTitle;
    private String title;
    private String backdrop_path;
    private int voteCount;
    private double voteAvg;

    public Movie(String posterPath, boolean adult, String overview, String releaseDate, int movieDBID, String originalTitle, String title,
        String backdrop_path, int voteCount, double voteAvg) {

        this.posterPath = posterPath;
        this.adult = adult;
        this.releaseDate = parseDate(releaseDate);
        this.movieDBID = movieDBID;
        this.originalTitle = originalTitle;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.voteCount = voteCount;
        this.voteAvg = voteAvg;
    }

    private Date parseDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {

            date = formatter.parse(dateInString.trim());

        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error in parsing date string", e);
        }

        return date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieDBID() {
        return movieDBID;
    }

    public void setMovieDBID(int movieDBID) {
        this.movieDBID = movieDBID;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(double voteAvg) {
        this.voteAvg = voteAvg;
    }
}
