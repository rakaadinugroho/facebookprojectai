package com.rakaadinugroho.sealmood;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.rakaadinugroho.sealmood.models.Category;
import com.rakaadinugroho.sealmood.models.Chapter;
import com.rakaadinugroho.sealmood.models.Daily;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Raka Adi Nugroho on 4/4/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class BaseApps extends Application {
    public static AtomicInteger CategoryID  = new AtomicInteger();
    public static AtomicInteger DailyID = new AtomicInteger();
    public static AtomicInteger ChapterID   = new AtomicInteger();
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        setUpConfiguration();
        Realm realm = Realm.getDefaultInstance();

        /*
        Facebook SDK Setup
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        /*
        Generate Autoincrement Table
         */

        CategoryID  = getIdByTable(realm, Category.class);
        DailyID = getIdByTable(realm, Daily.class);
        ChapterID   = getIdByTable(realm, Chapter.class);
        realm.close();
    }

    private void setUpConfiguration(){
        RealmConfiguration configuration    = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("sealmood.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0 ) ? new AtomicInteger(results.max("id").intValue()): new AtomicInteger() ;
    }
}
