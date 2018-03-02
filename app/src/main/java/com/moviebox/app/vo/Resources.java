package com.moviebox.app.vo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.moviebox.app.vo.Status.ERROR;
import static com.moviebox.app.vo.Status.SUCCESS;

/**
 * Created by user on 01/03/2018.
 */

public class Resources<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @NonNull
    public final T data;

    public Resources(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resources<T> success(@Nullable T data) {
        return new Resources<>(SUCCESS, data, null);
    }

    public static <T> Resources<T> error(String msg, @Nullable T data) {
        return new Resources<>(ERROR, data, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resources<?> resource = (Resources<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (message != null ? !message.equals(resource.message) : resource.message != null) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
