package com.moviebox.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moviebox.app.MovieApp;
import com.moviebox.app.R;
import com.moviebox.app.adapter.MovieAdapter;
import com.moviebox.app.utils.AppConstants;
import com.moviebox.app.vo.data.Movie;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 01/03/2018.
 */

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG_SAVE_STATE = "save_state_main";

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.main_recyclerview)
    RecyclerView mMovieRV;
    @BindView(R.id.main_progress_bar)
    AVLoadingIndicatorView mProgressBar;
    @BindView(R.id.main_no_data)
    TextView mMainNoData;

    private MainContract.UserActionListener mActionListener;
    private MovieAdapter movieAdapter;
    private GridLayoutManager mGridLayoutManager;
    private Parcelable mState;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mState = mMovieRV.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(TAG_SAVE_STATE, mState);
        mActionListener.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mState = savedInstanceState.getParcelable(TAG_SAVE_STATE);
            mActionListener.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mState != null) {
            mMovieRV.getLayoutManager().onRestoreInstanceState(mState);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActivityComponent();
        mActionListener = mainPresenter;
        mainPresenter.setView(this);
        mActionListener.loadData();
        checkSourceIntent();
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMovieRV.setLayoutManager(mGridLayoutManager);
    }

    @Override
    public void setupActivityComponent() {
        MovieApp.get()
                .getAppComponent()
                .plus(new MainModule(this))
                .inject(this);
    }

    @Override
    public void setAdapter(List<Movie> movies, int typeAdapter) {
        if(movies.size() > 0){
            mMainNoData.setVisibility(View.GONE);
            mMovieRV.setVisibility(View.VISIBLE);
            movieAdapter = new MovieAdapter(movies, this, typeAdapter);
            mMovieRV.setAdapter(movieAdapter);
        }else{
            mMainNoData.setVisibility(View.VISIBLE);
            mMovieRV.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_most_popular:
                mActionListener.getMovies(AppConstants.SORT_TYPE.POPULAR_MOVIES);
                return true;
            case R.id.sort_top_rated:
                mActionListener.getMovies(AppConstants.SORT_TYPE.TOP_RATED_MOVIES);
                return true;
            case R.id.sort_favorite:
                mActionListener.getMovies(AppConstants.SORT_TYPE.FAVORITE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void checkSourceIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey(AppConstants.INTENT_TAG.TAG_SOURCE)) {
                int mSource = getIntent().getIntExtra(AppConstants.INTENT_TAG.TAG_SOURCE,0);
                switch (mSource){
                    case AppConstants.SORT_TYPE.FAVORITE:
                        mActionListener.getMovies(AppConstants.SORT_TYPE.FAVORITE);
                        break;
                }
            }
        }
    }
}
