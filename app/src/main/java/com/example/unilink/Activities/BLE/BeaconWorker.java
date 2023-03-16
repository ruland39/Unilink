package com.example.unilink.Activities.BLE;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.unilink.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class BeaconWorker extends Worker {

    public static final Identifier UNILINK_BEACON_ID = Identifier.fromInt(0x8b9c);
    private BeaconTransmitter mBeaconTransmitter;

    public BeaconWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        BeaconManager mBeaconMgr = BeaconManager.getInstanceForApplication(getApplicationContext());
        mBeaconMgr.getBeaconParsers().clear();
        // Setting up the beacon layout
        BeaconParser customBeaconParser = new BeaconParser().setBeaconLayout(getApplicationContext().getString(R.string.beaconlayout));
        // Applying beacon parser
        mBeaconMgr.getBeaconParsers().add(customBeaconParser);
        mBeaconTransmitter = new BeaconTransmitter(getApplicationContext(), customBeaconParser);
    }

    @NonNull
    @Override
    public Result doWork() {
        UUID userID = UUID.fromString(getInputData().getString("CurrentUid"));
        if (userID == null) {
            Toast.makeText(getApplicationContext(), "Unable to start Beacon Transmission", Toast.LENGTH_SHORT).show();
            Log.e("com.example.unilink: BeaconService","Unable to start beacon service due to a null Unilink user");
            return null;
        }

        // Converting the UUID to bytes
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(userID.getMostSignificantBits());
        bb.putLong(userID.getLeastSignificantBits());

        Log.d("BeaconWorker", "UUID: " + userID + "; Bytes: " + Arrays.toString(bb.array()));
        // Creating beacon
        Beacon beacon = new Beacon.Builder()
                .setId1(UNILINK_BEACON_ID.toString())
                .setId2(Identifier.fromBytes(bb.array(),0,16, false).toString())
                .setTxPower(-55)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();
        mBeaconTransmitter.startAdvertising(beacon);

        return Result.success(new Data.Builder()
                .putBoolean("StartedTransmission",
                        mBeaconTransmitter.isStarted()).build());
    }

    @NonNull
    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();
        String id = context.getString(R.string.beacon_transmission_notif_id);
        String title = context.getString(R.string.beacon_transmission_notif_title);
        String cancel = context.getString(R.string.beacon_transmission_notif_cancel);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        Notification notification = new NotificationCompat.Builder(context, id)
                .setContentTitle(title)
                .setTicker(title)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setOngoing(true)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(1, notification, FOREGROUND_SERVICE_TYPE_LOCATION);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        // Create a Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.beacon_transmission_notif_ChannelTitle);
            String description = getApplicationContext().getString(R.string.beacon_transmission_notif_ChannelDesc);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(getApplicationContext().getString(R.string.beacon_transmission_notif_ChannelId), name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
