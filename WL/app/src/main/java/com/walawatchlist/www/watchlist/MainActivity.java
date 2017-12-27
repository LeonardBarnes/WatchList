package com.walawatchlist.www.watchlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.walawatchlist.www.watchlist.SearchFunctionality.MovieResults;
import com.walawatchlist.www.watchlist.SearchFunctionality.SearchMovies;

public class MainActivity extends AppCompatActivity implements SearchMovies.TextClicked {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If app is opened for the first time, we'll load the search movies fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SearchMovies())
                    .commit();
        }
    }


    @Override
    public void sendText(String text) {

        //implemented interface methods for the main activity to open up movie search result fragment
        //Passes in the users search term

        Fragment movieResultsfragment = new MovieResults();

        Bundle bundle = new Bundle();
        bundle.putString("searchTerm", text);
        movieResultsfragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, movieResultsfragment)
                .addToBackStack("Searching")
                .commit();

    }


}


