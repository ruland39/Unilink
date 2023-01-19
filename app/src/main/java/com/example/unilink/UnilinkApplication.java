package com.example.unilink;

import android.app.Application;
import android.app.Notification;
import android.content.Intent;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class UnilinkApplication extends Application implements MonitorNotifier {

	@Override
	public void onCreate() {
		super.onCreate();
		BeaconManager beaconMgr = BeaconManager.getInstanceForApplication(this);
		beaconMgr.setDebug(true);

		// Setting up foreground service for the application
		Notification.Builder notifBuilder = new Notification.Builder(this);
		notifBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
		notifBuilder.setContentTitle("Scanning for beacons in the background");
		Intent fgService = new Intent(this, )

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