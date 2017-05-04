package utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import utils.InstagramGet.OnGetResultListener;

public class C implements OnGetResultListener {

	private static C c_instatnce;

	private Handler handler;
	public static Handler xhandler;
	public static InstagramApp mApp;
	public static InstagramGet mGet;

	public static Context context;

	public static final String TABLE_FOLLOWER = "follower_list";
	public static final String TABLE_FOLLOWING = "following_list";
	public static final String TABLE_UNFOLLOWER = "unfollower_list";
	public static final String TABLE_TEMP_UPDATE = "temp_table_for_update";

	// Users users;

	public static C getinstance() {
		if (c_instatnce == null)
			c_instatnce = new C();
		return c_instatnce;
	}

	private C() {
		// TODO Auto-generated constructor stub
		// mApp = new InstagramApp(context, CommanData.getClientId(),
		// CommanData.getClientSecret(), CommanData.getCallbackUrl());
		// mGet = new InstagramGet(context, mApp);
		// mGet.setListener(this);
	}

	public void Publish(String requestType, boolean isHTTP_GET, String params) {
		URL url;
		try {
			if (params == null)
				url = new URL(mApp.API_URL + "/users/" + mApp.getId() + "/"
						+ requestType + "?access_token="
						+ mApp.getAccessToken());
			else
				url = new URL(mApp.API_URL + "/users/" + CommanData.anotherUser.getId() + "/"
						+ requestType + "?access_token="
						+ mApp.getAccessToken() );
			Log.v("publishURL", url.toString());
			mGet.requestdata(url, requestType, isHTTP_GET);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ResponseGet(JSONArray result, String requestCase) {
		// TODO Auto-generated method stub
		if (requestCase.equals(Properties.Following)) {
			sendMessage(ResponseCodes.Following, result, true);
		}
		if (requestCase.equals(Properties.Followers)) {
			sendMessage(ResponseCodes.Followers, result, true);
		}
		if (requestCase.equals(Properties.Relationship)) {
			Log.w("request", "msg come from server" + result);
			sendMessage(ResponseCodes.Relationship, result, true);
		}
	}

	private void sendMessage(int Response, JSONArray object, boolean hasObject) {
		// TODO Auto-generated method stub
		if (xhandler != null) {
			Message msg = new Message();
			msg.what = Response;
			if (hasObject)
				msg.obj = object;
			xhandler.sendMessage(msg);
		}
	}

	@Override
	public void onSuccess(JSONArray result, String request) {
		// TODO Auto-generated method stub
		String event = new String(request);
		// Log.v("ServerResponse", "" + event + "\nMessage : " + result);
		ResponseGet(result, event);
	}

	@Override
	public void onFail(String error, String request) {
		// TODO Auto-generated method stub

	}

	public void setUpInstagramGet() {
		mGet.setListener(this);
	}

	public static void setHandler(Handler handler) {
		xhandler = handler;
	}

	public static Handler getHandler() {
		return xhandler;
	}
}
