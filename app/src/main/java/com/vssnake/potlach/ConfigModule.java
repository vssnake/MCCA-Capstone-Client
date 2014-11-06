package com.vssnake.potlach;

import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.presenter.GiftListPresenter;
import com.vssnake.potlach.main.fragments.presenter.GiftViewerPresenter;
import com.vssnake.potlach.main.fragments.presenter.LoginPresenter;
import com.vssnake.potlach.main.fragments.presenter.NavigationDrawerPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.NavigationDrawerFragment;

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
                NavigationDrawerFragment.class,
                FragmentListGifts.class,
                FragmentGiftViewer.class
        },
        library = false,
        complete = false
)
class ConfigModule{

    private final PotlatchApp application;

    public ConfigModule(PotlatchApp application){
        this.application = application;
    }

    @Provides @Singleton
    ConnectionManager communicationInterface(){
        return new LocalComunication(application.getApplicationContext());
    }

    @Provides @Singleton public MainActivityPresenter mainPresenter(
            ConnectionManager connectionManager){
        return new MainActivityPresenter(application,connectionManager);
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
    NavigationDrawerPresenter navigationDrawerPresenter(MainActivityPresenter mainPresenter){
        return new NavigationDrawerPresenter(mainPresenter);
    }

    @Provides @Singleton
    GiftListPresenter giftLIstPresenter(MainActivityPresenter mainPresenter){
        return new GiftListPresenter(mainPresenter);
    }

    @Provides @Singleton
    GiftViewerPresenter giftViewerPresenter(MainActivityPresenter mainPresenter){
        return new GiftViewerPresenter(mainPresenter);
    }

}
