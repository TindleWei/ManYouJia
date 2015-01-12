/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanCommentActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午1:35:58 
 * @version V1.0   
 */
package com.manyou.wei.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.manyou.wei.ui.adapter.CardsAnimationAdapter;
import com.manyou.wei.ui.adapter.PlanCommentAdapter;
import com.manyou.wei.util.CommonUtils;
import com.manyou.wei.util.DateUtils;
import com.manyou.wei.util.ListViewUtils;
import com.manyou.wei.util.PreferenceUtils;
import com.manyou.wei.util.WidgetUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import com.github.kevinsawicki.wishlist.AsyncLoader;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.base.MyBaseActivity;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.JsonObjectPostRequest;
import com.manyou.wei.data.StringRequest;
import com.manyou.wei.entity.PicassoService;
import com.manyou.wei.entity.PlanCommentEntity;
import com.manyou.wei.entity.PlanController;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.vendor.ManyoujiaApi;
import com.manyou.wei.view.GalleryUrlActivity;
import com.manyou.wei.view.LoadingFooter;
import com.squareup.picasso.Picasso;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午1:35:58
 * 
 */

// @ContentView(R.layout.activity_plancomment)
public class PlanCommentActivity extends MyBaseActivity implements
		LoaderManager.LoaderCallbacks<List<PlanCommentEntity>>,
		PullToRefreshAttacher.OnRefreshListener, View.OnClickListener{

	@InjectView(R.id.iv_comment_send)
	ImageView iv_send;

	@InjectView(R.id.et_commment_content)
	EditText et_content;

	@InjectView(R.id.listView)
	ListView mListView;

	View planHeadView;

	private TextView tv_destination;
	private TextView tv_date;
	private TextView tv_postscript;
	private RelativeLayout layout_plan_photo;
	private ImageView iv_pho1;
	private ImageView iv_pho2;
	private ImageView iv_pho3;
	private TextView tv_plan_type;
	private TextView tv_createTime;
	
	private ImageView iv_avatar;
	private TextView tv_username;
	private TextView tv_age;
	private TextView tv_residence;
	private ImageView iv_gender;

	private int mPage = 1;

	private PlanCommentActivity mActivity;

	private PullToRefreshAttacher mPullToRefreshAttacher;

	private LoadingFooter mLoadingFooter;

	String[] urls = new String[3]; // plan images

	private PlanCommentAdapter mAdapter;

	protected List<PlanCommentEntity> items = new ArrayList<PlanCommentEntity>();

	private PlanEntity planEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_plancomment);

		setActionBar("计划评论");

		mActivity = this;

		planEntity = getSerializableExtra("Plan_Entity");

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {

		planHeadView = LayoutInflater.from(context).inflate(
				R.layout.include_plan_header, null);

		mListView.addHeaderView(planHeadView);

		initPlanHeadView();

		mAdapter = new PlanCommentAdapter(getLayoutInflater(), items);

		View header = new View(this);
		mPullToRefreshAttacher = new PullToRefreshAttacher(this);
		mPullToRefreshAttacher.setRefreshableView(mListView, this);
		mLoadingFooter = new LoadingFooter(this);

		mListView.addHeaderView(header);
		mListView.addFooterView(mLoadingFooter.getView());

		AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
		animationAdapter.setListView(mListView);
		
		mListView.setAdapter(animationAdapter);

		getSupportLoaderManager().initLoader(0, null, this);
	}

	private void initPlanHeadView() {
		
		iv_avatar =(ImageView) planHeadView.findViewById(R.id.iv_avatar);
		tv_username = (TextView) planHeadView.findViewById(R.id.tv_name);
		tv_age = (TextView) planHeadView.findViewById(R.id.tv_age);
		tv_residence = (TextView) planHeadView.findViewById(R.id.tv_residence);
		iv_gender = (ImageView) planHeadView.findViewById(R.id.iv_gender);

		tv_destination = (TextView) planHeadView
				.findViewById(R.id.tv_destination);
		tv_date = (TextView) planHeadView.findViewById(R.id.tv_startdate);
		tv_postscript = (TextView) planHeadView
				.findViewById(R.id.tv_postscript);
		iv_pho1 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho1);
		iv_pho2 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho2);
		iv_pho3 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho3);
		layout_plan_photo = (RelativeLayout) planHeadView
				.findViewById(R.id.layout_plan_photo);
		tv_plan_type = (TextView) planHeadView.findViewById(R.id.tv_plan_type);
		tv_createTime = (TextView) planHeadView
				.findViewById(R.id.tv_createTime);
		
		iv_pho1.setOnClickListener(this);
		iv_pho2.setOnClickListener(this);
		iv_pho3.setOnClickListener(this);
		
		/////////////////////////////////////////////////
		// init data
		/////////////////////////////////////////////////
		PicassoService.setCirclePhoto(UserController.getAvatarDiff(planEntity.getAvatar0()), iv_avatar);
		tv_username.setText(planEntity.getUsername());
		
		tv_age.setText(""+UserController.getAgeFromDateString(planEntity
				.getBirthday()));
		tv_residence.setText(planEntity.getResidence());
		iv_gender.setImageDrawable(context.getResources().getDrawable(
				String.valueOf(planEntity.getGender()).equals("0") ? R.drawable.bg_icon_woman
						: R.drawable.bg_icon_man));
		

		tv_destination.setText(planEntity.getDestination());
		tv_date.setText(DateUtils.getPlanDate(planEntity.getStartDate(),
				planEntity.getEndDate()));
		tv_postscript.setText(planEntity.getPostscript());

		tv_createTime.setText("发布时间："
				+ DateUtils.convertServerTime(planEntity.getCreateTime()));

		if (planEntity.getImages() != null
				&& planEntity.getImages().length() > 0) {
			List<String> listImages = PlanController.getPlanImages(planEntity
					.getImages());

			for (int i = 0; i < listImages.size(); i++) {

				Picasso.with(context).load(listImages.get(i)).resize(400, 400)
						.centerCrop().placeholder(R.drawable.gravatar_image)
						.into(i == 0 ? iv_pho1 : (i == 1 ? iv_pho2 : iv_pho3));
				(i == 0 ? iv_pho1 : (i == 1 ? iv_pho2 : iv_pho3))
						.setVisibility(View.VISIBLE);
				urls[i] = listImages.get(i);
			}
		}

		if (urls[0] == null) {
			layout_plan_photo.setVisibility(View.GONE);
		} else {
			layout_plan_photo.setVisibility(View.VISIBLE);
		}

		String for_with_seek = PlanController.list_for
				.get(planEntity.getType())
				+ "-"
				+ PlanController.list_with.get(planEntity.getTogether())
				+ "-"
				+ PlanController.list_seek.get(planEntity.getPurpose());
		tv_plan_type.setText(for_with_seek);
	}

	@Override
	protected void initEvent() {
		iv_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!et_content.getText().toString().isEmpty()) {
					if (PreferenceUtils.getBoolean(context,
							PreferConfig.PREFER_REQUEST_NEWACCOUNT, true) == true) {
					dialogToLogin();
					
					}else{
						sendComment();
						iv_send.setClickable(false);
						iv_send.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								iv_send.setClickable(true);
							}
						}, 500);
					}
					
				} else {
					Toaster.showShort(PlanCommentActivity.this, "没有内容发送");
				}
			}
		});

		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mLoadingFooter.getState() == LoadingFooter.State.Loading
						|| mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
					return;
				}
				if (firstVisibleItem + visibleItemCount >= totalItemCount
						&& totalItemCount != 0
						&& totalItemCount != mListView.getHeaderViewsCount()
								+ mListView.getFooterViewsCount()
						&& mAdapter.getCount() > 0) {
					
					//如果条目少于20条，就不加载更多
					if(totalItemCount< 20)
						return;
					
					loadNextPage();
				}
			}
		});
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*PlanCommentEntity comment = mAdapter.getItem(position
						- mListView.getHeaderViewsCount());*/

			}
		});

		mListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						Toaster.showShort(PlanCommentActivity.this, "position:"+position);
						return true;
					}
				});
	}
	
	public void dialogToLogin(){
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
						overridePendingTransition(R.anim.left_in, R.anim.left_out);

					}
				}).show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPullToRefreshAttacher.getHeaderTransformer().onConfigurationChanged(
				mActivity);
	}

	@Override
	public void onRefreshStarted(View view) {
		loadFirstPage();

	}

	@Override
	public Loader<List<PlanCommentEntity>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<PlanCommentEntity>>(context) {

			@Override
			public List<PlanCommentEntity> loadInBackground() {
				// 可从数据库中获取数据
				return items;
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<PlanCommentEntity>> loader,
			List<PlanCommentEntity> items) {
		mAdapter.setItems(items);
		if (items != null && items.size() == 0) {
			loadFirstPage();
		}

	}

	@Override
	public void onLoaderReset(Loader<List<PlanCommentEntity>> loader) {
		// 我不太清楚这里做什么，先空着
	}

	@Override
	protected void init() {

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_plan_pho1:

			startActivity(new Intent(PlanCommentActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 0));
			break;

		case R.id.iv_plan_pho2:

			startActivity(new Intent(PlanCommentActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 1));
			break;

		case R.id.iv_plan_pho3:

			startActivity(new Intent(PlanCommentActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 2));
			break;
		}
		
	}

	private void loadData(final int page) {
		final boolean isRefreshFromTop = page == 1;
		if (!mPullToRefreshAttacher.isRefreshing() && isRefreshFromTop) {
			mPullToRefreshAttacher.setRefreshing(true);
		}
		String url = ManyoujiaApi.PLAN_COMMENTS_LIST;
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.addHeader("User-Agent", "android");
		
		client.addHeader(
				"Cookie",
				"PHPSESSID="
						+ PreferenceUtils.getString(context,
								PreferConfig.PREFER_INITIAL_SESSIONID));	
		RequestParams params = new RequestParams();
		params.put("Plan[planId]", planEntity.getPlanId());
		
		client.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int code, Header[] header,
							byte[] bytes, Throwable throwable) {

						Toast.makeText(PlanCommentActivity.this, R.string.refresh_list_failed,
                                Toast.LENGTH_SHORT).show();
                        if (isRefreshFromTop) {
                            mPullToRefreshAttacher.setRefreshComplete();
                        } else {
                            mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
                        }
                    }

					@Override
					public void onSuccess(int code, Header[] header,
							byte[] bytes) {
						String str = new String(bytes);
						if (str != null)
							Log.e("TAG", str);

						String json = null;
						try {
							json = (new JSONObject(str).getJSONArray("message")).toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						Gson gson = new Gson();
						ArrayList<PlanCommentEntity> shots = gson.fromJson(json,
								new TypeToken<List<PlanCommentEntity>>() {
								}.getType());
						items = shots;
						mAdapter.setItems(items);
						mAdapter.notifyDataSetChanged();

						if (isRefreshFromTop) {
                            mPullToRefreshAttacher.setRefreshComplete();
                            
                            mListView.smoothScrollToPosition(0);
    						//这两个都可以
    						//listView.getRefreshableView().setSelection(0);
                        } else {
                            mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
                        }
					}

				});
	}

	private void loadNextPage() {
		mLoadingFooter.setState(LoadingFooter.State.Loading);
		loadData(mPage + 1);
	}

	private void loadFirstPage() {
		loadData(1);
	}

	public void loadFirstPageAndScrollToTop() {
		ListViewUtils.smoothScrollListViewToTop(mListView);
		loadFirstPage();
	}

	public void sendComment() {

		HashMap<String, Object> mMap = new HashMap<String, Object>();

		mMap.put("PlanComment[planId]", "" + planEntity.getPlanId());
		mMap.put("PlanComment[content]", et_content.getText().toString());
		// hashMap.put("PlanComment[replyId]", planId);

		String url = ManyoujiaApi.PLAN_SEND_COMMENT_URL;

		PlanController.sendPlanComment(context, mMap, sendHandler);

	}
	
	Handler sendHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1: // fail
				Toaster.showShort(PlanCommentActivity.this, "发送是失败，请再试一下");
				break;

				
			case 1: // success
				Toaster.showShort(PlanCommentActivity.this, "发送成功");
				
				loadFirstPage();
				
				et_content.setText(null);
				
				WidgetUtils.hideKeyBoard(PlanCommentActivity.this, et_content);

				break;
			}
		};
	};


}
