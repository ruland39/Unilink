package com.example.unilink;

import android.app.Application;
import android.content.Context;

import com.example.unilink.Activities.OneSignalNotificationOpenHandler;
import com.onesignal.OneSignal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnilinkApplication extends Application{
	private static Context mContext;
	private static ExecutorService executor = Executors.newFixedThreadPool(3);
	private static final String ONESIGNAL_APP_ID = "3e4557a0-48c3-4443-b253-a7c87f2896ff";
	private static final String ONESIGNAL_API_KEY=BuildConfig.ONESIGNAL_API_KEY;

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("YO");
		this.mContext = getApplicationContext();
		OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
		OneSignal.initWithContext(this);
		OneSignal.setNotificationOpenedHandler(new OneSignalNotificationOpenHandler(mContext));
		OneSignal.setAppId(ONESIGNAL_APP_ID);
	}

	public static Context getContext() {
		return mContext;
	}

	public static ExecutorService getExecutor(){
		return executor;
	}

	public static String getOnesignalAppId(){
		return ONESIGNAL_APP_ID;
	}

	public static String getOnesignalApiKey(){
		return ONESIGNAL_API_KEY;
	}
}
