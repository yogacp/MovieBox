package com.moviebox.app.ui.main;

import com.moviebox.app.di.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by user on 01/03/2018.
 */

@ActivityScope
@Subcomponent(
        modules = MainModule.class
)
public interface MainComponent {
    MainActivity inject(MainActivity mainActivity);
}
