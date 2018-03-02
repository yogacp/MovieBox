package com.moviebox.app.ui.detail;

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
public class DetailModule {

    private DetailActivity detailActivity;

    public DetailModule(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Provides
    @ActivityScope
    DetailActivity provideDetailActivity() {
        return detailActivity;
    }

    @Provides
    @ActivityScope
    MainRepository provideMainRepository(NetworkService networkService) {
        return new MainRepository(networkService);
    }

    @Provides
    @ActivityScope
    DetailPresenter provideDetailPresenter(MainRepository mainRepository,Context context) {
        return new DetailPresenter(mainRepository, context);
    }
}
