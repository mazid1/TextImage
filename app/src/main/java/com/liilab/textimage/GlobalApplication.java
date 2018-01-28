package com.liilab.textimage;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * Created by Mazid on 23-Jan-18.
 */

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    public static GlobalApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }


}
