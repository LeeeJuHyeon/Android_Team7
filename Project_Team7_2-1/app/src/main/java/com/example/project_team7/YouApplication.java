package com.example.project_team7;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by LG on 2017-12-06.
 */

public class YouApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
