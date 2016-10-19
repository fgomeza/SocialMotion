package com.globales.socialmotion;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by Francisco on 24-May-16.
 */
public class SocialMotion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
        configuration.locale = new Locale("es", "ES");
        Resources.getSystem().updateConfiguration(configuration, null);
    }
}
