package com.example.unilink;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import com.example.unilink.Activities.BLE.MonitoringActivity;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnilinkApplication extends Application{
	private static Context mContext;
	private static ExecutorService executor = Executors.newFixedThreadPool(3);
	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = getApplicationContext();
	}

	public static Context getContext() {
		return mContext;
	}

	public static ExecutorService getExecutor(){
		return executor;
	}
}
