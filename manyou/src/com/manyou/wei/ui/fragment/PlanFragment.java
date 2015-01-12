/**
 * @Package com.manyou.wei.ui.fragment    
 * @Title: PlanFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-14 下午10:29:43 
 * @version V1.0   
 */
package com.manyou.wei.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.dao.DataProvider;
import com.manyou.wei.dao.PlansDataHelper;
import com.manyou.wei.data.JsonObjectPostRequest;
import com.manyou.wei.entity.Chooser;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.ui.MainActivity;
import com.manyou.wei.ui.PlanCommentActivity;
import com.manyou.wei.ui.adapter.CardsAnimationAdapter;
import com.manyou.wei.ui.adapter.PlanAdapter;
import com.manyou.wei.util.CommonUtils;
import com.manyou.wei.util.ListViewUtils;
import com.manyou.wei.vendor.ManyoujiaApi;
import com.manyou.wei.view.LoadingFooter;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-14 下午10:29:43
 * 
 */
public class PlanFragment extends BaseFragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		PullToRefreshAttacher.OnRefreshListener {
	public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

	private Chooser mChooser;

	private PlansDataHelper mDataHelper;

	private PlanAdapter mAdapter;

	private ListView mListView;

	private int mPage = 1;

	private MainActivity mActivity;

	private PullToRefreshAttacher mPullToRefreshAttacher;

	private LoadingFooter mLoadingFooter;

