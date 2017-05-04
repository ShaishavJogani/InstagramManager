package utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class InstagramUsers {

	JSONArray result;
	private final InstagramApp mApp;
	private URL mURL;
	private OnGetResultListener mListener;
	private static int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private static int WHAT_GET = 2;
	private String mRequest_Case;
	private DataOutputStream wr;
	static boolean flag = false;


	public InstagramUsers(Context context, InstagramApp instaApp) {
		// TODO Auto-generated constructor stub
		mApp = instaApp;
	}

	private class getRequest extends AsyncTask<URL, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(URL... params) {
			// TODO Auto-generated method stub
			HttpURLConnection urlConnection;
			try {
				urlConnection = (HttpURLConnection) params[0].openConnection();

				urlConnection.setRequestMethod("GET");

				urlConnection.setDoInput(true);
				urlConnection.connect();
				String response = mApp.streamToString(urlConnection
						.getInputStream());
				JSONObject jsonObj = (JSONObject) new JSONTokener(response)
						.nextValue();
				return jsonObj;
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(JSONObject response) {
			// TODO Auto-generated method stub
			super.onPostExecute(response);
			JSONArray count;
			try {
				count = response.getJSONArray("data");
				Log.w("getting", "" + count.length());
				for (int i = 0; i < count.length(); i++) {
					result.put(count.getJSONObject(i));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (response.has("pagination")) {
					if (response.getJSONObject("pagination").has("next_url")) {
						URL temp = new URL(response.getJSONObject("pagination")
								.getString("next_url"));
						mURL = temp;
						new getRequest().execute(mURL);
					} else {
						if (!flag) {
							flag = true;
							mHandler.sendMessage(mHandler.obtainMessage(
									WHAT_FINALIZE, 0, 0));
						}
						return;
					}
				}
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;

		}

	}

	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_ERROR) {
				// mProgress.dismiss();
				mListener.onFail("Failed to get request", mRequest_Case);
			}
			if (msg.what == WHAT_FINALIZE) {
				// mProgress.dismiss();
				mListener.onSuccess(result, mRequest_Case);
			}
			if (msg.what == WHAT_GET) {
				// getRequest(mURL);
			}
		}
	};

	public void setListener(OnGetResultListener listener) {
		mListener = listener;
	}

	/*
	 * public void setListener(OnGetResultListener listener, URL url, String
	 * request) { mListener = listener; mURL = url; result = new JSONArray();
	 * mProgress.setMessage("Getting data from request ..."); mProgress.show();
	 * flag = false; mRequest_Case = new String(request); new
	 * getRequest().execute(mURL); // getRequest(mURL); }
	 */

	public void requestdata(URL url, String requestendpoint, boolean isHTTP_GET) {
		mURL = url;
		result = new JSONArray();
		flag = false;
		mRequest_Case = new String(requestendpoint);
		if (isHTTP_GET) {
			new getRequest().execute(mURL);
		}
	}

	public interface OnGetResultListener {
		public abstract void onSuccess(JSONArray result, String request);

		public abstract void onFail(String error, String request);
	}

}
