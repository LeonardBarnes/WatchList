package com.walawatchlist.www.watchlist.ViewDetailsFunctionality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walawatchlist.www.watchlist.Models.MovieResultsRepresentation;
import com.walawatchlist.www.watchlist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Leonard on 2017/12/23.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieResultsRepresentation> movieResults;
    private int widthOfTiles;
    private View.OnClickListener clickListenerForViews;

    public ImageAdapter(Context context, ArrayList<MovieResultsRepresentation> results, int movieTileWidth, View.OnClickListener clickListener) {
        this.mContext = context;
        this.movieResults = results;
        this.widthOfTiles = movieTileWidth;
        this.clickListenerForViews = clickListener;
    }

    @Override
    public int getCount() {
        return movieResults.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.movie_layout, parent, false);

            holder = new ViewHolder();
            holder.movieTitle = (TextView) convertView.findViewById(R.id.textView);
            holder.votes = (TextView) convertView.findViewById(R.id.textView2);
            holder.popularity = (TextView) convertView.findViewById(R.id.textViewPopularity);
            holder.imgView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.watchLater = (ImageButton) convertView.findViewById(R.id.imageButton);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        Drawable placeHolderPicture = resizeDrawable(mContext.getResources().getDrawable(R.drawable.loading));
        holder.movieTitle.setText(movieResults.get(position).getMovieName());
        holder.votes.setText("Ratings: " + movieResults.get(position).getVotes());
        holder.popularity.setText("Popularity:  " + new DecimalFormat("#").format(movieResults.get(position).getMoviePopularity()));

        setClickListeners(holder.imgView);
        setClickListeners(holder.watchLater);

        setTagsToViews(holder.imgView, position);
        setTagsToViews(holder.watchLater, position);

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + movieResults.get(position).getMoviePoster()).resize(widthOfTiles, (int) (widthOfTiles * 1.5)).placeholder(placeHolderPicture).into(holder.imgView);

        return convertView;
    }

    private void setTagsToViews(View view, int position) {
        view.setTag(R.id.key_position, position);
    }

    private void setClickListeners(View view) {
        view.setOnClickListener(clickListenerForViews);
    }


    private Drawable resizeDrawable(Drawable image) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, widthOfTiles, (int) (widthOfTiles * 1.5), false);
        return new BitmapDrawable(mContext.getResources(), bitmapResized);
    }

    public static class ViewHolder {
        public TextView movieTitle, votes, popularity;
        private ImageView imgView;
        private ImageButton watchLater;
    }

}


