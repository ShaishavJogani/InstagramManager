package adapters;

import java.util.ArrayList;

import unfollower.Dashboard.RelationBtnClickListener;
import utils.Parameters;

import com.example.unfollower.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import dataItems.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_List extends BaseAdapter {

	private LayoutInflater inflater;
	private Context mContext;
	ArrayList<Profile> user;
	private RelationBtnClickListener listener = null;
	@SuppressWarnings("deprecation")
	DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisc(true)
			.showImageOnLoading(R.drawable.profile1)
			.showImageForEmptyUri(R.drawable.profile1)
			.showImageOnFail(R.drawable.profile1).build();

	@SuppressWarnings("unchecked")
	public Adapter_List(Context c, ArrayList<Profile> userDisplay,
			RelationBtnClickListener mlistener) {
		mContext = c;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		user = new ArrayList<Profile>();
		user = (ArrayList<Profile>) userDisplay.clone();
		listener = mlistener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return user.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return user.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = convertView;
		ViewHolder holder = new ViewHolder();

		if (view == null) {
			view = inflater.inflate(R.layout.adapter_follower, null);

			holder.Name = (TextView) view.findViewById(R.id.tvNameList);
			holder.UName = (TextView) view.findViewById(R.id.tvUNameList);
			holder.pic = (ImageView) view.findViewById(R.id.ivProfilePicList);
			holder.relationship = (ImageButton) view
					.findViewById(R.id.ibRelation);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.Name.setText(user.get(position).getName());
		holder.UName.setText(user.get(position).getUName());
		SaveRelationStatusIcon(holder.relationship, user.get(position)
				.getOutRelationship());

		holder.relationship.setTag(position);
		holder.relationship.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onBtnClick((Integer) v.getTag());
			}
		});

		// holder.relationship.setText(user.get(position).getOutRelationship());
		ImageLoader.getInstance().displayImage(
				user.get(position).getProfilePicURL(), holder.pic,
				defaultOptions);

		return view;
	}

	private void SaveRelationStatusIcon(ImageView relationshipIcon,
			String outRelationship) {
		// TODO Auto-generated method stub
		if (outRelationship.equals(Parameters.followRelation))
			relationshipIcon.setBackgroundResource(R.drawable.remove_user_2);
		if (outRelationship.equals(Parameters.requestedRelation))
			relationshipIcon.setBackgroundResource(R.drawable.change_user_2);
		if (outRelationship.equals(Parameters.noneRelation))
			relationshipIcon.setBackgroundResource(R.drawable.add_user);
	}

	class ViewHolder {
		ImageView pic;
		TextView Name, UName;
		ImageButton relationship;
	}

}
