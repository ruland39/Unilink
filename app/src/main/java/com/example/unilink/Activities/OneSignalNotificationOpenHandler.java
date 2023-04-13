package com.example.unilink.Activities;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.othersProfileActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.List;

public class OneSignalNotificationOpenHandler extends Application implements OneSignal.OSNotificationOpenedHandler {
    //private ProfileRowAdapter profileRowAdapter;
    private UnilinkUser currentUAcc;
    private Context context;

    public OneSignalNotificationOpenHandler(Context context) {
        this.context=context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        OSNotificationAction.ActionType actionType = result.getAction().getType();
        JSONObject data = result.getNotification().getAdditionalData();
        int notificationId = result.getNotification().getAndroidNotificationId();
        String currentUID = null, senderUID = null;

        currentUID = data.optString("tuid");
        senderUID = data.optString("cuid");
        Log.d("WaveRequestHandler", "notificationOpened: " + currentUID + " " + senderUID);

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            OneSignal.removeNotification(notificationId);

        if (result.getAction().getActionId() != null)
            if (result.getAction().getActionId().equals("waveBackBtn")) {
//            if (isAppRunning(context, "com.example.unilink")){
                Intent intent = new Intent(context, OthersProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("RETRIEVE_WAVER_PROFILE");
                intent.putExtra("SENDER_USERID", senderUID);
                intent.putExtra("RECEIVER_USERID", currentUID);
                context.startActivity(intent);
//            } else {
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction("OPEN_WAVER_PROFILE_APPLICATION");
//                intent.putExtra("SENDER_USERID", senderUID);
//                intent.putExtra("RECEIVER_USERID", currentUID);
//                context.startActivity(intent);
//            }

        }
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}