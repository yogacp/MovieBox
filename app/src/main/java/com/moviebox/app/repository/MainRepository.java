package com.moviebox.app.repository;

import com.moviebox.app.api.ApiWrapper;
import com.moviebox.app.api.NetworkService;
import com.moviebox.app.utils.AppConstants;
import com.moviebox.app.vo.Resources;
import com.moviebox.app.vo.data.Movie;
import com.moviebox.app.vo.data.Review;
import com.moviebox.app.vo.data.Video;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 01/03/2018.
 */

public class MainRepository extends BaseRepository {

    public MainRepository(NetworkService networkService) {
        super(networkService);
    }

    /**
     * Get Movies Data
     * @Param Sorting Type
     * */
    public Flowable<Resources<List<Movie>>> getMovies(String sort) {
        return networkService.getMoviesData(sort, AppConstants.TOKEN)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiWrapper::fetchApi);
    }

    /**
     * Get Movie Trailer Data
     * @Param Movie ID
     * */
    public Flowable<Resources<List<Video>>> getVideos(String id){
        return networkService.getVideoData(id, AppConstants.TOKEN)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiWrapper::fetchApi);
    }

    /**
     * Get Movie Review Data
     * @Param Movie ID
     * */

    public Flowable<Resources<List<Review>>> getReviews(String id){
        return networkService.getReviewData(id, AppConstants.TOKEN)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiWrapper::fetchApi);
    }
}
