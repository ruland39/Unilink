package com.example.unilink;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.example.unilink.Activities.BLE.MonitoringActivity;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class UnilinkApplication extends Application implements MonitorNotifier {
	// Simple test application that requires you to understand the
	@Override
	public void onCreate() {
		super.onCreate();
		// Code to create foreground service should be coded here
		// but need to make sure it doesn't scan if it's not logged in
	}

	@Override
	public void didEnterRegion(Region region) {

	}

	@Override
	public void didExitRegion(Region region) {

	}
	@Override
	public void didDetermineStateForRegion(int state, Region region) {

	}

}
