package com.moviebox.app.ui.detail;

import android.os.Bundle;

import com.moviebox.app.vo.data.Movie;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 01/03/2018.
 */

public interface DetailContract {
    interface View {
        void showData(Movie movie) throws MalformedURLException;
        void showLoading(boolean isLoading);
        <T> void setAllAdapter(List<T> data, int adapterId);
        void isFavorite(boolean stat);
        void backToMain(int source);
        void shareContent(String url);
        void setupActivityComponent();
        void setPalette(String url) throws MalformedURLException;
    }

    interface UserActionListener {
        <T> void getReviewAndTrailerList(String id);
        <T>void setAdapter(HashMap<Integer, List<T>> data);
        void saveFavorite(Movie movie) throws Exception;
        void checkFavorite(long id);
        void urlShareContent();
        void onSaveInstanceState(Bundle outState);
        void onRestoreInstanceState(Bundle savedInstanceState);
    }
}
