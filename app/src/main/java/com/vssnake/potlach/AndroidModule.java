package com.vssnake.potlach;

import android.content.Context;
import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by vssnake on 29/10/2014.
 */
@Module(library = true)
public class AndroidModule {
    private final PotlatchApp application;

    public AndroidModule(PotlatchApp application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }
}