package com.materialapp.varunramani.materialapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Varun Ramani on 3/15/2015.
 */
public class Movie implements Parcelable {
    private long id;
    private String title;
    private Date releaseDateTheatre;
    private int audienceScore;
    private String urlThumbnail;
    private String urlSelf;
    private String urlCast;
    private String urlReviews;
    private String urlSimilar;
    private String synopsis;

    public Movie(){

    }

    public Movie(Parcel input){
        id = input.readLong();
        title = input.readString();
        releaseDateTheatre = new Date(input.readLong());
        audienceScore = input.readInt();
        urlThumbnail = input.readString();
        urlSelf = input.readString();
        urlCast = input.readString();
        urlReviews = input.readString();
        urlSimilar = input.readString();
        synopsis = input.readString();
    }

    public Movie(long id,
            String title,
            Date releaseDateTheatre,
            int audienceScore,
            String urlThumbnail,
            String urlSelf,
            String urlCast,
            String urlReviews,
            String urlSimilar){

        this.id = id;
        this.title = title;
        this.releaseDateTheatre = releaseDateTheatre;
        this.audienceScore = audienceScore;
        this.urlThumbnail = urlThumbnail;
        this.urlSelf = urlSelf;
        this.urlCast = urlCast;
        this.urlReviews = urlReviews;
        this.urlSimilar = urlSimilar;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        Log.d("VRR", "ID is: " + id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Log.d("VRR", "Title Movie is: " + title);
        this.title = title;
    }

    public Date getReleaseDateTheater() {
        return releaseDateTheatre;
    }

    public void setReleaseDateTheater(Date releaseDateTheater) {
        Log.d("VRR", "releaseDate Movie is: " + releaseDateTheater);
        this.releaseDateTheatre = releaseDateTheater;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(int audienceScore) {
        Log.d("VRR", "audienceScore Movie is: " + audienceScore);
        this.audienceScore = audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        Log.d("VRR", "synopsis Movie is: " + synopsis);
        this.synopsis = synopsis;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        Log.d("VRR", "urlThumbNail Movie is: " + urlThumbnail);
        this.urlThumbnail = urlThumbnail;
    }

    public String getUrlSelf() {
        return urlSelf;
    }

    public void setUrlSelf(String urlSelf) {
        this.urlSelf = urlSelf;
    }

    public String getUrlCast() {
        return urlCast;
    }

    public void setUrlCast(String urlCast) {
        this.urlCast = urlCast;
    }

    public String getUrlReviews() {
        return urlReviews;
    }

    public void setUrlReviews(String urlReviews) {
        this.urlReviews = urlReviews;
    }

    public String getUrlSimilar() {
        return urlSimilar;
    }

    public void setUrlSimilar(String urlSimilar) {
        this.urlSimilar = urlSimilar;
    }

    @Override
    public int describeContents() {
        Toast.makeText(MyApplication.getAppContext(), "Describe contents Movie", Toast.LENGTH_LONG).show();
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Toast.makeText(MyApplication.getAppContext(), "Write to parcel Movie", Toast.LENGTH_LONG).show();
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(releaseDateTheatre.getTime());
        dest.writeInt(audienceScore);
        dest.writeString(urlThumbnail);
        dest.writeString(urlSelf);
        dest.writeString(urlCast);
        dest.writeString(urlReviews);
        dest.writeString(urlSimilar);
        dest.writeString(synopsis);
    }


    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            Toast.makeText(MyApplication.getAppContext(), "createFromParcel: Movie", Toast.LENGTH_LONG).show();
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /*@Override
    public String toString(){
        return "ID: " + id
                + "Title: " + title
                + "Release Date: " + releaseDateTheatre
                + "Synopsis: " + synopsis
                + "Score: " + audienceScore
                + "urlThumbnail: " + urlThumbnail;
    }*/
}
