package com.materialapp.varunramani.materialapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import network.VolleySingleton;

/**
 * Created by Varun Ramani on 3/15/2015.
 */
public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolderBoxOffice> {

    private LayoutInflater layoutInflater;
    private ArrayList<Movie> listMovies = new ArrayList<>();

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AdapterBoxOffice(Context context) {
        layoutInflater = layoutInflater.from(context);
        //Log.d("VRR", "LAYOUT INFLATER is: " + layoutInflater);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieList(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        //Log.d("VRR", "listMovies size is: " + listMovies.size());
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderBoxOffice viewHolderBoxOffice = new ViewHolderBoxOffice(view);

        //Log.d("VRR", "viewHolderBoxOffice is: " + viewHolderBoxOffice);

        return viewHolderBoxOffice;
    }

    @Override
    public void onBindViewHolder(final ViewHolderBoxOffice holder, int position) {
        //Log.d("VRR", "onBindViewHolder AdapterBoxOffice" + position);
        Movie currentMovie = listMovies.get(position);

        holder.movieTitle.setText(currentMovie.getTitle());
        //holder.movieReleaseDate.setText(currentMovie.getReleaseDateTheater().toString());

        Date movieReleaseDate = currentMovie.getReleaseDateTheater();

        if(movieReleaseDate != null){
            String formattedDate = dateFormat.format(movieReleaseDate);
            holder.movieReleaseDate.setText(formattedDate);
        } else {
            holder.movieReleaseDate.setText(Constants.NA);
        }

        int audienceScore = currentMovie.getAudienceScore();

        if (audienceScore != -1){
            holder.movieAudienceScore.setRating(audienceScore / 20.0F);
            holder.movieAudienceScore.setAlpha(1.0F);
        } else {
            holder.movieAudienceScore.setRating(0.0F);
            holder.movieAudienceScore.setAlpha(0.5F);
        }


        String urlThumbnail = currentMovie.getUrlThumbnail();
        loadImages(urlThumbnail, holder);
    }

    private void loadImages(String urlThumbnail, final ViewHolderBoxOffice holder){
        if (!urlThumbnail.equals(Constants.NA)) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //Log.d("VRR", "listMovies getItemCount is: " + listMovies.size());
        return listMovies.size();
    }

    static class ViewHolderBoxOffice extends RecyclerView.ViewHolder {

        private ImageView movieThumbnail;
        private TextView movieTitle;
        private TextView movieReleaseDate;
        private RatingBar movieAudienceScore;


        public ViewHolderBoxOffice(View itemView) {
            super(itemView);

            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }
    }
}
