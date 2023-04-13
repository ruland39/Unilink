package com.example.unilink.Activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.othersProfileActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

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

        if (data != null) {
            currentUID = data.optString("tuid");
            senderUID = data.optString("cuid");
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            OneSignal.removeNotification(notificationId);

        if (result.getAction().getActionId().equals("waveBackBtn")) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("OPEN_WAVER_PROFILE_APPLICATION");
            intent.putExtra("SENDER_USERID", senderUID);
            intent.putExtra("RECEIVER_USERID", currentUID);
            context.startActivity(intent);
        }
    }
}