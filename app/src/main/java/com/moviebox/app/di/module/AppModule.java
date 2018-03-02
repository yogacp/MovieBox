package com.moviebox.app.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 01/03/2018.
 */
@Module
public class AppModule {
    private Application application;
    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return application;
    }
}
