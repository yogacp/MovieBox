package com.moviebox.app.api.response;

/**
 * Created by user on 01/03/2018.
 */

public class BaseApiResponse<T> {
    public int page;
    public int total_results;
    public int total_pages;
    public T results;
}
