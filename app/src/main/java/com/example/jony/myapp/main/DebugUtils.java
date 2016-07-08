package com.example.jony.myapp.main;

import android.content.Context;
import android.util.Log;

/**
 * Created by Jony on 2016/6/15.
 */
public class DebugUtils {

    private static boolean DEBUG = true;
    private static Context mContext = BaseApplication.AppContext;

    public static final String TAG ="Debug";

    private DebugUtils() {

    }
    public static void DLog(String text){
        if(DEBUG) {
            Log.d(TAG, text);
        }
    }
}
