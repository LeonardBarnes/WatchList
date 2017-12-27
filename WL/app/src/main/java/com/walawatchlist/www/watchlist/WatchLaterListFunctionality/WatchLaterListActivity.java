package com.walawatchlist.www.watchlist.WatchLaterListFunctionality;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.walawatchlist.www.watchlist.Models.MovieResultsRepresentation;
import com.walawatchlist.www.watchlist.R;
import com.walawatchlist.www.watchlist.ViewDetailsFunctionality.DetailActivity;

import java.text.DecimalFormat;


public class WatchLaterListActivity extends AppCompatActivity {

    private static int width;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myAccountReference = database.getReference("users");
    private TextView noMoviesSaved;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private FirebaseRecyclerAdapter<MovieResultsRepresentation, MyMovieViewHolder> adapter;


    //private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @SuppressWarnings("ConstantConditions")
    //private final String uid = firebaseAuth.getCurrentUser().getUid();
    //private String current_subject_Selected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later_list);


        Context context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noMoviesSaved = (TextView) findViewById(R.id.noMovieSaved);
        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.myMovieList);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }

        //This determines the number of movie results in a row
        width = screenMangement();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Gets current logged in user
        FirebaseAuth user = FirebaseAuth.getInstance();

        adapter = new FirebaseRecyclerAdapter<MovieResultsRepresentation, MyMovieViewHolder>(
                MovieResultsRepresentation.class,
                R.layout.movieitem,
                MyMovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                myAccountReference.child(user.getUid()).getRef()
        ) {

            @Override
            public MyMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitem, parent, false);
                return new MyMovieViewHolder(view);
            }


            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(position);
            }


            @Override
            protected void populateViewHolder(final MyMovieViewHolder viewHolder, final MovieResultsRepresentation model, final int position) {

                //Loads movie poster into imageview
                Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + model.getMoviePoster()).resize(width, (int) (width * 1.5)).into(viewHolder.moviePicture);


                viewHolder.moviePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When user clicks on movie for more details, it passes the data to the details activity
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class)
                                .putExtra("title", model.getMovieName())
                                .putExtra("vote_average", model.getVotes() + "")
                                .putExtra("overview", model.getMovieOverview())
                                .putExtra("popularity", model.getMoviePopularity() + "")
                                .putExtra("release_date", model.getMovieReleaseDate())
                                .putExtra("vote_count", model.getMovieVoteCount())
                                .putExtra("Picture", model.getMoviePoster());

                        startActivity(intent);

                    }
                });


                viewHolder.movieName.setText(model.getMovieName());


                viewHolder.movieRating.setText("Rating: " + model.getVotes() + "");

                viewHolder.moviePopularity.setText("Popularity: " + new DecimalFormat("#").format(model.getMoviePopularity()));

                viewHolder.movieDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Deletes movie from myWatchLater list and from the cloud

                        getRef(viewHolder.getAdapterPosition()).removeValue();

                        adapter.notifyDataSetChanged();

                        toastMessage("Movie removed");
                    }
                });
            }
        };

        RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (adapter.getItemCount() > 0) {
                    noMoviesSaved.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();


                } else {
                    noMoviesSaved.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);


                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if (adapter.getItemCount() > 0) {
                    noMoviesSaved.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);


                } else {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    noMoviesSaved.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.registerAdapterDataObserver(mObserver);

        mRecyclerView.setAdapter(adapter);


        myAccountReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                if (dataSnapshot.hasChildren()) {
                    mRecyclerView.setVisibility(View.VISIBLE);

                } else {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    noMoviesSaved.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                toastMessage("Error occured");

            }
        });

    }

    private int screenMangement() {

        int width;

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        width = size.x;
        return width;
    }


    public void toastMessage(String message) {
        //Displays toasts  upon actions taken
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        this.setTitle("My watch later list");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }


    public static class MyMovieViewHolder extends RecyclerView.ViewHolder {

        //Viewholder for views displayed in watchLater list

        private ImageView moviePicture;
        private TextView movieName;
        private TextView movieRating;
        private TextView moviePopularity;

        private ImageButton movieDelete;


        private MyMovieViewHolder(View v) {
            super(v);
            moviePicture = (ImageView) v.findViewById(R.id.imageView);
            movieName = (TextView) v.findViewById(R.id.textView);
            movieRating = (TextView) v.findViewById(R.id.textView2);
            movieDelete = (ImageButton) v.findViewById(R.id.imageButton);
            moviePopularity = (TextView) v.findViewById(R.id.textViewPopularity);


        }
    }


}