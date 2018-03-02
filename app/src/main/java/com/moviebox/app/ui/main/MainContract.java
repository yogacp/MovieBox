package com.moviebox.app.ui.main;

import android.os.Bundle;

import com.moviebox.app.vo.data.Movie;

import java.util.List;

/**
 * Created by user on 01/03/2018.
 */

public interface MainContract {
    interface View {
        void setAdapter(List<Movie> movieList, int typeAdapter);
        void showLoading();
        void hideLoading();
        void checkSourceIntent();
        void setupActivityComponent();
    }

    interface UserActionListener {
        void getMovies(int sortId);
        void loadData();
        void getMoviesLocal(int sortId);
        void onSaveInstanceState(Bundle outState);
        void onRestoreInstanceState(Bundle savedInstanceState);
    }
}
