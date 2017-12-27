package com.walawatchlist.www.watchlist.SearchFunctionality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.walawatchlist.www.watchlist.Authentication.LoginActivityOfficial;
import com.walawatchlist.www.watchlist.R;
import com.walawatchlist.www.watchlist.WatchLaterListFunctionality.WatchLaterListActivity;


public class SearchMovies extends Fragment {

    TextClicked mCallback;
    private EditText movieKeywordSearch;
    private Button searchButton;
    private ProgressBar progressBar;

    public SearchMovies() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TextClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void searchTerm(String searchTerm) {

        if (searchTerm.equals("")) {
        } else {
            mCallback.sendText(searchTerm);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_movies, container, false);

        movieKeywordSearch = rootView.findViewById(R.id.searchInputText);
        searchButton = rootView.findViewById(R.id.searchButton);
        progressBar = rootView.findViewById(R.id.progressBarSearching);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchTerm(getSearchTerm());

            }
        });

        Button logoutButton = rootView.findViewById(R.id.buttonLogout);


        final FirebaseAuth user = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.signOut();
                Intent intent = new Intent(getActivity(), LoginActivityOfficial.class);
                startActivity(intent);


            }
        });

        FloatingActionButton watchList = rootView.findViewById(R.id.fab);
        watchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchLaterListActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }


    public String getSearchTerm() {

        toggleProgressBar(true);
        String searchTerm;
        searchTerm = null;

        if ((movieKeywordSearch.getText().toString()).equals("")) {
            toastMessage("Please enter search term!");
            searchTerm = "";
            toggleProgressBar(false);


        } else {
            searchTerm = movieKeywordSearch.getText().toString().replaceAll(" ", "%20");
            toggleProgressBar(true);
        }

        return searchTerm;
    }


    public void toggleProgressBar(Boolean toggle) {

        if (toggle) {
            progressBar.setVisibility(View.VISIBLE);
            movieKeywordSearch.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            movieKeywordSearch.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
        }

    }

    public void toastMessage(String message) {

        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();


    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking
        super.onDetach();
    }

    public interface TextClicked {
        public void sendText(String text);
    }

}
