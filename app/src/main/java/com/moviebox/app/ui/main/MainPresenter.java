package com.moviebox.app.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.moviebox.app.helper.DBHelperContract;
import com.moviebox.app.repository.MainRepository;
import com.moviebox.app.utils.CustomResourceSubscriber;
import com.moviebox.app.vo.Resources;
import com.moviebox.app.vo.data.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by user on 01/03/2018.
 */

public class MainPresenter implements MainContract.UserActionListener {

    private MainContract.View mView;
    private MainRepository mainRepository;
    private Context mContext;
    private String mSortType = null;
    private static int mTypeAdapter = 0;

    public MainPresenter(MainRepository mainRepository, Context context) {
        this.mainRepository = mainRepository;
        mContext = context;
    }

    public void setView(MainContract.View mView) {
        this.mView = mView;
    }

    private void getMoviesData(String sort_type, int typeAdapter) {
        mSortType = sort_type;
        mTypeAdapter = typeAdapter;
        mView.showLoading();
        mainRepository.getMovies(sort_type)
                .subscribe(new CustomResourceSubscriber<Resources<List<Movie>>>() {
                    @Override
                    protected void onNextAndCompleted(@NonNull Resources<List<Movie>> body) {
                        mView.setAdapter(body.data, typeAdapter);
                        mView.hideLoading();
                    }

                    @Override
                    protected void onError(String errorMessage) {
                        Log.e(getClass().getName(), errorMessage);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getMovies(int sortId) {
        switch (sortId) {
            case 0:
                getMoviesData("popular", sortId);
                break;
            case 1:
                getMoviesData("top_rated", sortId);
                break;
            case 2:
                getMoviesLocal(sortId);
                break;
        }
    }

    @Override
    public void getMoviesLocal(int sortId) {
        mView.hideLoading();
        mSortType = "local_sort";
        mTypeAdapter = sortId;
        Cursor cursor =
                mContext.getContentResolver().query(
                        DBHelperContract.FavoriteEntry.CONTENT_URI,
                        new String[]{DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ID,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_BACKDROP_PATH,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_TITLE,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ADULT,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_LANGUAGE,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_OVERVIEW,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POPULARITY,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_RELEASE_DATE,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_AVERAGE,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_COUNT,
                                DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POSTER_PATH,},
                        null,
                        null,
                        null);

        String movieId;
        String movieTitle;
        String movieBackDropPath;
        String movieOriginalTitle;
        String moviePosterPath;
        String movieOverview;
        String movieVoteAverage;
        String movieVoteCount;
        String movieReleaseDate;
        String moviePopularity;
        String movieOriginalLanguage;
        List<Movie> movieList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                Movie movie = new Movie();
                movieId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ID));
                movieTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE));
                movieBackDropPath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_BACKDROP_PATH));
                movieOriginalTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_TITLE));
                moviePosterPath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POSTER_PATH));
                movieOverview = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_OVERVIEW));
                movieVoteAverage = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_AVERAGE));
                movieVoteCount = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_COUNT));
                movieReleaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_RELEASE_DATE));
                moviePopularity = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POPULARITY));
                movieOriginalLanguage = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_LANGUAGE));
                movie.setId(Long.valueOf(movieId));
                movie.setTitle(movieTitle);
                movie.setBackdropPath(movieBackDropPath);
                movie.setPosterPath(moviePosterPath);
                movie.setOriginalTitle(movieOriginalTitle);
                movie.setOverview(movieOverview);
                movie.setVoteAverage(Float.valueOf(movieVoteAverage));
                movie.setVoteCount(Integer.parseInt(movieVoteCount));
                movie.setReleaseDate(movieReleaseDate);
                movie.setPopularity(Float.valueOf(moviePopularity));
                movie.setOriginalLanguage(movieOriginalLanguage);
                movieList.add(movie);
                cursor.moveToNext();
            }
        }

        mView.setAdapter(movieList, sortId);
        mTypeAdapter = 2;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sort_type", mSortType);
        outState.putInt("adapter_type", mTypeAdapter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String sortType  = savedInstanceState.getString("sort_type");
        int adapterType = savedInstanceState.getInt("adapter_type", 0);
        Log.d(getClass().getName(), sortType + String.valueOf(adapterType));
        if(sortType.equals("local_sort")){
            mTypeAdapter =2 ;
            getMoviesLocal(2);
        }else{
            getMoviesData(sortType, adapterType);
        }

    }

    @Override
    public void loadData() {
        if(mTypeAdapter == 2){
            getMoviesLocal(mTypeAdapter);
        }else{
            getMovies(mTypeAdapter);
        }

    }
}
