package com.moviebox.app.vo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 01/03/2018.
 */

public class Favorite {
    @SerializedName("id")
    public long id;
    @SerializedName("title")
    public String title;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
