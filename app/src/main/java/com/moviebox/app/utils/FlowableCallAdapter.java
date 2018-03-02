package com.moviebox.app.utils;

import android.util.Log;

import com.moviebox.app.api.response.ApiResponse;

import org.reactivestreams.Subscriber;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 01/03/2018.
 */

public class FlowableCallAdapter<R> implements CallAdapter<R, Flowable<ApiResponse<R>>> {

    private final String TAG = FlowableCallAdapter.class.getSimpleName();
    private final Type responseType;

    public FlowableCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Flowable<ApiResponse<R>> adapt(final Call<R> call) {
        return new Flowable<ApiResponse<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void subscribeActual(final Subscriber<? super ApiResponse<R>> s) {
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            Log.d(TAG, "onResponse");
                            s.onNext(new ApiResponse<>(response));
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            Log.d(TAG, "onFailure");
                            s.onError(throwable);
                        }
                    });
                }
            }
        };
    }
}
