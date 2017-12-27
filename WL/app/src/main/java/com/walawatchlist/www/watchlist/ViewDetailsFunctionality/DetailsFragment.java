package com.walawatchlist.www.watchlist.ViewDetailsFunctionality;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walawatchlist.www.watchlist.R;
import com.walawatchlist.www.watchlist.WatchLaterListFunctionality.WatchLaterListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private String name;
    private String voteAverage;
    private String overview;
    private String popularity;
    private String releaseDate;
    private Double voteCount;
    private String moviePicture;
    private int width;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Passed search term
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            name = bundle.getString("title", "N/A");
            voteAverage = bundle.getString("vote_average", "N/A");
            overview = bundle.getString("overview", "N/A");
            popularity = bundle.getString("popularity", "N/A");
            releaseDate = bundle.getString("release_date", "N/A");
            voteCount = bundle.getDouble("vote_count");
            moviePicture = bundle.getString("Picture", "N/A");
            width = bundle.getInt("width");
        }

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView movieTitle = rootView.findViewById(R.id.movieTitle);
        TextView movieReleaseDate = rootView.findViewById(R.id.movieReleaseDate);
        TextView movieRating = rootView.findViewById(R.id.movieRating);
        TextView movieOverview = rootView.findViewById(R.id.movieOverview);
        TextView movieVoteCount = rootView.findViewById(R.id.movieVoteCount);
        ImageView movieImageLink = rootView.findViewById(R.id.movieImage);

        FloatingActionButton watchList = rootView.findViewById(R.id.fab2);
        watchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WatchLaterListActivity.class);
                startActivity(intent);
            }
        });


        movieTitle.setText(name);
        movieReleaseDate.setText(releaseDate);
        movieRating.setText("Rating: " + voteAverage + "/10");
        movieOverview.setText(overview);
        movieVoteCount.setText("Vote count: " + voteCount);

        Drawable placeHolderPicture = resizeDrawable(getContext().getResources().getDrawable(R.drawable.loading));

        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + moviePicture).resize(width, (int) (width * 1.5)).placeholder(placeHolderPicture).into(movieImageLink);


        // Inflate the layout for this fragment
        return rootView;


    }


    private Drawable resizeDrawable(Drawable image) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, (int) (width * 1.5), false);
        return new BitmapDrawable(getContext().getResources(), bitmapResized);
    }

}
