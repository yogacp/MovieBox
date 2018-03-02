package com.moviebox.app.api;

import com.moviebox.app.api.response.ApiResponse;
import com.moviebox.app.api.response.BaseApiResponse;
import com.moviebox.app.vo.Resources;

/**
 * Created by user on 01/03/2018.
 */

public class ApiWrapper {
    public static <T> Resources<T> fetchApi(ApiResponse<BaseApiResponse<T>> response) {
        if (response.isSuccessful()) {
            if(response.body != null){
                return Resources.success(response.body.results);
            }
        } else {
            return Resources.error(response.errorMessage, null);
        }
        return Resources.error("Somethings Wrong", null);
    }
}
