package pl.lantkowiak.sdm.di;

import android.app.Application;
import android.content.Context;

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
public class ApplicationContext extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
