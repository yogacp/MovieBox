package com.moviebox.app.utils;

import android.util.Log;

import com.moviebox.app.vo.Resources;
import com.moviebox.app.vo.Status;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by user on 01/03/2018.
 */

public abstract class CustomResourceSubscriber<T> extends ResourceSubscriber<T> {

    protected abstract void onNextAndCompleted(@NonNull T body);

    protected abstract void onError(String errorMessage);

    @Override
    public void onNext(T t) {
        Resources resource = (Resources) t;
        if (resource.status == Status.SUCCESS) {
            onNextAndCompleted(t);
        } else {
            onError(resource.message);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            onError("No internet connection");
        } else {
            Log.d(getClass().getName(), e.getMessage());
            onError("Something's wrong");
        }
    }

    @Override
    public void onComplete() {

    }

}
