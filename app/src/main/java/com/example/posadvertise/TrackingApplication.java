package com.example.posadvertise;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.posadvertise.screensaver.util.UserInteractionCallback;
import com.posadvertise.util.POSAdvertiseApplication;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TrackingApplication extends POSAdvertiseApplication implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ActivityTracking";

    public boolean isDebugMode(){
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebugMode()) {
            listRunningServices();
            registerActivityLifecycleCallbacks(this);
        }
    }

    private void listRunningServices() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(Integer.MAX_VALUE);
        if(serviceList.size() > 0) {
            Log.d(TAG, "-------------------------------------------------");
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                Log.d(TAG, "Services : " + service.service.getClassName());
            }
            Log.d(TAG, "-------------------------------------------------");
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (isDebugMode()) {
            try {
                //Log Activity
                Log.d(TAG, "-------------------------------------------------");
                getPreviousCallingActivity(activity);
                Log.d(TAG, activity.getClass().getSimpleName());
                activity.getWindow().setCallback(new UserInteractionCallback(activity, new UserInteractionCallback.UserInteraction() {
                    @Override
                    public void onUserInteraction() {

                    }
                }));
                FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();
                fm.addFragmentOnAttachListener(new FragmentOnAttachListener() {
                    @Override
                    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
                        Log.d(TAG, "Attached : " + fragment.getClass().getSimpleName());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void getPreviousCallingActivity(Activity activity) {
        try {
            ComponentName componentName = activity.getCallingActivity();
            if (componentName == null) {
                return;
            }
            Log.d(TAG, "CallingFrom : " + componentName.getShortClassName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public static void log(@NotNull String message) {
        Log.d(TAG,  message);
    }
}
