package com.walawatchlist.www.watchlist.SearchFunctionality;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.walawatchlist.www.watchlist.Models.MovieResultsRepresentation;
import com.walawatchlist.www.watchlist.R;
import com.walawatchlist.www.watchlist.ViewDetailsFunctionality.DetailActivity;
import com.walawatchlist.www.watchlist.ViewDetailsFunctionality.ImageAdapter;
import com.walawatchlist.www.watchlist.WatchLaterListFunctionality.WatchLaterListActivity;

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
import java.util.Arrays;


public class MovieResults extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static GridView gridview;
    private static int width;
    private static ArrayList<MovieResultsRepresentation> searchResults;
    private static String API_KEY = "7e719bfe3cd3786ebf0a05d3b138853d";
    private String searchTerm;

    public MovieResults() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_results, container, false);


        //Passed search term
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchTerm = bundle.getString("searchTerm", "");
        }


        FloatingActionButton watchList = rootView.findViewById(R.id.fab);
        watchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchLaterListActivity.class);
                startActivity(intent);
            }
        });


        //This block of code determines the number of movie results in a row
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        //end

        //Setting gridview for displaying results
        if (getActivity() != null) {
            ArrayList<MovieResultsRepresentation> array = new ArrayList<MovieResultsRepresentation>();
            ImageAdapter adapter = new ImageAdapter(getActivity(), array, width, this
            );
            gridview = (GridView) rootView.findViewById(R.id.MovieGridView);
            gridview.setColumnWidth(width);
            gridview.setAdapter(adapter);
        }

        gridview.setOnItemClickListener(this);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        //Setting fragment title
        getActivity().setTitle("Movie Search Results");

        //Fetching search results on another thread
        new ImageLoadTask().execute();
    }

    public void toastMessage(String message) {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();

    }


    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag(R.id.key_position);
        if (v.getId() == R.id.imageButton) {
            //toastMessage(searchResults.get(position).getMovieName());
            //firebase
        } else if (v.getId() == R.id.imageView) {
            //toastMessage(searchResults.get(position).getMovieOverview());
            //intent
        }

    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }


    public class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<MovieResultsRepresentation>> implements View.OnClickListener,
            AdapterView.OnItemClickListener {

        /**
         * This class fetches search results from api on another thread
         **/

        @Override
        protected ArrayList<MovieResultsRepresentation> doInBackground(Void... voids) {
            while (true) {
                try {
                    searchResults = new ArrayList(Arrays.asList(movieResultsFromAPI()));
                    return searchResults;
                } catch (Exception e) {
                    continue;
                }
            }


        }


        @Override
        protected void onPostExecute(ArrayList<MovieResultsRepresentation> results) {
            super.onPostExecute(results);

            if (results != null && getActivity() != null) {

                ImageAdapter adapter = new ImageAdapter(getActivity(), results, width, this);
                gridview.setAdapter(adapter);

            }
        }

        public MovieResultsRepresentation[] movieResultsFromAPI() {
            while (true) {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String JSONResult;

                try {


                    //Constructing api url call
                    String urlString = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + searchTerm;


                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();


                    //Reading input stream into a string
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {

                        return null;
                    }

                    reader = new BufferedReader((new InputStreamReader(inputStream)));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");

                    }

                    if (buffer.length() == 0) {

                        return null;
                    }

                    JSONResult = buffer.toString();


                    try {

                        return getMovieListFromJSON(JSONResult);
                    } catch (JSONException e) {
                        return null;
                    }
                } catch (Exception e) {
                    continue;
                } finally {
                    if (urlConnection != null) {

                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {

                            reader.close();
                        } catch (final IOException e) {
                        }


                    }

                }


            }
        }


        public MovieResultsRepresentation[] getMovieListFromJSON(String jsonResult) throws JSONException {

            JSONObject JSONString = new JSONObject(jsonResult);

            JSONArray moviesArray = JSONString.getJSONArray("results");

            MovieResultsRepresentation[] movies = new MovieResultsRepresentation[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                String moviePAth = movie.getString("poster_path");
                String movieName = movie.getString("title");
                double movieVote = movie.getDouble("vote_average");
                String movieOverview = movie.getString("overview");
                double moviePopularity = movie.getDouble("popularity");
                String movieReleaseDate = movie.getString("release_date");
                double movieVoteCount = movie.getDouble("vote_count");
                movies[i] = new MovieResultsRepresentation(movieName, moviePAth, movieVote, movieOverview, movieReleaseDate, movieVoteCount, moviePopularity, false);
            }

            return movies;

        }

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag(R.id.key_position);
            if (v.getId() == R.id.imageButton) {


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                FirebaseAuth user = FirebaseAuth.getInstance();

                mDatabase.child("users").child(user.getUid()).push().setValue(searchResults.get(position), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        toastMessage("Added to watch later list.");
                    }
                });

                //firebase
            } else if (v.getId() == R.id.imageView) {

                //intent
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("title", searchResults.get(position).getMovieName())
                        .putExtra("vote_average", searchResults.get(position).getVotes() + "")
                        .putExtra("overview", searchResults.get(position).getMovieOverview())
                        .putExtra("popularity", searchResults.get(position).getMoviePopularity() + "")
                        .putExtra("release_date", searchResults.get(position).getMovieReleaseDate())
                        .putExtra("vote_count", searchResults.get(position).getMovieVoteCount())
                        .putExtra("Picture", searchResults.get(position).getMoviePoster());

                startActivity(intent);
            }

        }

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        }
    }


}
