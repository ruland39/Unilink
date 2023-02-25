package com.example.unilink.Activities.BLE;

import android.app.IntentService;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.unilink.Models.UnilinkUser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;

import java.nio.ByteBuffer;

public class BeaconService extends IntentService {
    public static final Identifier UNILINK_BEACON_ID = Identifier.fromInt(0x8bc9);
    private Long userID = null;
    private Context currentCtx;
    private DisableBeaconReceiver mDisableReceiver = new DisableBeaconReceiver();

    public BeaconService() {
        super("AltBeaconService");
        // Clearing the default parser list to use a custom parser
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this.currentCtx);
        beaconManager.getBeaconParsers().clear();

    }

    @Override
    public void onCreate(){
        super.onCreate();
        this.currentCtx = getApplicationContext();

        // Set up receiving to shut down Beacon
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.unilink.DISABLE_BEACON");
        registerReceiver(mDisableReceiver, filter);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        userID = intent.getLongExtra("CurrentUid", 0L);
        if (userID == null || userID == 0L) {
            Toast.makeText(currentCtx, "Unable to start Beacon Transmission", Toast.LENGTH_SHORT).show();
            Log.e("com.example.unilink: BeaconService","Unable to start beacon service due to a null Unilink user");
            return;
        }
        // Beacon Layout uses id2 to transmit information
        BeaconParser customBeaconParser = new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-5,i:6-23,p:24-24,d:25-25");
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(userID);
        byte[] uidAsBytes = buffer.array();
        Beacon beacon = new Beacon.Builder()
                .setId1(UNILINK_BEACON_ID.toString())
                .setId2(Identifier.fromBytes(uidAsBytes,0,18,false).toString())
                .setRssi(-55)
                .setTxPower(-59)
                .build();

        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(this.currentCtx, customBeaconParser);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.d("com.example.unilink", "Beacon Advertisement started; Beacon Id: "+ UNILINK_BEACON_ID);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("com.example.unilink", "Beacon Advertisement start failed with error: " + errorCode);
            }
        });
    }

    private class DisableBeaconReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BeaconService.this.stopSelf();
        }
    }
}
