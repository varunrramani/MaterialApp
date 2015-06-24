package service;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.materialapp.varunramani.materialapp.Constants;
import com.materialapp.varunramani.materialapp.Movie;
import com.materialapp.varunramani.materialapp.MyApplication;
import com.materialapp.varunramani.materialapp.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;
import network.VolleySingleton;

import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_AUDIENCE_SCORE;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_ID;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_MOVIES;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_POSTERS;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_RATINGS;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_RELEASE_DATES;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_SYNOPSIS;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_THEATER;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_THUMBNAIL;
import static com.materialapp.varunramani.materialapp.keys.EndpointBoxOffice.KEY_TITLE;

/**
 * Created by Varun Ramani on 5/20/2015.
 */
public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(this, "SERVICE STARTED", Toast.LENGTH_LONG).show();
        //jobFinished(jobParameters, false);
        new MyTask(this).doInBackground(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {

        MyService myService;
        private VolleySingleton volleySingleton;
        private ImageLoader imageLoader;
        private RequestQueue requestQueue;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        MyTask(MyService myService) {
            this.myService = myService;
            volleySingleton = VolleySingleton.getsInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            try {
                JSONObject response = sendJsonRequest();
                ArrayList<Movie> listMovies = parseJSONObject(response);
                MyApplication.getWritableDatabase().insertMovies(0,listMovies, true);
            } catch (JSONException e) {
                Log.d("VRR", "JSONException: " + e);
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            //super.onPostExecute(jobParameters);
            myService.jobFinished(jobParameters, false);
        }

        public static String getRequestUrl(int limit) {
            //Log.d("VRR", "Inside getRequestUrl " + limit);
            //return URL_ROTTEN_TOMATOES_BOX_OFFICE + "?apikey=" + MyApplication.API_KEY_ROTTEN_TOMATOES + "&limit=" + limit;
            return UrlEndpoints.URL_BOX_OFFICE
                    + UrlEndpoints.URL_CHAR_QUESTION
                    + UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                    + UrlEndpoints.URL_CHAR_AMPERSAND
                    + UrlEndpoints.URL_PARAM_LIMIT + limit;

        }

        private JSONObject sendJsonRequest() throws JSONException {
            //Log.d("VRR", "Inside sendJsonRequestJsonRequest FragmentBoxOffice");
            JSONObject test = new JSONObject();
            JSONObject response = null;
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(10),
                "Test".toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(), "JSON received: " + response, Toast.LENGTH_LONG).show();
                        Log.d("VRR", "Response: " + response);
                        listMovies = parseJSONObject(response);
                        Log.d("VRR", "ListMovies is: " + listMovies.toString());
                        adapterBoxOffice.setMovieList(listMovies);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VRR", "on Response error" + error.getMessage());
                    }
                });*/

            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    getRequestUrl(30), test,
                    requestFuture, requestFuture);

            requestQueue.add(request);

            try {
                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.d("VRR", "Interrupt: "+e);
            } catch (ExecutionException e) {
                Log.d("VRR", "Execution: " + e);
            } catch (TimeoutException e) {
                Log.d("VRR", "TimeOut: " + e);
            }

            return response;
        }

        private ArrayList<Movie> parseJSONObject(JSONObject response) {

            ArrayList<Movie> listMovies = new ArrayList<>();
            StringBuilder data = new StringBuilder();
            if (response != null || response.length() != 0) {

                try {
                    Log.d("VRR", "inside parseJasonObject if try statement");

                    JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);

                    for (int i = 0; i < arrayMovies.length(); i++) {
                        long id = -1;
                        String title = Constants.NA;
                        String releaseDates = Constants.NA;
                        int audienceScore = -1;
                        String synopsis = Constants.NA;
                        String urlThumbnail = Constants.NA;


                        //Log.d("VRR", "I = " + i + " Length is: " + arrayMovies.length());
                        JSONObject currentMovie = arrayMovies.getJSONObject(i);

                        //Get the ID of the current Movie
                        if (currentMovie.has(KEY_ID) && !currentMovie.isNull(KEY_ID)) {
                            id = currentMovie.getLong(KEY_ID);
                            //Log.d("VRR", "ID is: " + id);
                        }

                        //Get the title of the current Movie
                        if (currentMovie.has(KEY_TITLE) && !currentMovie.isNull(KEY_TITLE)) {
                            title = currentMovie.getString(KEY_TITLE);
                            //Log.d("VRR", "Title is: " + title);
                        }

                        //Get the release date of the current movie
                        if (currentMovie.has(KEY_RELEASE_DATES) && !currentMovie.isNull(KEY_RELEASE_DATES)) {
                            JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);
                            releaseDates = null;

                            if (objectReleaseDates.has(KEY_RELEASE_DATES) && !objectReleaseDates.isNull(KEY_RELEASE_DATES)) {
                                releaseDates = objectReleaseDates.getString(KEY_THEATER);
                                //Log.d("VRR", "inside if releaseDates: " + releaseDates);
                            }

                            //Log.d("VRR", "releaseDates is: " + releaseDates + "  objectReleaseDates.isNull(KEY_RELEASE_DATES): " + objectReleaseDates.isNull(KEY_RELEASE_DATES));
                        }

                        if (currentMovie.has(KEY_RATINGS) && !currentMovie.isNull(KEY_RATINGS)) {

                            JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);


                            if (objectRatings.has(KEY_AUDIENCE_SCORE) && !objectRatings.isNull(KEY_AUDIENCE_SCORE)) {
                                audienceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                            }

                            //Log.d("VRR", "Audience score is: " + audienceScore);
                        }

                        if (currentMovie.has(KEY_SYNOPSIS) && !currentMovie.isNull(KEY_SYNOPSIS)) {
                            synopsis = currentMovie.getString(KEY_SYNOPSIS);

                            //Log.d("VRR", "Synopsis is: " + synopsis);
                        }


                        if (currentMovie.has(KEY_POSTERS) && !currentMovie.isNull(KEY_POSTERS)) {
                            JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);


                            if (objectPosters.has(KEY_THUMBNAIL) && !objectPosters.isNull(KEY_THUMBNAIL)) {
                                urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                            }

                            //Log.d("VRR", "urlThumbnail is: " + urlThumbnail);
                        }

                        Movie movie = new Movie();
                        data.append(id + " " + title + " " + releaseDates + " " + audienceScore + " " + releaseDates + "\n");

                        movie.setId(id);
                        movie.setTitle(title);
                        Date date = null;
                        //if (releaseDates != "NA") {
                        try {
                            date = dateFormat.parse(releaseDates);

                        } catch (ParseException e) {
                            //Toast.makeText(getActivity(), "Exception is: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (NullPointerException e) {
                            Log.d("VRR", "NullException is: " + e.getMessage());
                        }
                        movie.setReleaseDateTheater(date);
                        movie.setAudienceScore(audienceScore);
                        movie.setSynopsis(synopsis);
                        movie.setUrlThumbnail(urlThumbnail);

                        if (id != -1 && !title.equals(Constants.NA)) {
                            listMovies.add(movie);
                        }

                        //data.append(id + " " + title + " " + releaseDates + " " + audienceScore + "\n");
                    }
                    //Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), listMovies.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            } else {
                Log.d("VRR", "RESPONSE NULL");
            }

            //Log.d("VRR", "DATA " + data);
            return listMovies;
        }
    }
}
