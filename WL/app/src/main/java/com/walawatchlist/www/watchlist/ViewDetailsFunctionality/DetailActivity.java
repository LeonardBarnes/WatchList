package com.walawatchlist.www.watchlist.ViewDetailsFunctionality;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

import com.walawatchlist.www.watchlist.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {



            //Intent Passed by previous activity
            Intent intent = getIntent();



            //Details parameters
            String name = intent.getStringExtra("title");
            String voteAverage = intent.getStringExtra("vote_average");
            String overview = intent.getStringExtra("overview");
            String popularity = intent.getStringExtra("popularity");
            String releaseDate = intent.getStringExtra("release_date");
            Double voteCount = intent.getDoubleExtra("vote_count", 0.0);
            String moviePicture = intent.getStringExtra("Picture");



            //Screen management
            int widthOfMovieTiles=screenManagment();

            createFragment(name,voteAverage,overview,popularity,releaseDate,voteCount,moviePicture,widthOfMovieTiles);



        }


    }

    private int screenManagment() {


        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x / 2;
    }

    private void createFragment(String name, String voteAverage,String overview,String popularity,String releaseDate,double voteCount,String moviePicture,int screenWidth) {

        //Creates new fragment to display a particular movies
        Fragment movieResultsfragment = new DetailsFragment();




        //Parameters to pass to details fragment
        Bundle bundle = new Bundle();
        bundle.putString("title", name);
        bundle.putString("vote_average", voteAverage);
        bundle.putString("overview", overview);
        bundle.putString("popularity", popularity);
        bundle.putString("release_date", releaseDate);
        bundle.putDouble("vote_count", voteCount);
        bundle.putString("Picture", moviePicture);
        bundle.putInt("width", screenWidth);
        movieResultsfragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.detailsActivityContainer, movieResultsfragment).commit();
    }


    @Override
    public void onStart() {
        super.onStart();

        //Setting fragment title
        this.setTitle("Movie Details");
    }
}
