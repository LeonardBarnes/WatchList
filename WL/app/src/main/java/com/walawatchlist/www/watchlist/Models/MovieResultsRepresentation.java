package com.walawatchlist.www.watchlist.Models;

/**
 * Created by Leonard on 2017/12/23.
 */

public class MovieResultsRepresentation {

    private String movieName;
    private String[] genre;
    private String moviePoster;
    private double votes;
    private String movieOverview;
    private String movieReleaseDate;
    private double movieVoteCount;
    private double moviePopularity;
    private Boolean watchLaterList;


    public MovieResultsRepresentation() {
    }

    public MovieResultsRepresentation(String movieName, String moviePoster, double votes, String movieOverview, String movieReleaseDate, double movieVoteCount, double moviePopularity, Boolean watchLater) {
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.votes = votes;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.movieVoteCount = movieVoteCount;
        this.moviePopularity = moviePopularity;
        this.watchLaterList = watchLater;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public double getVotes() {
        return votes;
    }

    public void setVotes(double votes) {
        this.votes = votes;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public double getMovieVoteCount() {
        return movieVoteCount;
    }

    public void setMovieVoteCount(double movieVoteCount) {
        this.movieVoteCount = movieVoteCount;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public void setMoviePopularity(double moviePopularity) {
        this.moviePopularity = moviePopularity;
    }


    public Boolean getWatchLaterList() {
        return watchLaterList;
    }

    public void setWatchLaterList(Boolean watchLaterList) {
        this.watchLaterList = watchLaterList;
    }
}
