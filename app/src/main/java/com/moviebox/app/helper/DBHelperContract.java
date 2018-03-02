package com.moviebox.app.helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 01/03/2018.
 */

public class DBHelperContract {

    public static final String CONTENT_AUTHORITY = "com.moviebox.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteEntry implements BaseColumns {
        //Favorite Table
        public static final String TABLE_FAVORITE = "favorite";
        public static final String FAVORITE_COLUMN_MOVIES_ID = "id";
        public static final String FAVORITE_COLUMN_MOVIES_TITLE = "title";
        public static final String FAVORITE_COLUMN_MOVIES_POSTER_PATH = "poster_path";
        public static final String FAVORITE_COLUMN_MOVIES_ORIGINAL_TITLE = "original_title";
        public static final String FAVORITE_COLUMN_MOVIES_BACKDROP_PATH = "backdrop_path";
        public static final String FAVORITE_COLUMN_MOVIES_VOTE_COUNT = "vote_count";
        public static final String FAVORITE_COLUMN_MOVIES_VOTE_AVERAGE = "vote_average";
        public static final String FAVORITE_COLUMN_MOVIES_POPULARITY = "popularity";
        public static final String FAVORITE_COLUMN_MOVIES_ORIGINAL_LANGUAGE = "original_language";
        public static final String FAVORITE_COLUMN_MOVIES_ADULT = "adult";
        public static final String FAVORITE_COLUMN_MOVIES_OVERVIEW = "overview";
        public static final String FAVORITE_COLUMN_MOVIES_RELEASE_DATE = "release_date";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITE).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITE;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
