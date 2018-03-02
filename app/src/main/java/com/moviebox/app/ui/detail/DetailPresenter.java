package com.moviebox.app.ui.detail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.moviebox.app.helper.DBHelperContract;
import com.moviebox.app.repository.MainRepository;
import com.moviebox.app.utils.AppConstants;
import com.moviebox.app.vo.Resources;
import com.moviebox.app.vo.data.Movie;
import com.moviebox.app.vo.data.Review;
import com.moviebox.app.vo.data.Video;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by user on 01/03/2018.
 */

public class DetailPresenter implements DetailContract.UserActionListener {

    private DetailContract.View mView;
    private MainRepository mainRepository;
    private Context mContext;

    private List<Video> mListTrailer;
    private List<Review> mListReview;
    private String mMovieID  = null;

    public DetailPresenter(MainRepository mainRepository,Context context) {
        this.mainRepository = mainRepository;
        mContext = context;
    }

    public void setView(DetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public <T> void getReviewAndTrailerList(String id) {
        mMovieID = id;
        mView.showLoading(true);
        Flowable.zip(mainRepository.getVideos(id), mainRepository.getReviews(id), new BiFunction<Resources<List<Video>>, Resources<List<Review>>, HashMap<Integer, List<T>>>() {
            @Override
            public HashMap<Integer, List<T>> apply(@NonNull Resources<List<Video>> trailerList, @NonNull Resources<List<Review>> reviewList) throws Exception {
                HashMap<Integer, List<T>> datas = new HashMap<>();
                datas.put(AppConstants.ADAPTER_TYPE.TRAILER_ADAPTER, (List<T>) trailerList.data);
                datas.put(AppConstants.ADAPTER_TYPE.REVIEW_ADAPTER, (List<T>) reviewList.data);
                return datas;
            }
        }).subscribe(new ResourceSubscriber<HashMap<Integer, List<T>>>() {
            @Override
            public void onNext(HashMap<Integer, List<T>> data) {
                mListReview = (List<Review>) data.get(1);
                mListTrailer = (List<Video>) data.get(0);
                setAdapter(data);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(getClass().getName(), t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public <T> void setAdapter(HashMap<Integer, List<T>> data) {
        for(int i = 0; i<data.size();i++){
            mView.setAllAdapter(data.get(i), i);
        }
        mView.showLoading(false);
    }

    @Override
    public void saveFavorite(Movie movie) throws Exception {
        Cursor cursor =
                mContext.getContentResolver().query(Uri.parse(DBHelperContract.FavoriteEntry.CONTENT_URI+"/"+ String.valueOf(movie.getId())),
                        new String[]{DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE},
                        null,
                        null,
                        null);
        if(cursor != null){
            if(cursor.getCount() == 0){
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ID,movie.getId());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE,movie.getTitle());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_TITLE,movie.getOriginalTitle());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POSTER_PATH,movie.getPosterPath());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_BACKDROP_PATH,movie.getBackdropPath());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE,movie.getTitle());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ADULT,movie.isAdult());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_ORIGINAL_LANGUAGE,movie.getOriginalLanguage());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_OVERVIEW,movie.getOverview());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_POPULARITY,movie.getPopularity());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_AVERAGE,movie.getVoteAverage());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_VOTE_COUNT,movie.getVoteCount());
                contentValues.put(DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_RELEASE_DATE,movie.getReleaseDate());
                mContext.getContentResolver().insert(DBHelperContract.FavoriteEntry.CONTENT_URI, contentValues);
                mView.isFavorite(true);
            }else{
                mContext.getContentResolver().delete(Uri.parse(DBHelperContract.FavoriteEntry.CONTENT_URI + "/" + movie.getId()),null,null);
                mView.isFavorite(false);
            }
        }
    }

    @Override
    public void checkFavorite(long id) {
        Cursor cursor =
                mContext.getContentResolver().query(Uri.parse(DBHelperContract.FavoriteEntry.CONTENT_URI+"/"+ String.valueOf(id)),
                        new String[]{DBHelperContract.FavoriteEntry.FAVORITE_COLUMN_MOVIES_TITLE},
                        null,
                        null,
                        null);
        if (cursor != null) {
            if(cursor.getCount() == 0){
                mView.isFavorite(false);
            }else{
                mView.isFavorite(true);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void urlShareContent() {
        if(mListTrailer.size() >0){
            mView.shareContent(mListTrailer.get(0).getKey());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("movie_id", mMovieID);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String movieId = savedInstanceState.getString("movie_id");
        getReviewAndTrailerList(movieId);
    }
}
