package com.example.unilink.Activities.BLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.checkerframework.checker.units.qual.A;

public class MonitoringActivity extends AppCompatActivity implements MonitorNotifier {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		verify();
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

	private void verify() {
		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {

			}
		} catch(RuntimeException e) { // Catches when the method throws a runtime exception
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Bluetooth Low Energy not supported/available");
			alert.setMessage("Sorry, this device does not support Bluetooth LE.");
			alert.setPositiveButton(android.R.string.ok, null);
		}
	}
}