/**
 * @Package com.manyouren.android.ui    
 * @Title: BaseActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-3 下午4:07:28 
 * @version V1.0   
 */
package com.manyou.wei.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.RoboFragmentActivity;
import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnConfigurationChangedEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;
import roboguice.inject.RoboInjector;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import com.github.kevinsawicki.wishlist.ViewFinder;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.manyou.wei.R;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-3 下午4:07:28
 * 
 */

public abstract class MyBaseActivity extends RoboFragmentActivity {

	/**
	 * Finder bound to this activity's view
	 */
	protected ViewFinder finder;

	protected Context context;

	@Inject
	protected EventManager eventManager;
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finder = new ViewFinder(this);
		context = this;
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}

	/** 初始化视图 **/
	protected abstract void initView();

	/** 初始化事件 **/
	protected abstract void initEvent();

	/** 初始化数据 **/
	protected abstract void init();

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case android.R.id.home:
			// Don't call finish! Because activity could have been started by an
			// outside activity and the home button would not operated as
			// expected!
			// final Intent homeIntent = new Intent(this, HomeActivity.class);
			// homeIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP |
			// FLAG_ACTIVITY_SINGLE_TOP);
			// startActivity(homeIntent);
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Get intent extra
	 * 
	 * @param name
	 * @return serializable
	 */
	@SuppressWarnings("unchecked")
	protected <V extends Serializable> V getSerializableExtra(final String name) {
		return (V) getIntent().getSerializableExtra(name);
	}

	/**
	 * Get intent extra
	 * 
	 * @param name
	 * @return int
	 */
	protected int getIntExtra(final String name) {
		return getIntent().getIntExtra(name, -1);
	}

	/**
	 * Get intent extra
	 * 
	 * @param name
	 * @return string
	 */
	protected String getStringExtra(final String name) {
		return getIntent().getStringExtra(name);
	}

	/**
	 * Get intent extra
	 * 
	 * @param name
	 * @return string array
	 */
	protected String[] getStringArrayExtra(final String name) {
		return getIntent().getStringArrayExtra(name);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/**
	 * 设置ActionBar
	 * 
	 */
	public void setActionBar(String title) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		actionBar.setLogo(R.drawable.ic_actionbar);
		actionBar.setTitle(title);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}

