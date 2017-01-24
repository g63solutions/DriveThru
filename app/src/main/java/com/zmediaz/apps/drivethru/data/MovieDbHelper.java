package com.zmediaz.apps.drivethru.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Computer on 1/6/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*SPACES AFTER ALL THE GREEN OR THE SYNTAX MONSTER WILL EAT YOU*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +


                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " STRING NOT NULL, " +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
