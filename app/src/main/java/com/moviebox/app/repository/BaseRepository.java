package com.moviebox.app.repository;

import com.moviebox.app.api.NetworkService;

/**
 * Created by user on 01/03/2018.
 */

public class BaseRepository {
    protected NetworkService networkService;
    public BaseRepository(NetworkService networkService) {
        this.networkService = networkService;
    }
}
