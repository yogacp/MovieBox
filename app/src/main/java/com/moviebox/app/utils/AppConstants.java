package com.moviebox.app.utils;

import com.moviebox.app.BuildConfig;

/**
 * Created by user on 01/03/2018.
 */

public class AppConstants {
    public static String BASE_URL = "https://api.themoviedb.org/3/";
    public static String TOKEN =  BuildConfig.MOVIEDB_APIKEY;
    public static String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185/";
    public static String BASE_URL_IMAGE_BACKDROP = "http://image.tmdb.org/t/p";

    public interface SORT_TYPE{
        int POPULAR_MOVIES = 0;
        int TOP_RATED_MOVIES = 1;
        int FAVORITE = 2;
    }

    public interface ADAPTER_TYPE{
        int TRAILER_ADAPTER = 0;
        int REVIEW_ADAPTER = 1;
    }

    public interface INTENT_TAG{
        String TAG_SOURCE = "source_tag";
        String TAG_DATA = "data_tag";
    }
}
