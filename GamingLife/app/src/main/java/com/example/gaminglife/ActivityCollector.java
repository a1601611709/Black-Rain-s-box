package com.example.gaminglife;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by black_rain on 2018/5/29.
 */

public class ActivityCollector {

    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
}
