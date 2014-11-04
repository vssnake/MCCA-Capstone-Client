package com.vssnake.potlach;

import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.presenter.LoginPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.manager.RestManager;

import javax.inject.Singleton;

import comunication.ComInterface;
import comunication.LocalComunication;
import dagger.Module;
import dagger.Provides;

/**
 * Created by vssnake on 29/10/2014.
 */
@Module(
        injects = {
            MainActivity.class,
                LoginPresenter.class,
                FragmentLogin.class,
                FragmentGiftCreator.class,
                RestManager.class
        },
        library = true,
        complete = false
)
class ConfigModule{

    private final PotlatchApp application;

    public ConfigModule(PotlatchApp application){
        this.application = application;
    }

    @Provides @Singleton public MainActivityPresenter mainPresenter(){
        return new MainActivityPresenter(application);
    }

    @Provides @Singleton
    LoginPresenter loginPresenter(MainActivityPresenter mainPresenter){
        return new LoginPresenter(mainPresenter);
    }

    @Provides @Singleton
    GiftCreatorPresenter giftCreatorPresenter(MainActivityPresenter mainPresenter){
        return new GiftCreatorPresenter(mainPresenter);
    }
    @Provides @Singleton
    ComInterface comunicatonInterface(PotlatchApp application){
        return new LocalComunication(application.getApplicationContext());
    }
}
