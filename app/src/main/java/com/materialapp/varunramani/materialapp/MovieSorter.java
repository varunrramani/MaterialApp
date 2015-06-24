package com.materialapp.varunramani.materialapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Varun Ramani on 3/29/2015.
 */
public class MovieSorter {
    public void sortMovieByName(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }

    public void sortMovieByDate(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getReleaseDateTheater().compareTo(rhs.getReleaseDateTheater());
            }
        });
    }

    public void sortMovieByRatings(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs = lhs.getAudienceScore();
                int ratingRhs = rhs.getAudienceScore();
                int returnScore = 0;

                if(ratingLhs < ratingRhs){
                    returnScore = 1;
                } else if (ratingLhs == ratingRhs){
                    returnScore = 0;
                } else if (ratingLhs > ratingRhs){
                    returnScore = -1;
                }
                return returnScore;
            }
        });
    }
}
