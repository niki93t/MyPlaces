package rs.elfak.mosis.nikola.myplaces;

import android.app.Application;
import android.content.Context;

/**
 * Created by Nikola on 4/14/2016.
 */
public class MyPlacesApplication extends Application {
    private static MyPlacesApplication instance;

    public MyPlacesApplication(){
        instance = this;
    }

    public static Context getContext(){
        return instance;
    }
}
