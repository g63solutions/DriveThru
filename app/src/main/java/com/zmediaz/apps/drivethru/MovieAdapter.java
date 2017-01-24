package com.zmediaz.apps.drivethru;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Computer on 12/29/2016.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;

    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(long _id);
    }

    private Cursor mCursor;

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.layout_movie_list_item, viewGroup, false);

        view.setFocusable(true);


        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        /*MOVIE SUMMARY*/
        String poster_path = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER_PATH);
        String original_title = mCursor.getString(MainActivity.INDEX_MOVIE_ORIGINAL_TITLE);

        String mMovieTextView = poster_path + " - " + original_title;

        movieAdapterViewHolder.mMovieTextView.setText(mMovieTextView);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final TextView mMovieTextView;

        MovieAdapterViewHolder(View view) {
            super(view);
            mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            view.setOnClickListener(this);
        }

        //TODO 37 37 37 37 37 37 37 37 37 37 37 377 3 73
        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long columnId = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onClick(columnId);

            /*String movie = mMovieTextView.getText().toString();
            mClickHandler.onClick(movie);*/
        }
    }
}








