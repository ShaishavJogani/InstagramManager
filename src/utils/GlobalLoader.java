package utils;

import java.util.Timer;
import java.util.TimerTask;

import com.example.unfollower.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GlobalLoader {
	C c = C.getinstance();

	public static Dialog dialog;
	static Timer t;
	static TextView tv;

	public GlobalLoader(final String PreloaderText, final Context context,
			final Activity activity) {
		// TODO Auto-generated constructor stub
			dialog = new Dialog(context,R.style.Theme_Transparent );

			dialog.getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);
			dialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			View v;

			v = activity.getLayoutInflater().inflate(R.layout.progress_layout,
					null, false);
			RelativeLayout rLayout = (RelativeLayout) v
					.findViewById(R.id.progressContainer);
//			rLayout.setBackgroundResource(R.anim.preloader_animation);

			tv = (TextView) v.findViewById(R.id.progress_text);
			tv.setText(PreloaderText);
			tv.setTextColor(Color.WHITE);

			dialog.setContentView(v);
			dialog.setCancelable(false);

			dialog.show();

	}
	public static void FinishMe() {

		try {
			if (dialog != null) {
				if (dialog.isShowing()) {
//					if (frameAnimation.isRunning()) {
//						frameAnimation.stop();
//					}
					dialog.dismiss();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (t != null) {
			t.cancel();
		}
	}

	public static void updateText(String text) {
		try {
			if (dialog != null) {
				if (dialog.isShowing() && tv != null) {
					tv.setText(text);
					tv.invalidate();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
//	public GlobalLoader(final String PreloaderText, final Context context,
//			final Activity activity) {
//		// TODO Auto-generated constructor stub
//		try {
//			FinishMe();
//
//			if (c.conn.isConnected()) {
//				t = new Timer();
//				t.scheduleAtFixedRate(new TimerTask() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						this.cancel();
//						activity.runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//
//								FinishMe();
//
//								try {
//									activity.runOnUiThread(new Runnable() {
//
//										@Override
//										public void run() {
//											// TODO Auto-generated method stub
//											Toast.makeText(context,
//													"Retrying....",
//													Toast.LENGTH_LONG).show();
//										}
//									});
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//
//								if (!c.conn.isConnected()) {
//									new GlobalLoader(PreloaderText, context,
//											activity);
//								} else {
//									new GlobalLoader(PreloaderText, context,
//											activity);
//									// EmitManager.recall();
//								}
//							}
//						});
//					}
//				}, 45000, 45000);
//			} else {
//				t = new Timer();
//				t.scheduleAtFixedRate(new TimerTask() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						this.cancel();
//						activity.runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								FinishMe();
//								try {
//									activity.runOnUiThread(new Runnable() {
//										@Override
//										public void run() {
//											// TODO Auto-generated method stub
//											Toast.makeText(context,
//													"Retrying....",
//													Toast.LENGTH_LONG).show();
//										}
//									});
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//								if (!c.conn.isConnected()) {
//									new GlobalLoader(PreloaderText, context,
//											activity);
//								} else {
//									new GlobalLoader(PreloaderText, context,
//											activity);
//									// EmitManager.recall();
//								}
//							}
//						});
//					}
//				}, 60000, 60000);
//			}
//			dialog = new Dialog(context, R.style.Theme_Transparent);
//
//			dialog.getWindow().setBackgroundDrawableResource(
//					android.R.color.transparent);
//			dialog.getWindow().clearFlags(
//					WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//			View v;
//
//			v = activity.getLayoutInflater().inflate(R.layout.progress_layout,
//					null, false);
//			RelativeLayout rLayout = (RelativeLayout) v
//					.findViewById(R.id.progressContainer);
//			rLayout.setBackgroundResource(R.anim.preloader_animation);
//
//			frameAnimation = (AnimationDrawable) rLayout.getBackground();
//			frameAnimation.start();
//			tv = (TextView) v.findViewById(R.id.progress_text);
//			tv.setText(PreloaderText);
//			tv.setTextColor(Color.WHITE);
//
//			dialog.setContentView(v);
//			dialog.setCancelable(false);
//
//			dialog.show();
//
//		} catch (BadTokenException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
}
