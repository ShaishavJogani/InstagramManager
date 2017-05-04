package unfollower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.unfollower.R;

import utils.C;
import utils.CommanData;
import utils.GlobalLoader;
import utils.InstagramApp;
import utils.InstagramGet;
import utils.ResponseCodes;
import utils.InstagramApp.OAuthAuthenticationListener;
import utils.Parameters;
import utils.Properties;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends Activity {

	C c = C.getinstance();

	ImageButton signUp;
	boolean flag = false;
	public Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indexscreen);

		signUp = (ImageButton) findViewById(R.id.loginInsta);

		signUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				c.mApp.authorize();
			}
		});
		initHandler();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub

				Log.v("Dashboard", "" + msg.what);
				JSONArray result = (JSONArray) msg.obj;

				if (msg.what == ResponseCodes.Following) {
					GlobalLoader.FinishMe();
					try {
						CommanData.userDB.createDatabaseTableEntry(result,
								C.TABLE_FOLLOWING);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new GlobalLoader("Getting followers list...", Home.this,
							Home.this);
					c.Publish(Properties.Followers, true, null);
				}

				if (msg.what == ResponseCodes.Followers) {
					GlobalLoader.FinishMe();

					try {
						CommanData.userDB.createDatabaseTableEntry(result,
								C.TABLE_FOLLOWER);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					startActivity(new Intent(Home.this, Dashboard.class));
					finish();
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				}

				return false;
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		c.mApp = new InstagramApp(Home.this, CommanData.getClientId(),
				CommanData.getClientSecret(), CommanData.getCallbackUrl());
		c.mApp.setListener(listener);
		// database = new Users(getApplicationContext());
		// database.Open();

		c.mGet = new InstagramGet(Home.this, c.mApp);
		c.setUpInstagramGet();
	}

	// OnGetResultListener getFollowingListener = new OnGetResultListener() {
	//
	// @Override
	// public void onSuccess(JSONArray result, String request) {
	// // TODO Auto-generated method stub
	// System.out.println("oye: " + result.length());
	//
	// try {
	//
	// if (request.equals(Properties.Following)) {
	// createDatabaseTableEntry(result, C.TABLE_FOLLOWING);
	// createEntry(Properties.Followers);
	//
	// } else if (request.equals(Properties.Followers)) {
	// createDatabaseTableEntry(result, C.TABLE_FOLLOWER);
	// startActivity(new Intent(getApplicationContext(),
	// Dashboard.class));
	// finish();
	// overridePendingTransition(android.R.anim.fade_in,
	// android.R.anim.fade_out);
	// }
	//
	// // createDatabaseTableEntry(result, C.TABLE_FOLLOWING);
	//
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// }
	//
	// @Override
	// public void onFail(String error, String request) {
	// // TODO Auto-generated method stub
	// Log.v("fail", "" + error);
	// signUp.setEnabled(true);
	// }
	// };

	/*
	 * OnGetResultListener getFollowerListener = new OnGetResultListener() {
	 * 
	 * @Override public void onSuccess(JSONArray result, String request) { //
	 * TODO Auto-generated method stub System.out.println("oye: " +
	 * result.length());
	 * 
	 * try { createDatabaseTableEntry(result, C.TABLE_FOLLOWER); Log.v("output",
	 * CommanData.userDB.getData(C.TABLE_FOLLOWER) .toString() + ""); } catch
	 * (JSONException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); }
	 * 
	 * startActivity(new Intent(getApplicationContext(), Dashboard.class));
	 * finish(); overridePendingTransition(android.R.anim.fade_in,
	 * android.R.anim.fade_out); }
	 * 
	 * @Override public void onFail(String error, String request) { // TODO
	 * Auto-generated method stub Log.v("fail", "" + error);
	 * signUp.setEnabled(true); } };
	 */

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			new GlobalLoader("Getting following list...", Home.this, Home.this);
			c.Publish(Properties.Following, true, null);
			signUp.setEnabled(false);
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
			signUp.setEnabled(true);
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GlobalLoader.FinishMe();
		c.setHandler(handler);
		c.context = Home.this;
		init();
		if (c.mApp.hasAccessToken()) {
			new GlobalLoader("Getting following list...", this, this);
			c.Publish(Properties.Following, true, null);
			signUp.setEnabled(false);
		}
	}

}
