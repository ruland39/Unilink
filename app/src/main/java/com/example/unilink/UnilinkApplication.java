package com.example.unilink;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import com.example.unilink.Activities.BLE.MonitoringActivity;
import com.onesignal.OneSignal;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnilinkApplication extends Application{
	private static Context mContext;
	private static ExecutorService executor = Executors.newFixedThreadPool(3);
	private static final String ONESIGNAL_APP_ID = "3e4557a0-48c3-4443-b253-a7c87f2896ff";

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("YO");
		this.mContext = getApplicationContext();
		OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
		OneSignal.initWithContext(this);
		OneSignal.setAppId(ONESIGNAL_APP_ID);
		OneSignal.promptForPushNotifications();
	}

	public static Context getContext() {
		return mContext;
	}

	public static ExecutorService getExecutor(){
		return executor;
	}
}
