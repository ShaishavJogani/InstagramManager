package utils;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import dataItems.Profile;
import database.Users;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class CommanData extends Application {

	static SharedPreferences preferences;
	static SharedPreferences.Editor prefEditor;

//	private static final String CLIENT_ID = "0a287da3421d4bb39a1088fc0458ff97";
//	private static final String CLIENT_SECRET = "167d1142471e4fa78582ade1e16ccdfa";
	private static final String CLIENT_ID = "1d3c9a972bac40aa8eb6bfcd5ee73931";
	private static final String CLIENT_SECRET = "0ddab84f94ed42d6882c48bea28ce501";
	private static final String CALLBACK_URL = "instagram://connect";

	public static Users userDB;

	public static Profile anotherUser;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		preferences = getSharedPreferences("myInstaPreferences", MODE_PRIVATE);
		prefEditor = preferences.edit();

		System.out.println("Application started");

		userDB = new Users(getApplicationContext());
		userDB.Open();

		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);
		// END - UNIVERSAL IMAGE LOADER SETUP

	}

	public static String getClientId() {
		return CLIENT_ID;
	}

	public static String getClientSecret() {
		return CLIENT_SECRET;
	}

	public static String getCallbackUrl() {
		return CALLBACK_URL;
	}

}
