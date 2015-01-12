/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanPublishActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-21 上午10:45:48 
 * @version V1.0   
 */
package com.manyou.wei.ui;

import com.manyou.wei.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.manyou.wei.base.MyBaseActivity;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.entity.PlanController;
import com.manyou.wei.ui.adapter.PlacesAutoCompleteAdapter;
import com.manyou.wei.util.AutoCompleteUtils;
import com.manyou.wei.util.DateUtils;
import com.manyou.wei.util.PhotoUtils;
import com.manyou.wei.vendor.ManyoujiaApi;
import com.manyou.wei.view.ElasticScrollView;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-21 上午10:45:48
 * 
 */
public class PlanPublishActivity extends MyBaseActivity {

	@InjectView(R.id.btn_start_date)
	private Button start_date;

	@InjectView(R.id.btn_more)
	private Button btn_more;

	private AutoCompleteTextView autoCompView;

	private ImageButton ib_clear;

	private Context context;

	private PlanPublishActivity instance;

	private AlertDialog dialogTimes;

	private CalendarPickerView calendar;

	ArrayList<Date> dateList = null;

	LinearLayout layout_more;

	private EditText et_postscript;

	private ImageView pho1, pho2, pho3;

	private String p1Path, p2Path, p3Path;

	private String photoPath;

	/**
	 * to mark which photo part would be show
	 */
	private int photoIndex = 0;

	private static final int MENUITEM_PLAN_UP = 11001;

	private RadioGroup group_for, group_with, group_seek;

	private String str_for= "游玩", str_with="自己", str_seek="伙伴";

	private ElasticScrollView elasticScrollView;

