package com.taoze.basic.common.base;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taoze on 2018/3/23.
 */
public class BaseApplication extends Application{

    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void removeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
