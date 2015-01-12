/**
 * @Package com.manyou.wei.ui    
 * @Title: UserActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-18 下午2:34:09 
 * @version V1.0   
 */
package com.manyou.wei.ui;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.base.MyBaseActivity;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.entity.PicassoService;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.PreferenceUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-18 下午2:34:09
 * 
 */
public class UserActivity extends MyBaseActivity {

	@InjectView(R.id.iv_avatar)
	ImageView iv_avatar;

	@InjectView(R.id.tv_name)
	TextView tv_name;

	@InjectView(R.id.tv_gender)
	TextView tv_gender;

	@InjectView(R.id.tv_age)
	TextView tv_age;

	@InjectView(R.id.tv_residence)
	TextView tv_residence;

	@InjectView(R.id.tv_version)
	TextView tv_version;

	@InjectView(R.id.btn_logout)
	Button btn_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		setActionBar("我的资料");

		initEvent();
		init();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {
		
		btn_logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PreferenceUtils.putBoolean(AppData.getContext(),
						PreferConfig.PREFER_REQUEST_NEWACCOUNT,
						true);
				finish();

				startActivity(new Intent(getApplicationContext(),
						AccountLoginActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK));
				
			}
		});
	}

	@Override
	protected void init() {
		MineProvider mine = MineProvider.getInstance();
		Log.e("BUG",mine.getUserAvatar());
		PicassoService.setSquarePhoto(mine.getUserAvatar(), iv_avatar);
		tv_name.setText(mine.getUserName());
		tv_age.setText(""+UserController.getAgeFromDateString(mine
				.getUserBirthday()));
		tv_residence.setText(mine.getUserResidence());
		tv_gender.setText(mine.getUserGender().equals("0")?"女":"男");
	}

}