	String destinationStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_publish);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		context = this;
		instance = this;

		setActionBar("发布计划");

		initView();
		initEvent();

		initTimesDialog();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_PLAN_UP, 0, "Upload");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_accept));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENUITEM_PLAN_UP:
			handlePlanUpload();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void initView() {
		start_date = (Button) findViewById(R.id.btn_start_date);

		btn_more = (Button) findViewById(R.id.btn_more);

		autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);

		ib_clear = (ImageButton) findViewById(R.id.ib_search_clear);

		et_postscript = (EditText) findViewById(R.id.et_postscript);

		pho1 = (ImageView) findViewById(R.id.more_iv1);

		pho2 = (ImageView) findViewById(R.id.more_iv2);

		pho3 = (ImageView) findViewById(R.id.more_iv3);

		group_for = (RadioGroup) findViewById(R.id.rg_for);

		group_with = (RadioGroup) findViewById(R.id.rg_with);

		group_seek = (RadioGroup) findViewById(R.id.rg_seek);

		layout_more = (LinearLayout) findViewById(R.id.layout_more);

		elasticScrollView = (ElasticScrollView) findViewById(R.id.elastic_scrollview);

	}

	@Override
	protected void initEvent() {

		MyOnClickListener listener = new MyOnClickListener();
		btn_more.setOnClickListener(listener);
		ib_clear.setOnClickListener(listener);
		pho1.setOnClickListener(listener);
		pho2.setOnClickListener(listener);
		pho3.setOnClickListener(listener);

		/*autoCompView.setThreshold(1);
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(context,
				android.R.layout.simple_dropdown_item_1line));*/

		autoCompView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String str = (String) adapterView.getItemAtPosition(position);
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

				// 隐藏软键盘
				InputMethodManager imm = (InputMethodManager) view.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(
							view.getApplicationWindowToken(), 0);
				}
			}
		});

		autoCompView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						/* 判断是否是 Search 键 */
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							/* 隐藏软键盘 */
							/*
							 * InputMethodManager imm = (InputMethodManager) v
							 * .getContext().getSystemService(
							 * Context.INPUT_METHOD_SERVICE); if
							 * (imm.isActive()) { imm.hideSoftInputFromWindow(
							 * v.getApplicationWindowToken(), 0); }
							 */

							((InputMethodManager) autoCompView.getContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);

							autoCompView.dismissDropDown();

							return true;
						}
						return false;
					}

				});

		group_for.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				str_for = rb.getText() + "";

			}
		});

		group_with.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				str_with = rb.getText() + "";

			}
		});

		group_seek.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				str_seek = rb.getText() + "";
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();

				Bitmap bitmap = PhotoUtils.getBitmapFromUri(cr, uri);
				if (bitmap != null) {
					String path = PhotoUtils.savePhotoToSDCard(bitmap);
					PhotoUtils.fliterPhoto(context, instance, path);
					// PhotoUtils.cropPhoto(context, instance, path);
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_FLITER:

				photoPath = data.getStringExtra("path");

				Bitmap filterBitmap = PhotoUtils.getBitmapFromFile(photoPath);

				if (photoIndex == 0) {
					pho1.setImageBitmap(filterBitmap);
					pho2.setVisibility(View.VISIBLE);
					p1Path = photoPath;
				} else if (photoIndex == 1) {
					pho2.setImageBitmap(filterBitmap);
					pho3.setVisibility(View.VISIBLE);
					p2Path = photoPath;
				} else if (photoIndex == 2) {
					pho3.setImageBitmap(filterBitmap);
					p3Path = photoPath;
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_CROP:
				break;

			}
		}
	}

	/**
	 * this is times square open-sources
	 * 
	 * @return void
	 */
	private void initTimesDialog() {

		start_date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				start_date.setClickable(false);

				Calendar nextYear = Calendar.getInstance();

				Calendar thisYear = Calendar.getInstance();

				dateList = new ArrayList<Date>();

				thisYear.add(Calendar.YEAR, 0);

				nextYear.add(Calendar.YEAR, 1);

				calendar = (CalendarPickerView) getLayoutInflater().inflate(
						R.layout.dialog_times, null, false);

				calendar.init(thisYear.getTime(), nextYear.getTime()).inMode(
						SelectionMode.RANGE);

				calendar.setOnDateSelectedListener(new OnDateSelectedListener() {

					@Override
					public void onDateUnselected(Date date) {
					}

					@Override
					public void onDateSelected(Date date) {
						if (dateList.size() == 0) {
							dateList.add(date);
						} else if (dateList.size() == 1) {
							if (date.after(dateList.get(0))) {
								dateList.add(date);
							} else {
								dateList.remove(0);
								dateList.add(date);
							}
						} else if (dateList.size() > 1) {
							if (date.after(dateList.get(1))) {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);
							} else if (date.before(dateList.get(0))) {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);

							} else {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);
							}
						}
					}
				});

				dialogTimes = new AlertDialog.Builder(PlanPublishActivity.this)
						.setView(calendar)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										dialogInterface.dismiss();
										showTimes();
										start_date.setClickable(true);
									}
								}).create();
				dialogTimes
						.setOnShowListener(new DialogInterface.OnShowListener() {
							@Override
							public void onShow(DialogInterface dialogInterface) {
								calendar.fixDialogDimens();
								start_date.setClickable(true);
							}
						});
				dialogTimes.show();
			}
		});

	}

	public void showTimes() {

		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		if (dateList.size() == 2) {
			int rangeDay = 1 + (int) ((dateList.get(1).getTime() - dateList
					.get(0).getTime()) / 86400 / 1000);

			start_date.setText(format.format(dateList.get(0)) + "  " + rangeDay
					+ "天");
		} else if (dateList.size() == 1) {
			start_date.setText(format.format(dateList.get(0)) + "  当日");
		} else {
			start_date.setText("");
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_more:
				if (layout_more.getVisibility() == View.GONE) {

					ObjectAnimator anim = ObjectAnimator.ofFloat(btn_more,
							"rotation", 0f, 90f);
					anim.setDuration(250);
					anim.start();

					layout_more.setVisibility(View.VISIBLE);
					elasticScrollView.post(new Runnable() {
						@Override
						public void run() {
							elasticScrollView.fullScroll(View.FOCUS_DOWN);
						}
					});
				}

				else {

					ObjectAnimator anim = ObjectAnimator.ofFloat(btn_more,
							"rotation", 90f, 0f);
					anim.setDuration(250);
					anim.start();

					elasticScrollView.post(new Runnable() {
						@Override
						public void run() {
							elasticScrollView.fullScroll(View.FOCUS_UP);
						}
					});

					layout_more.postDelayed((new Runnable() {

						@Override
						public void run() {
							layout_more.setVisibility(View.GONE);
						}

					}), 200);
				}

				break;

			case R.id.ib_search_clear:
				autoCompView.setText("");
				break;

			case R.id.more_iv1:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 0;
				break;

			case R.id.more_iv2:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 1;
				break;

			case R.id.more_iv3:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 2;
				break;
			}
		}
	}

	/**
	 * Hide progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void hideProgress() {
		dismissDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress() {
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("发布计划中");
		dialog.setIndeterminate(true);
		/*
		 * dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * dialog.setProgress(100);
		 */
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		return dialog;
	}

	/**
	 * 判断 能否上传
	 * 
	 * @return void
	 */
	private void handlePlanUpload() {

		if (autoCompView.getText().toString() == "") {
			Toaster.showShort(PlanPublishActivity.this, "需要有目的地");
			return;
		}

		if (dateList == null || dateList.size() == 0) {
			Toaster.showShort(PlanPublishActivity.this, "需要有时间");
			return;
		}

		if (str_for == null || str_seek == null || str_with == null) {
			Toaster.showShort(PlanPublishActivity.this, "需要目的，和谁，寻求");
			return;
		}

		destinationStr = autoCompView.getText().toString();

		showProgress();

		fetchRequest();

	}

	/**
	 * 传递 上传参数
	 * 
	 * @return void
	 */
	private void fetchRequest() {
		String url = ManyoujiaApi.PLAN_PUBLISH_URL;

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("Plan[destination]", destinationStr);
		if (dateList != null) {
			if (dateList.size() == 1) {
				hashMap.put("Plan[startDate]",
						DateUtils.getTime(dateList.get(0)));
				hashMap.put("Plan[endDate]", DateUtils.getTime(dateList.get(0)));
			} else if (dateList.size() == 2) {
				hashMap.put("Plan[startDate]",
						DateUtils.getTime(dateList.get(0)));
				hashMap.put("Plan[endDate]", DateUtils.getTime(dateList.get(1)));
			}
		}

		hashMap.put("Plan[type]", "" + PlanController.list_for.indexOf(str_for));
		hashMap.put("Plan[together]",
				"" + PlanController.list_with.indexOf(str_with));
		hashMap.put("Plan[purpose]",
				"" + PlanController.list_seek.indexOf(str_seek));

		hashMap.put("Plan[transportation]", 0 + "");

		if (p1Path != null) {
			hashMap.put("image[0]", new File(p1Path));
			Log.e("TAG", "图片1有");
		}
		if (p2Path != null) {
			// bug 特殊处理，param中处理，改为image[]
			hashMap.put("image[1]", new File(p2Path));
			Log.e("TAG", "图片2有");
		}
		if (p3Path != null) {
			// bug 特殊处理，param中处理，改为image[]
			hashMap.put("image[2]", new File(p3Path));
			Log.e("TAG", "图片3有");
		}

		hashMap.put("Plan[flightNumber]", 0 + "");

		// hashMap.put("Plan[city]", Constants.PLACE_NOW);

		if (et_postscript.getText().toString().isEmpty()) {

			String with = (str_with.equals("自己") ? "一个人" : "和" + str_with);
			String note = "" + with + "去" + autoCompView.getText().toString()
					+ str_for + ",寻求" + str_seek + "。";
			hashMap.put("Plan[postscript]", note);
			Log.e("TAG", "发布计划这里  empty note");
		} else {
			hashMap.put("Plan[postscript]", et_postscript.getText().toString());
		}

		Log.e("TAG", autoCompView.getText().toString());
		Log.e("TAG", hashMap.toString());

		Log.e("TAG", "session: " + AsyncHttpLoader.SessionId);

		PlanController.fetchPublishPlan(context, url, hashMap, handler);

	}

	private Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				Toaster.showLong(PlanPublishActivity.this, "fail");
				break;
			case 0: // finish
				hideProgress();
				break;
			case 1: // success
				Toaster.showLong(PlanPublishActivity.this, "success");

				// to put user's plan to the top
				finish();
				break;
			}
		};
	};

	@Override
	protected void init() {
		// TODO Auto-generated method stub
	}

}
