package com.vssnake.potlach;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.vssnake.potlach.main.ConnectionManager;
import com.vssnake.potlach.main.fragments.presenter.GiftCreatorPresenter;
import com.vssnake.potlach.main.fragments.presenter.GiftListPresenter;
import com.vssnake.potlach.main.fragments.presenter.GiftViewerPresenter;
import com.vssnake.potlach.main.fragments.presenter.LoginPresenter;
import com.vssnake.potlach.main.fragments.presenter.NavigationDrawerPresenter;
import com.vssnake.potlach.main.fragments.presenter.SpecialInfoPresenter;
import com.vssnake.potlach.main.fragments.presenter.UserInfoPresenter;
import com.vssnake.potlach.main.fragments.views.FragmentGiftCreator;
import com.vssnake.potlach.main.fragments.views.FragmentGiftViewer;
import com.vssnake.potlach.main.fragments.views.FragmentListGifts;
import com.vssnake.potlach.main.fragments.views.FragmentLogin;
import com.vssnake.potlach.main.fragments.views.FragmentNavigationDrawer;
import com.vssnake.potlach.main.fragments.views.FragmentSpecialInfo;
import com.vssnake.potlach.main.fragments.views.FragmentUserInfo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.inject.Singleton;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import comunication.EasyHttpClient;
import comunication.LocalComunication;
import comunication.RetrofitInterface;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.OkClient;

/**
 * Created by vssnake on 29/10/2014.
 */
@Module(
        injects = {
            MainActivity.class,
                LoginPresenter.class,
                FragmentLogin.class,
                FragmentGiftCreator.class,
                FragmentNavigationDrawer.class,
                FragmentListGifts.class,
                FragmentGiftViewer.class,
                FragmentUserInfo.class,
                FragmentSpecialInfo.class
        },
        library = false,
        complete = false
)
public class ConfigModule{

    private static boolean mlocal = false;
    private static boolean staticConfigChanged = true;

    private final PotlatchApp application;

    public ConfigModule(PotlatchApp application){
        this.application = application;
    }

    public ConnectionManager mConnectionManager;

    public static void setlocal(boolean mlocal) {
        ConfigModule.mlocal = mlocal;
        staticConfigChanged = true;
    }

    @Provides
    ConnectionManager communicationInterface(){

        if (staticConfigChanged){
            staticConfigChanged = false;
            if (mlocal){
                mConnectionManager = new ConnectionManager(new LocalComunication(application
                        .getApplicationContext())
                        ,application.getApplicationContext()) {
                };
            }else{
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("https://192.168.1.108:9993")
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new ApacheClient(new EasyHttpClient()))
                        .build();

                RetrofitInterface service = restAdapter.create(RetrofitInterface.class);
                mConnectionManager = new ConnectionManager(service,
                        application.getApplicationContext());
            }
        }
        return mConnectionManager;







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
    @Provides @Singleton
    UserInfoPresenter userInfoPresenter(MainActivityPresenter mainPresenter){
        return new UserInfoPresenter(mainPresenter);
    }

    @Provides @Singleton
    SpecialInfoPresenter specialInfoPresenter(MainActivityPresenter mainPresenter){
        return new SpecialInfoPresenter(mainPresenter);
    }

}
