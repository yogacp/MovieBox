package com.moviebox.app.di.component;

import com.moviebox.app.di.module.AppModule;
import com.moviebox.app.di.module.NetworkModule;
import com.moviebox.app.ui.detail.DetailComponent;
import com.moviebox.app.ui.detail.DetailModule;
import com.moviebox.app.ui.main.MainActivity;
import com.moviebox.app.ui.main.MainComponent;
import com.moviebox.app.ui.main.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by user on 01/03/2018.
 */

@Singleton
@Component(
        modules = {
                AppModule.class,
                NetworkModule.class
        }
)
public interface AppComponent {
        MainComponent plus(MainModule mainModule);
        DetailComponent plus(DetailModule detailModule);
}
