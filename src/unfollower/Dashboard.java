package unfollower;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.unfollower.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import dataItems.Profile;

import utils.C;
import utils.CommanData;
import utils.GlobalLoader;
import utils.Parameters;
import utils.Properties;
import adapters.Adapter_List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends Activity implements OnClickListener,
		OnItemClickListener {

	// InstagramApp mApp;
	C c = C.getinstance();

	ListView list;
	ImageView selfPic;
	TextView selfName, selfUname, selfFollower, selfFollwing, logout,
			unfollower;
	LinearLayout followBox, followingBox;

	ArrayList<Profile> followList, followingList;
	ArrayList<Profile> displayList;
	JSONArray followers;
	JSONArray following;

	List<Profile> fo, ing, unfollowerList;

	Adapter_List AdapterList;

	public interface RelationBtnClickListener {
		public abstract void onBtnClick(int position);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		findViewByIds();
		try {
			init();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// displayList = new ArrayList<Profile>();
		// Adapter = new Adapter_List(getApplicationContext(), displayList);
		setAdapter((ArrayList<Profile>) fo);
	}

	private void setAdapter(final ArrayList<Profile> temp) {
		// TODO Auto-generated method stub
		// displayList.clear();
		// displayList = new ArrayList<Profile>(temp);
		AdapterList = new Adapter_List(getApplicationContext(), temp,
				new RelationBtnClickListener() {

					@Override
					public void onBtnClick(int position) {
						// TODO Auto-generated method stub
						CommanData.anotherUser = temp.get(position);
						c.Publish(Properties.Relationship, false,
								"action=unfollow");
					}
				});
		list.setAdapter(AdapterList);
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.lvFollowers);
		selfPic = (ImageView) findViewById(R.id.ivSelfPic);
		selfName = (TextView) findViewById(R.id.tvSelfName);
		selfUname = (TextView) findViewById(R.id.tvSelfUName);
		selfFollwing = (TextView) findViewById(R.id.tvSelfFollowing);
		selfFollower = (TextView) findViewById(R.id.tvSelfFollowers);
		followBox = (LinearLayout) findViewById(R.id.llFollowBox);
		followingBox = (LinearLayout) findViewById(R.id.llFollowingBox);
		logout = (TextView) findViewById(R.id.tvLogOUT);
		unfollower = (TextView) findViewById(R.id.tvunfollower);

		logout.setOnClickListener(this);
		followBox.setOnClickListener(this);
		followingBox.setOnClickListener(this);
		unfollower.setOnClickListener(this);

		list.setOnItemClickListener(this);

	}

	private void init() throws JSONException {
		// TODO Auto-generated method stub

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showImageOnLoading(R.drawable.profile1)
				.showImageForEmptyUri(R.drawable.profile1)
				.showImageOnFail(R.drawable.profile1).build();

		selfName.setText(c.mApp.getName());
		selfUname.setText(c.mApp.getUserName());
		selfFollower.setText(c.mApp.getFollowers());
		selfFollwing.setText(c.mApp.getFollwing());
		Log.v("Dashboard", "" + c.mApp.getProfilePic());
		ImageLoader.getInstance().displayImage(c.mApp.getProfilePic(), selfPic,
				defaultOptions);

		// database = new Users(getApplicationContext());
		// database.Open();

		followList = new ArrayList<Profile>();
		followingList = new ArrayList<Profile>();

		followers = new JSONArray();
		following = new JSONArray();

		// followers = database.getData(C.TABLE_FOLLOWER);
		// following = database.getData(C.TABLE_FOLLOWING);

		fo = CommanData.userDB.getData(C.TABLE_FOLLOWER);
		ing = CommanData.userDB.getData(C.TABLE_FOLLOWING);
		unfollowerList = CommanData.userDB.getUnfollower();

		// initialListPrepare(followers, followList);
		// initialListPrepare(following, followingList);

	}

	private void initialListPrepare(JSONArray data, ArrayList<Profile> list)
			throws JSONException {
		for (int i = 0; i < data.length(); i++) {
			String id = data.getJSONObject(i).getString("ID");
			String name = data.getJSONObject(i).getString("NAME");
			String user_name = data.getJSONObject(i).getString("UNAME");
			String profilePhoto = data.getJSONObject(i).getString("PROFILE");
			list.add(new Profile(id, name, user_name, profilePhoto,
					Parameters.noneRelation));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(Dashboard.this, "Item click " + position,
				Toast.LENGTH_SHORT).show();
		Log.v("ItemClick", "you had clicked on item " + position);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvLogOUT:
			logoutFromInsta();

			break;
		case R.id.llFollowBox:
			setAdapter((ArrayList<Profile>) fo);
			Log.v("flo", "follow call");
			break;
		case R.id.llFollowingBox:
			setAdapter((ArrayList<Profile>) ing);
			Log.v("flo", "following call");
			break;
		case R.id.tvunfollower:
			setAdapter((ArrayList<Profile>) unfollowerList);
			break;
		}
	}

	private void logoutFromInsta() {
		// TODO Auto-generated method stub
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				Dashboard.this);
		builder.setMessage("Disconnect from Instagram?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								c.mApp.resetAccessToken();
								startActivity(new Intent(Dashboard.this,
										Home.class));
								finish();
								overridePendingTransition(
										android.R.anim.fade_in, 0);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GlobalLoader.FinishMe();
		// c.setHandler(handler);
		c.context = Dashboard.this;
	}

}
