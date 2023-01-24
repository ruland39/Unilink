package com.example.unilink.Models;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageButton;

import com.example.unilink.R;

public class BluetoothButton extends AppCompatImageButton {
	private static final int[] STATE_DISCOVER = {R.attr.state_discover};
	private static final int[] STATE_CONNECTED = {R.attr.state_connected};
	private static final int[] STATE_OFF = {R.attr.state_off};

	private boolean mIsDiscovering = false;
	private boolean mIsConnected = false;
	private boolean mIsOff = true;

	public BluetoothButton(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}

	@Override
	public int[] onCreateDrawableState(int space){
		final int[] drawableState = super.onCreateDrawableState(space+2);
		if (mIsDiscovering) {
			mergeDrawableStates(drawableState, STATE_DISCOVER);
		}
		if  (mIsConnected) {
			mergeDrawableStates(drawableState, STATE_CONNECTED);
		}
		if (mIsOff){
			mergeDrawableStates(drawableState, STATE_OFF);
		}
		return drawableState;
	}

	public void setConnected() {
		System.out.println("connected");
		this.mIsConnected = true;
		this.mIsDiscovering = false;
		this.mIsOff = false;
	}

	public void setDiscovering() {
		System.out.println("discovering");
		this.mIsConnected = false;
		this.mIsDiscovering = true;
		this.mIsOff = false;
	}

	public void setOff() {
		System.out.println("off");
		this.mIsConnected = false;
		this.mIsDiscovering = false;
		this.mIsOff = true;
	}

	public boolean isConnected() {
		return this.mIsConnected;
	}

	public boolean isDiscovering() {
		return this.mIsDiscovering;
	}

	public boolean isOff() {
		return this.mIsOff;
	}
}
