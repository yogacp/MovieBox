package com.moviebox.app.ui.detail;

import com.moviebox.app.di.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by user on 01/03/2018.
 */

@ActivityScope
@Subcomponent(
        modules = DetailModule.class
)
public interface DetailComponent {
    DetailActivity inject(DetailActivity detailActivity);
}