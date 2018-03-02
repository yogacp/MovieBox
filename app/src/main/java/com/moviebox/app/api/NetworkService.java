package com.moviebox.app.api;

import com.moviebox.app.api.response.ApiResponse;
import com.moviebox.app.api.response.BaseApiResponse;
import com.moviebox.app.vo.data.Movie;
import com.moviebox.app.vo.data.Review;
import com.moviebox.app.vo.data.Video;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by user on 01/03/2018.
 */

public interface NetworkService {
    @GET("movie/{sort}")
    Flowable<ApiResponse<BaseApiResponse<List<Movie>>>> getMoviesData(@Path("sort") String sort, @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Flowable<ApiResponse<BaseApiResponse<List<Video>>>> getVideoData(@Path("id") String movieId, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Flowable<ApiResponse<BaseApiResponse<List<Review>>>> getReviewData(@Path("id") String movieId, @Query("api_key") String api_key);
}
