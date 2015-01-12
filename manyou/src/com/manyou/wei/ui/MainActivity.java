package com.manyou.wei.ui;

import com.github.kevinsawicki.wishlist.LightDialog;
import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.entity.Chooser;
import com.manyou.wei.ui.fragment.DrawerFragment;
import com.manyou.wei.ui.fragment.PlanFragment;
import com.manyou.wei.util.PreferenceUtils;
import com.manyouren.android.service.location.MyLocation;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	private Context context;
	
	private DrawerLayout mDrawerLayout;

	private ActionBarDrawerToggle mDrawerToggle;

	private PlanFragment mContentFragment;
	
	private Chooser mChooser;

	private Menu mMenu;

	private PullToRefreshAttacher mPullToRefreshAttacher;
	
	public static int REQUESTCODE_PUBLISH = 23; // 发布计划的返回情况

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		context = this;
		
		MyLocation.getLocation(context);
		
		findViews();
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		//actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.ic_actionbar);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
		actionBar.setTitle("漫游家");

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				mMenu.findItem(R.id.action_refresh).setVisible(true);
			}

			public void onDrawerOpened(View drawerView) {
				mMenu.findItem(R.id.action_refresh).setVisible(false);
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		PullToRefreshAttacher.Options options = new PullToRefreshAttacher.Options();
		options.headerInAnimation = R.anim.pulldown_fade_in;
		options.headerOutAnimation = R.anim.pulldown_fade_out;
		options.refreshScrollDistance = 0.3f;
		options.headerLayout = R.layout.pulldown_header;
		mPullToRefreshAttacher = new PullToRefreshAttacher(this, options);

		setChooser(Chooser.All);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.left_drawer, new DrawerFragment()).commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void findViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_refresh:
			if (PreferenceUtils.getBoolean(this,
					PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
				
				
				new AlertDialog.Builder(context)
				.setTitle("是否要登录")
				.setNegativeButton("不了",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						})
				.setPositiveButton("好的",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								context.startActivity(new Intent(context,
										AccountLoginActivity.class));
								MainActivity.this.overridePendingTransition(R.anim.left_in, R.anim.left_out);

							}
						}).show();


			}else{
				AsyncHttpLoader.SessionId = PreferenceUtils.getString(this,
						PreferConfig.PREFER_INITIAL_SESSIONID);
				
				startActivityForResult(new Intent(this,
						PlanPublishActivity.class), REQUESTCODE_PUBLISH);
				overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
			
			return true;
//		case R.id.action_settings:
//			startActivity(new Intent(this, PreferenceActivity.class));

			/*PreferenceUtils.putBoolean(AppData.getContext(),
									PreferConfig.PREFER_REQUEST_NEWACCOUNT,
									true);
			finish();
			startActivity(new Intent(AppData.getContext(),
					AccountLoginActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK));*/

//			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	public void setChooser(Chooser chooser) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (mChooser == chooser) {
            return;
        }
        mPullToRefreshAttacher.setRefreshing(false);
        mChooser = chooser;
        mContentFragment = PlanFragment.newInstance(chooser);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mContentFragment).commit();
    }

	public PullToRefreshAttacher getPullToRefreshAttacher() {
		return mPullToRefreshAttacher;
	}
	
	//@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == RESULT_OK) {
				if (requestCode == REQUESTCODE_PUBLISH) {
					mContentFragment.loadFirstPageAndScrollToTop();
				}
			}
		}
}