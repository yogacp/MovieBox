package com.moviebox.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.moviebox.app.helper.DBHelper;
import com.moviebox.app.helper.DBHelperContract;

/**
 * Created by user on 01/03/2018.
 */

public class DataProvider extends ContentProvider {
    private static final String LOG_TAG = DataProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mDbHelper;
    private static final int FAVORITE = 100;
    private static final int FAVORITE_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBHelperContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, DBHelperContract.FavoriteEntry.TABLE_FAVORITE, FAVORITE);
        matcher.addURI(authority, DBHelperContract.FavoriteEntry.TABLE_FAVORITE + "/#", FAVORITE_WITH_ID);

        return matcher;
    }

    /**
     * Initial new DBHelper
     *  */
    @Override
    public boolean onCreate() {
        Log.d(getClass().getName(), "Content Provider onCreated");
        mDbHelper = new DBHelper(getContext(),"movies.db",1);
        mDbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            // All Flavors selected
            case FAVORITE:{
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBHelperContract.FavoriteEntry.TABLE_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            // Individual flavor based on Id selected
            case FAVORITE_WITH_ID:{
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBHelperContract.FavoriteEntry.TABLE_FAVORITE,
                        projection,
                        DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITE:{
                return DBHelperContract.FavoriteEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITE_WITH_ID:{
                return DBHelperContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE: {
                long _id = db.insert(DBHelperContract.FavoriteEntry.TABLE_FAVORITE, null, contentValues);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = DBHelperContract.FavoriteEntry.buildFlavorsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FAVORITE:
                numDeleted = db.delete(
                        DBHelperContract.FavoriteEntry.TABLE_FAVORITE, selection, selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                numDeleted = db.delete(DBHelperContract.FavoriteEntry.TABLE_FAVORITE,
                        DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
