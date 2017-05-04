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

public class InstagramGet {

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

	// private static ProgressDialog mProgress;

	public InstagramGet(Context context, InstagramApp instaApp) {
		// TODO Auto-generated constructor stub
		mApp = instaApp;
		// mURL = url;
		// mProgress = new ProgressDialog(C.context);
		// mProgress.setCancelable(false);
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

	private class getRequsetPOST extends AsyncTask<URL, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(URL... params) {
			// TODO Auto-generated method stub
			// HttpURLConnection urlConnection;
			// DataOutputStream wr;
			// OutputStreamWriter osw;
			// try {
			// urlConnection = (HttpURLConnection) params[0].openConnection();
			//
			String para = "action=follow";
			// urlConnection.setRequestMethod("POST");
			// urlConnection.setDoInput(true);
			// urlConnection.setDoOutput(true);
			// urlConnection.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");
			// urlConnection.setRequestProperty("Content-Length",
			// "" + Integer.toString(para.getBytes().length));
			// urlConnection.setRequestProperty("Content-Language", "en-US");
			// // urlConnection.setRequestProperty("access_token=",
			// // mApp.getAccessToken());
			// urlConnection.connect();
			//
			// System.out.println("" + params[0] + "\n"
			// + CommanData.anotherUser.getUName() + "--"
			// + CommanData.anotherUser.getId() + "---");
			// osw = new OutputStreamWriter(urlConnection.getOutputStream());
			// osw.write(para);
			// osw.flush();
			// osw.close();

			// wr = new DataOutputStream(urlConnection.getOutputStream());
			// wr.writeBytes(para);
			// wr.flush();
			// wr.close();

			DefaultHttpClient httpclient;
			HttpPost httpost;
			HttpResponse httpResponse;
			httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			try {
				// if(sendObject!=null)
				httpost = new HttpPost(params[0].toURI());
				httpost.setEntity(new StringEntity(para, "UTF-8"));
				System.out.println("Calling HttpPost");
				httpResponse = httpclient.execute(httpost);
				// receiveObject = parseResponse(httpResponse.getEntity()
				// .getContent());

				// int responseCode = urlConnection.getResponseCode();
				// String rr = urlConnection.getResponseMessage();
				// Log.i("responsecode", "" + responseCode + "\n" + rr);
				// String response = mApp.streamToString(urlConnection
				// .getInputStream());
				String response = mApp.streamToString(httpResponse.getEntity()
						.getContent());
				JSONObject jsonObj = (JSONObject) new JSONTokener(response)
						.nextValue();
				// JSONObject jsonObj = (JSONObject) new JSONObject(
				// "{\"data\":\"india\"}");
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
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject response) {
			// TODO Auto-generated method stub
			super.onPostExecute(response);
			Log.w("getting", "" + response.toString());
			try {
				result.put(response.getJSONObject("data").getString(
						"outgoing_status"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// new getRequest().execute(mURL);
			flag = true;
			mHandler.sendMessage(mHandler.obtainMessage(WHAT_FINALIZE, 0, 0));
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
		} else {
			new getRequsetPOST().execute(mURL);
		}
	}

	public interface OnGetResultListener {
		public abstract void onSuccess(JSONArray result, String request);

		public abstract void onFail(String error, String request);
	}

}
