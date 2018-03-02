package com.moviebox.app.ui.main;

import android.content.Context;

import com.moviebox.app.api.NetworkService;
import com.moviebox.app.di.scope.ActivityScope;
import com.moviebox.app.repository.MainRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 01/03/2018.
 */
@Module
public class MainModule {

    private MainActivity mainActivity;
    public MainModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    MainRepository provideMainRepository(NetworkService networkService) {
        return new MainRepository(networkService);
    }

    @Provides
    @ActivityScope
    MainPresenter provideMainPresenter(MainRepository mainRepository,Context context) {
        return new MainPresenter(mainRepository, context);
    }
}