	public static PlanFragment newInstance(Chooser chooser) {
		PlanFragment fragment = new PlanFragment();
		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_CATEGORY, chooser.name());
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_shot, null);
		mListView = (ListView) contentView.findViewById(R.id.listView);
		parseArgument();
		mDataHelper = new PlansDataHelper(AppData.getContext(), mChooser);
		mAdapter = new PlanAdapter(getActivity(), mListView);
		View header = new View(getActivity());
		mPullToRefreshAttacher = ((MainActivity) getActivity())
				.getPullToRefreshAttacher();
		mPullToRefreshAttacher.setRefreshableView(mListView, this);
		mLoadingFooter = new LoadingFooter(getActivity());

		mListView.addHeaderView(header);
		mListView.addFooterView(mLoadingFooter.getView());
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
		animationAdapter.setListView(mListView);
		mListView.setAdapter(animationAdapter);
		getLoaderManager().initLoader(0, null, this);
		mActivity = (MainActivity) getActivity();

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
				PlanEntity shot = mAdapter.getItem(position
						- mListView.getHeaderViewsCount());
				// 跳转到计划页
				startActivity(new Intent(getActivity(),
						PlanCommentActivity.class)
						.putExtra("Plan_Entity", shot));
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});

		mListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						PlanEntity shot = mAdapter.getItem(position
								- mListView.getHeaderViewsCount());

						return true;
					}
				});

		return contentView;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mPullToRefreshAttacher.getHeaderTransformer().onConfigurationChanged(
				getActivity());
	}

	private void parseArgument() {
		Bundle bundle = getArguments();
		mChooser = Chooser.valueOf(bundle.getString(EXTRA_CATEGORY));
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return mDataHelper.getCursorLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
		/*Log.e("TAG data ", data.getPosition()+"");
		Log.e("TAG data ", data.getCount()+"");
		for(; data.moveToNext();){
			Log.e("TAG data ", PlanEntity.fromCursor(data).getPlanId()+"");
		}*/
		
		/*DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
    	Cursor cursor = db.rawQuery( "SELECT distinct * FROM shots order by _id DESC", null);
    	//cursor.moveToFirst();
    	Log.e("TAG data ", cursor.getCount()+"");
    	for(; cursor.moveToNext();){
			Log.e("TAG data ", cursor.getString(0)+"  "
					+ PlanEntity.fromCursor(cursor).getDestination()
					+ " json " + cursor.getString(cursor.getColumnIndex(PlansDataHelper.ShotsDBInfo.JSON)));
		}*/
    	
		
		mAdapter.swapCursor(data);
		if (data != null && data.getCount() == 0) {
			loadFirstPage();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	private void loadData(final int page) {
		final boolean isRefreshFromTop = page == 1;
		if (!mPullToRefreshAttacher.isRefreshing() && isRefreshFromTop) {
			mPullToRefreshAttacher.setRefreshing(true);
		}
		String url = ManyoujiaApi.PLANS_LIST;
		Log.e("TAG", "loadData  "+ url);

		MineProvider mine = MineProvider.getInstance();
		
		HashMap<String, String> mMap = new HashMap<String, String>();
		mMap.put("TempForm[latitude]", mine.getUserLatitude());
		mMap.put("TempForm[longitude]", mine.getUserLongitude());
		mMap.put("TempForm[currentCity]", mine.getUserLocation());

		RequestQueue requestQueue = Volley
				.newRequestQueue(AppData.getContext());
		JsonObjectPostRequest jsonObjectPostRequest = new JsonObjectPostRequest(
				url, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(final JSONObject response) {
						CommonUtils
								.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
									@Override
									protected Object doInBackground(
											Object... params) {
										/*
										 * mPage = requestData.getPage(); if
										 * (mPage == 1) {
										 * mDataHelper.deleteAll(); }
										 */

										Log.e("TAG", response.toString());

										String json = null;
										try {
											if (!response.getString("errorCode")
													.equals("0")) {
												return null;
											}

											json = (response
													.getJSONArray("message"))
													.toString();
										} catch (JSONException e) {
											e.printStackTrace();
										}

										Gson gson = new Gson();
										ArrayList<PlanEntity> shots = gson
												.fromJson(
														json,
														new TypeToken<List<PlanEntity>>() {
														}.getType());
										
										for(int i=0; i<shots.size();i++){
											Log.e("BUG", shots.get(i).getDestination());
										}
										
										if (isRefreshFromTop && page ==1) {
											mDataHelper.deleteAll();
											mDataHelper.bulkInsert(shots);
										}else{
											mDataHelper.bulkInsert(shots);
										}
										return null;
									}

									@Override
									protected void onPostExecute(Object o) {
										super.onPostExecute(o);
										if (isRefreshFromTop) {
											mPullToRefreshAttacher
													.setRefreshComplete();

										} else {
											mLoadingFooter.setState(
													LoadingFooter.State.Idle,
													3000);
										}
										
//										DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
//								        SQLiteDatabase db = mDBHelper.getWritableDatabase();
//								    	Cursor cursor = db.rawQuery( "SELECT * FROM shots", null);
//								    	cursor.moveToLast();
								    	//Log.e("BUG", "cursor: "+cursor.toString());
								    	//mAdapter.swapCursor(cursor);
								    	
										//mAdapter.getCursor().moveToFirst();
										//mAdapter.swapCursor(mAdapter.getCursor());
//										mAdapter.changeCursor(cursor);
//										mAdapter.notifyDataSetChanged();
									}
								});
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(),
								R.string.refresh_list_failed,
								Toast.LENGTH_SHORT).show();
						if (isRefreshFromTop) {
							mPullToRefreshAttacher.setRefreshComplete();
						} else {
							mLoadingFooter.setState(LoadingFooter.State.Idle,
									3000);
						}
					}
				}, mMap);
		requestQueue.add(jsonObjectPostRequest);

	}

	/*
	 * executeRequest(new GsonRequest<PlanEntity.ShotsRequestData>(
	 * String.format(ManyoujiaApi.PLANS_LIST, mChooser.name(), page),
	 * PlanEntity.ShotsRequestData.class, null, new
	 * Response.Listener<PlanEntity.ShotsRequestData>() {
	 * 
	 * @Override public void onResponse( final PlanEntity.ShotsRequestData
	 * requestData) { CommonUtils .executeAsyncTask(new AsyncTask<Object,
	 * Object, Object>() {
	 * 
	 * @Override protected Object doInBackground( Object... params) { mPage =
	 * requestData.getPage(); if (mPage == 1) { mDataHelper.deleteAll(); }
	 * ArrayList<PlanEntity> shots = requestData .getShots();
	 * mDataHelper.bulkInsert(shots); return null; }
	 * 
	 * @Override protected void onPostExecute(Object o) {
	 * super.onPostExecute(o); if (isRefreshFromTop) { mPullToRefreshAttacher
	 * .setRefreshComplete(); } else { mLoadingFooter.setState(
	 * LoadingFooter.State.Idle, 3000); } } }); } }, new
	 * Response.ErrorListener() {
	 * 
	 * @Override public void onErrorResponse(VolleyError volleyError) {
	 * Toast.makeText(getActivity(), R.string.refresh_list_failed,
	 * Toast.LENGTH_SHORT).show(); if (isRefreshFromTop) {
	 * mPullToRefreshAttacher.setRefreshComplete(); } else {
	 * mLoadingFooter.setState(LoadingFooter.State.Idle, 3000); } } }));
	 */

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

	@Override
	public void onRefreshStarted(View view) {
		loadFirstPage();
	}
}
