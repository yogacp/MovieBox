package com.moviebox.app;

import android.app.Application;
import com.moviebox.app.di.component.AppComponent;
import com.moviebox.app.di.component.DaggerAppComponent;
import com.moviebox.app.di.module.AppModule;
import com.moviebox.app.di.module.NetworkModule;

public class MovieApp extends Application {

    private AppComponent appComponent = createAppComponent();

    private static MovieApp instance;

    public static MovieApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    protected AppComponent createAppComponent() {
        return appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return  appComponent;
    }
}
