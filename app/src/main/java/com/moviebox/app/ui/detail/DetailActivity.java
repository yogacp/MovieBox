package com.moviebox.app.ui.detail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moviebox.app.MovieApp;
import com.moviebox.app.R;
import com.moviebox.app.adapter.ReviewAdapter;
import com.moviebox.app.adapter.TrailerAdapter;
import com.moviebox.app.ui.main.MainActivity;
import com.moviebox.app.utils.AppConstants;
import com.moviebox.app.vo.data.Movie;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 01/03/2018.
 */

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private static final String TAG_REVIEW_STATE = "review_state";
    private static final String TAG_TRAILER_STATE = "trailer_state";

    @Inject
    DetailPresenter detailPresenter;

    @BindView(R.id.detail_movie_description)
    TextView mMovieDescription;
    @BindView(R.id.detail_movie_header)
    ImageView mMovieHeader;
    @BindView(R.id.detail_movie_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.detail_movie_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_movie_poster)
    ImageView mPoster;
    @BindView(R.id.detail_movie_release_date)
    TextView mReleaseDate;
    @BindView(R.id.detail_movie_count_average)
    TextView mCountAverage;
    @BindView(R.id.detail_movie_progress_bar)
    AVLoadingIndicatorView mProgressBar;
    @BindView(R.id.detail_movie_btn_favorite)
    FloatingActionButton mBtnFavorite;
    @BindView(R.id.detail_movie_movie_trailer_list)
    RecyclerView mMovieTrailerRV;
    @BindView(R.id.detail_movie_movie_review_list)
    RecyclerView mReviewRV;

    private DetailContract.UserActionListener mActionListener;
    private TrailerAdapter trailerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ReviewAdapter reviewAdapter;
    private Parcelable mStateTrailer;
    private Parcelable mStateReview;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mActionListener.onRestoreInstanceState(savedInstanceState);
        mStateReview = savedInstanceState.getParcelable(TAG_REVIEW_STATE);
        mStateTrailer = savedInstanceState.getParcelable(TAG_TRAILER_STATE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mActionListener.onSaveInstanceState(outState);
        mStateTrailer = mMovieTrailerRV.getLayoutManager().onSaveInstanceState();
        mStateReview = mReviewRV.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(TAG_TRAILER_STATE, mStateTrailer);
        outState.putParcelable(TAG_REVIEW_STATE, mStateReview);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mStateTrailer != null){
            mMovieTrailerRV.getLayoutManager().onRestoreInstanceState(mStateTrailer);
        }

        if(mStateReview != null){
            mMovieTrailerRV.getLayoutManager().onRestoreInstanceState(mStateReview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setupActivityComponent();
        mActionListener = detailPresenter;
        detailPresenter.setView(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showLoading(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mMovieTrailerRV.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mReviewRV.setLayoutManager(mLinearLayoutManager);

        if(extras != null){
            if(extras.containsKey(AppConstants.INTENT_TAG.TAG_DATA)){
                Movie movie = getIntent().getParcelableExtra(AppConstants.INTENT_TAG.TAG_DATA);
                try {
                    showData(movie);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                mActionListener.getReviewAndTrailerList(String.valueOf(movie.getId()));
                mActionListener.checkFavorite(movie.getId());
                mBtnFavorite.setOnClickListener(v -> {
                    try {
                        mActionListener.saveFavorite(movie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            if(extras.containsKey(AppConstants.INTENT_TAG.TAG_SOURCE)){
                int source = getIntent().getIntExtra(AppConstants.INTENT_TAG.TAG_SOURCE,0);
                backToMain(source);
            }
        }
    }

    @Override
    public void setupActivityComponent() {
        MovieApp.get()
                .getAppComponent()
                .plus(new DetailModule(this))
                .inject(this);
    }

    @Override
    public void showData(Movie movie) throws MalformedURLException {
        if(movie != null){
            mCollapsingToolbarLayout.setTitle(movie.getTitle());
            mMovieDescription.setText(movie.getOverview());
            Picasso.with(getApplicationContext())
                    .load(AppConstants.BASE_URL_IMAGE_BACKDROP+"/w500"+ movie.getBackdropPath())
                    .into(mMovieHeader);

            Log.d(getClass().getName(),AppConstants.BASE_URL_IMAGE_BACKDROP+"/w500"+ movie.getBackdropPath());
            try{
                setPalette(AppConstants.BASE_URL_IMAGE_BACKDROP+"/w342"+movie.getBackdropPath());
            }catch (Exception e){
                Log.e(getClass().getName(), e.getMessage());
            }
            Picasso.with(getApplicationContext())
                    .load(AppConstants.BASE_URL_IMAGE_BACKDROP+"/w342"+movie.getPosterPath())
                    .into(mPoster);
            Log.d(getClass().getName(),AppConstants.BASE_URL_IMAGE_BACKDROP+"/w342"+ movie.getPosterPath());
            mCountAverage.setText(getResources().getString(R.string.movie_vote_average, String.valueOf(movie.getVoteAverage())));
            mReleaseDate.setText(movie.getReleaseDate());
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading(boolean isLoading) {
        if(isLoading){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public <T> void setAllAdapter(List<T> data, int adapterId) {
        switch (adapterId){
            case AppConstants.ADAPTER_TYPE.TRAILER_ADAPTER:
                trailerAdapter = new TrailerAdapter(data, this);
                mMovieTrailerRV.setAdapter(trailerAdapter);
                break;
            case AppConstants.ADAPTER_TYPE.REVIEW_ADAPTER:
                mReviewRV.setVisibility(View.VISIBLE);
                reviewAdapter = new ReviewAdapter(data, this);
                mReviewRV.setAdapter(reviewAdapter);
                break;
        }
    }

    @Override
    public void isFavorite(boolean stat) {
        if(stat){
            mBtnFavorite.setColorFilter(getResources().getColor(R.color.colorAccent));
        }else{
            mBtnFavorite.setColorFilter(getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public void backToMain(int source) {
        switch (source){
            case AppConstants.SORT_TYPE.FAVORITE:
                mToolbar.setNavigationOnClickListener(v -> {
                    Intent i = new Intent(DetailActivity.this, MainActivity.class);
                    i.putExtra("source", AppConstants.SORT_TYPE.FAVORITE);
                    startActivity(i);
                });
                break;

        }
    }

    @Override
    public void shareContent(String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.youtube_video_url,url));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_share:
                mActionListener.urlShareContent();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void setPalette(String url) throws MalformedURLException {
        Task.call(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                try {
                    URL mUrl = new URL(url);
                    HttpURLConnection connection=(HttpURLConnection)mUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input=connection.getInputStream();
                    Bitmap myBitmap= BitmapFactory.decodeStream(input);
                    Palette.from(myBitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch mutedColor = palette.getVibrantSwatch();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mutedColor != null) {
                                mCollapsingToolbarLayout.setBackgroundColor(mutedColor.getRgb());
                                mBtnFavorite.setBackgroundTintList(ColorStateList.valueOf(mutedColor.getRgb()));
                                mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor.getRgb()));
                                getWindow().setStatusBarColor(palette.getDarkMutedColor(mutedColor.getRgb()));
                            }
                        }
                    });
                    return myBitmap;
                } catch (IOException e) {
                    Log.d(getClass().getName(),  e.getMessage());
                    return null;
                }
            }
        }, Task.BACKGROUND_EXECUTOR);
    }
}
