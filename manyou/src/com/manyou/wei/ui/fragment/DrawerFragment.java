/**
 * @Package com.manyou.wei.ui.fragment    
 * @Title: DrawerFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-14 下午10:51:43 
 * @version V1.0   
 */
package com.manyou.wei.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.manyou.wei.R;
import com.manyou.wei.ui.AccountLoginActivity;
import com.manyou.wei.ui.MainActivity;
import com.manyou.wei.ui.PlanCommentActivity;
import com.manyou.wei.ui.PreferenceActivity;
import com.manyou.wei.ui.UserActivity;
import com.manyou.wei.ui.adapter.DrawerAdapter;
import com.manyou.wei.util.PreferenceUtils;

import com.manyou.wei.base.MyBaseFragment;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.entity.Chooser;
import com.manyou.wei.entity.PicassoService;
import com.manyou.wei.entity.PlanController;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.entity.UserController;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Issac on 7/18/13.
 */
public class DrawerFragment extends BaseFragment {
	private ListView mListView;

	private DrawerAdapter mAdapter;

	private MainActivity mActivity;

	View mineHeaderView;
	LinearLayout layout_threeinfo;
	ImageView iv_avatar;
	TextView tv_name;
	TextView tv_age;
	TextView tv_residence;
	ImageView iv_gender;

	private Context context;

	protected List<PlanEntity> items = new ArrayList<PlanEntity>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		context = getActivity();

		View contentView = inflater.inflate(R.layout.fragment_drawer, null);
		mListView = (ListView) contentView.findViewById(R.id.listView);

		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		iniHeader();
		initView();
		initEvent();
		fetchData();
	}

	private void iniHeader() {
		mineHeaderView = LayoutInflater.from(context).inflate(
				R.layout.include_mine_header, null);
		mineHeaderView.setClickable(false);

		iv_avatar = (ImageView) mineHeaderView.findViewById(R.id.iv_avatar);
		tv_name = (TextView) mineHeaderView.findViewById(R.id.tv_name);
		tv_age = (TextView) mineHeaderView.findViewById(R.id.tv_age);
		tv_residence = (TextView) mineHeaderView
				.findViewById(R.id.tv_residence);
		iv_gender = (ImageView) mineHeaderView.findViewById(R.id.iv_gender);
		layout_threeinfo = (LinearLayout) mineHeaderView
				.findViewById(R.id.layout_threeinfo);

		if (PreferenceUtils.getBoolean(getActivity(),
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
			// 未登录状态

			tv_name.setText("未登录");
			layout_threeinfo.setVisibility(View.GONE);

		} else {
			MineProvider mine = MineProvider.getInstance();
			PicassoService.setCirclePhoto(mine.getUserAvatar(), iv_avatar);
			tv_name.setText(mine.getUserName());
			tv_age.setText(""
					+ UserController.getAgeFromDateString(mine
							.getUserBirthday()));
			tv_residence.setText(mine.getUserResidence());
			iv_gender
					.setImageDrawable(getActivity()
							.getResources()
							.getDrawable(
									mine.getUserGender().equals("0") ? R.drawable.bg_icon_woman
											: R.drawable.bg_icon_man));
		}
	}

	public void initView() {

		mListView.addHeaderView(mineHeaderView);

		// initPlanHeadView();

		mAdapter = new DrawerAdapter(mActivity.getLayoutInflater(), items);

		mAdapter = new DrawerAdapter(getActivity().getLayoutInflater(), items);

		mListView.setAdapter(mAdapter);
		mListView.setItemChecked(0, true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(position==0){
					return;
				}
				
				PlanEntity shot = mAdapter.getItem(position
						- mListView.getHeaderViewsCount());
				shot.setBirthday(MineProvider.getInstance().getUserBirthday());
				shot.setGender(Integer.valueOf(MineProvider.getInstance().getUserGender()));
				shot.setResidence(MineProvider.getInstance().getUserResidence());
				
				// 跳转到计划页
				startActivity(new Intent(getActivity(),
						PlanCommentActivity.class)
						.putExtra("Plan_Entity", shot));
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});
	}

	public void initEvent() {
		iv_avatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (PreferenceUtils.getBoolean(context,
						PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
					dialogToLogin();

				} else {
					startActivity(new Intent(getActivity(), UserActivity.class));
					getActivity().overridePendingTransition(R.anim.left_in,
							R.anim.left_out);
				}
			}
		});

		tv_name.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (PreferenceUtils.getBoolean(context,
						PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
					dialogToLogin();

				} else {
					startActivity(new Intent(getActivity(), UserActivity.class));
					getActivity().overridePendingTransition(R.anim.left_in,
							R.anim.left_out);
				}
			}
		});

	}

	public void dialogToLogin() {
		new AlertDialog.Builder(context).setTitle("是否要登录")
				.setNegativeButton("不了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				})
				.setPositiveButton("好的", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						context.startActivity(new Intent(context,
								AccountLoginActivity.class));
						getActivity().overridePendingTransition(R.anim.left_in,
								R.anim.left_out);

					}
				}).show();
	}

	public void fetchData() {

		if (PreferenceUtils.getBoolean(context,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
			return;
		}
		if (items.isEmpty()) {

			PlanController.fetchUserPlan(context, null, handler);
		}
	}

	Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1:
				break;

			case 0: // finish
				break;

			case 1: // success

				items = PlanController.userplanList;

				mAdapter.setItems(items.toArray());
				mAdapter.notifyDataSetChanged();

				break;
			case 3: // empty

				items = Collections.emptyList();

				mAdapter.setItems(items.toArray());
				mAdapter.notifyDataSetChanged();
			}

		}
	};

}