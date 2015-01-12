/**
 * @Package com.manyouren.android.ui.user    
 * @Title: LoginMainActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-13 下午2:11:25 
 * @version V1.0   
 */
package com.manyou.wei.ui;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-8-13 下午2:11:25 
 *  
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.view.inputmethod.InputMethodManager;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.manyou.wei.R;
import com.manyou.wei.base.MyBaseActivity;
import com.manyou.wei.config.DefaultConfig;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.PreferenceUtils;
import com.manyou.wei.util.ScreenUtils;
import com.manyou.wei.vendor.ManyoujiaApi;
import com.manyouren.android.service.location.MyLocation;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-30 上午11:20:00
 * 
 */
public class AccountLoginActivity extends MyBaseActivity {

	//private final TextWatcher watcher = validationTextWatcher();

	private AutoCompleteTextView emailText;

	private EditText passwordText;

	private Button signInButton;

	private Button signUpButton;

	private String email;

	private String password;

	private boolean requestNewAccount = true;

	private Context context = null;

	private RelativeLayout layout_relative_header;

	private ImageView iv_header_logo;

	private ObjectAnimator objectAnimator;

	private AnimatorSet animatorSet;

	public static int FLAG_MODE = 0; // initial 0; login 1; signup 2;

	private LinearLayout layout_editbox;

	private RelativeLayout layout_btns;

	private Button btn_next;

	private LinearLayout layout_back;

	private TextView tv_header_title;

	private volatile boolean requestAvaliable = false; // flag to determine

	private volatile long requestVariable = 0; // time number to count

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login_main);

		context = this;

		requestNewAccount = PreferenceUtils.getBoolean(context,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true);

		MyLocation.getLocation(context);

		initView();
		initEvent();

	}

	@Override
	protected void initView() {
		emailText = (AutoCompleteTextView) this.findViewById(R.id.et_email);
		passwordText = (EditText) this.findViewById(R.id.et_password);
		signInButton = (Button) this.findViewById(R.id.b_signin);
		signUpButton = (Button) this.findViewById(R.id.b_signup);

		emailText.setText(PreferenceUtils.getString(context,
				PreferConfig.PREFER_INITIAL_EMAIL, ""));
		/*
		 * passwordText.setText(PreferenceUtils.getString(context,
		 * PreferConfig.PREFER_INITIAL_PASSWORD, ""));
		 */

		layout_relative_header = (RelativeLayout) this
				.findViewById(R.id.layout_relative_header);

		iv_header_logo = (ImageView) this.findViewById(R.id.iv_header_logo);

		layout_editbox = (LinearLayout) this.findViewById(R.id.layout_editbox);

		layout_btns = (RelativeLayout) this.findViewById(R.id.layout_btns);

		btn_next = (Button) this.findViewById(R.id.btn_next);

		layout_back = (LinearLayout) this.findViewById(R.id.layout_back);

		tv_header_title = (TextView) this.findViewById(R.id.tv_header_title);

	}

	@Override
	protected void initEvent() {
		emailText.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				userEmailAccounts()));

		passwordText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && KeyEvent.ACTION_DOWN == event.getAction()
						&& keyCode == KeyEvent.KEYCODE_ENTER
						&& signInButton.isEnabled()) {
					handleLogin(signInButton);
					return true;
				}
				return false;
			}
		});

		passwordText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						&& signInButton.isEnabled()) {
					handleLogin(signInButton);
				}
				return false;
			}
		});

//		emailText.addTextChangedListener(watcher);
//		passwordText.addTextChangedListener(watcher);

		layout_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeHeaderLayout(0);
			}
		});

	}

	@Override
	protected void onDestroy() {
		Log.e("TAG", "destroy location");
		super.onDestroy();
	}

	/**
	 * @date: 7/1 just leave this and finish it in future
	 * 
	 * @return List<String>
	 */
	private List<String> userEmailAccounts() {
		List<String> list = new ArrayList<String>();
		String emailString = PreferenceUtils.getString(context,
				PreferConfig.PREFER_INITIAL_EMAIL);
		if (email != null)
			list.add(emailString);
		return list;
	}


	@Override
	protected void onResume() {
		super.onResume();
		FLAG_MODE = 0;
		updateUIWithValidation();
		initAnimValue();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void updateUIWithValidation() {
		final boolean populated = populated(emailText)
				&& populated(passwordText);
		signInButton.setEnabled(true);
	}

	private boolean populated(final EditText editText) {
		return editText.length() > 0;
	}

	private boolean isEmailEnabled(String email) {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher mc = pattern.matcher(email);
		return mc.matches();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.message_signing_in));
		dialog.setIndeterminate(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		return dialog;
	}

	float LOGO_X0, LOGO_X1, LOGO_Y0, LOGO_Y1;
	float TITLEBAR_Y0, TITLEBAR_Y1;
	int ANIM_TIME = 600;

	private void initAnimValue() {
		TITLEBAR_Y0 = 0f;
		TITLEBAR_Y1 = -ScreenUtils.getIntPixels(context, 220 - 56);

		LOGO_X0 = iv_header_logo.getX();

		LOGO_X1 = ScreenUtils.getIntPixels(context, 8);

		LOGO_Y0 = iv_header_logo.getY();

		LOGO_Y1 = ScreenUtils.getIntPixels(context, 40);

		Log.e("Anim", "LOGO_X0:" + LOGO_X0); // 0
		Log.e("Anim", "LOGO_X0:" + LOGO_X1); // 16
		Log.e("Anim", "LOGO_Y0:" + LOGO_Y0); // 0
		Log.e("Anim", "LOGO_Y1:" + LOGO_Y1); // 80

	}

	private void changeHeaderLayout(int flag_mode) {

		if (layout_editbox.isShown()) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}

		if (FLAG_MODE == 0) { // 是初始界面,跳到登录或注册

			FLAG_MODE = flag_mode;

			signInButton.setEnabled(false);
			signUpButton.setEnabled(false);
			btn_next.setEnabled(true);
			layout_editbox.setVisibility(View.VISIBLE);

			objectAnimator = ObjectAnimator.ofFloat(layout_relative_header,
					"translationY", 0f,
					-ScreenUtils.getIntPixels(context, 220 - 56));
			objectAnimator.setDuration(600);
			objectAnimator.setInterpolator(new DecelerateInterpolator());
			objectAnimator.start();

			animatorSet = new AnimatorSet();
			iv_header_logo
					.setPivotY(layout_relative_header.getLayoutParams().height);
			iv_header_logo.setPivotX(0);
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(iv_header_logo, "scaleX", 1f, 0.4f),
					ObjectAnimator.ofFloat(iv_header_logo, "scaleY", 1f, 0.4f),
					ObjectAnimator.ofFloat(iv_header_logo, "X",
							iv_header_logo.getX(),
							ScreenUtils.getIntPixels(context, 32)),
					ObjectAnimator.ofFloat(iv_header_logo, "Y",
							iv_header_logo.getY(),
							ScreenUtils.getIntPixels(context, 40)));

			animatorSet.setDuration(600);
			animatorSet.setInterpolator(new DecelerateInterpolator());
			animatorSet.start();

			// 接下来是EidtBox
			AnimatorSet animatorSet2 = new AnimatorSet();
			animatorSet2.playTogether(
					ObjectAnimator.ofFloat(layout_editbox, "translationY", 0f,
							-ScreenUtils.getIntPixels(context, 128)),
					ObjectAnimator.ofFloat(layout_editbox, "alpha", 1f, 1f));

			animatorSet2.setDuration(600);
			animatorSet2.setInterpolator(new DecelerateInterpolator());
			animatorSet2.start();

			// 接着是原始按钮的下移
			AnimatorSet animatorSet3 = new AnimatorSet();
			animatorSet3.playTogether(
					ObjectAnimator.ofFloat(layout_btns, "translationY", 0f,
							ScreenUtils.getIntPixels(context, 400)),
					ObjectAnimator.ofFloat(layout_btns, "alpha", 1f, 0f));

			animatorSet3.setDuration(600);
			animatorSet3.setInterpolator(new AccelerateInterpolator());
			animatorSet3.start();

			// 下来要弹出软键盘
			emailText.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 设置光标的位置，出现在最后
					emailText.setSelection(emailText.getText().length());

					emailText.setFocusable(true);
					emailText.setFocusableInTouchMode(true);
					emailText.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(emailText,
							InputMethodManager.SHOW_IMPLICIT);
					// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					// InputMethodManager.RESULT_SHOWN);
					// if (imm.isActive()) {
					// measureSoftKeyboardHeight();
					// Log.e("MESSAGETIME", "+1 ");
					//
					// }
				}

			}, 600);

			// 这里要出现底部按钮
			btn_next.postDelayed(new Runnable() {

				@Override
				public void run() {

					// RelativeLayout.LayoutParams param = new
					// RelativeLayout.LayoutParams(
					// ScreenUtils.getIntPixels(context, mScreenWidth),
					// ScreenUtils.getIntPixels(context, 60));
					//
					// param.setMargins(0, 0, 0,
					// ScreenUtils.getIntPixels(context, 300));

					// 感觉这里获取的键盘高度值还是有一点偏差
					/*
					 * if(keyBoardtPixelHeight!=0){ param.setMargins(0, 0, 0,
					 * keyBoardtPixelHeight); }else{ param.setMargins(0, 0, 0,
					 * ScreenUtils.getIntPixels(context, 300)); }
					 */

					// param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

					// btn_next.setLayoutParams(param);

					AnimatorSet animatorSet3 = new AnimatorSet();
					animatorSet3.playTogether(ObjectAnimator.ofFloat(btn_next,
							"alpha", 0f, 1f));

					animatorSet3.setDuration(400);
					animatorSet3.setInterpolator(new AccelerateInterpolator());
					animatorSet3.start();

					btn_next.setVisibility(View.VISIBLE);

					layout_back.setVisibility(View.VISIBLE);
				}

			}, 600);

		} else { // 不是初始界面,跳到初始界面

			FLAG_MODE = flag_mode; // 0

			signInButton.setEnabled(true);
			signUpButton.setEnabled(true);
			btn_next.setEnabled(false);

			layout_editbox.setVisibility(View.INVISIBLE);
			// emailText.setEnabled(true);
			// passwordText.setEnabled(true);

			objectAnimator = ObjectAnimator.ofFloat(layout_relative_header,
					"translationY",
					-ScreenUtils.getIntPixels(context, 220 - 56), 0);
			objectAnimator.setDuration(600);
			objectAnimator.setInterpolator(new DecelerateInterpolator());
			objectAnimator.start();

			animatorSet = new AnimatorSet();
			iv_header_logo
					.setPivotY(layout_relative_header.getLayoutParams().height);
			iv_header_logo.setPivotX(0);
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(iv_header_logo, "scaleX", 0.4f, 1f),
					ObjectAnimator.ofFloat(iv_header_logo, "scaleY", 0.4f, 1f),
					ObjectAnimator.ofFloat(iv_header_logo, "X",
							iv_header_logo.getX(), mScreenWidth / 2
									- ScreenUtils.getIntPixels(context, 48)),
					ObjectAnimator.ofFloat(iv_header_logo, "Y",
							iv_header_logo.getY(),
							ScreenUtils.getIntPixels(context, 84)));

			animatorSet.setDuration(600);
			animatorSet.setInterpolator(new DecelerateInterpolator());
			animatorSet.start();

			// 接下来是EidtBox
			AnimatorSet animatorSet2 = new AnimatorSet();
			animatorSet2.playTogether(
					ObjectAnimator.ofFloat(layout_editbox, "translationY",
							-ScreenUtils.getIntPixels(context, 128), 0f),
					ObjectAnimator.ofFloat(layout_editbox, "alpha", 1f, 0f));

			animatorSet2.setDuration(400);
			animatorSet2.setInterpolator(new DecelerateInterpolator());
			animatorSet2.start();

			// 接着是原始按钮的上移
			AnimatorSet animatorSet3 = new AnimatorSet();
			animatorSet3.playTogether(
					ObjectAnimator.ofFloat(layout_btns, "translationY",
							ScreenUtils.getIntPixels(context, 200), 0f),
					ObjectAnimator.ofFloat(layout_btns, "alpha", 0f, 1f));

			animatorSet3.setDuration(600);
			animatorSet3.setInterpolator(new DecelerateInterpolator());
			animatorSet3.start();

			// 底部按钮消失
			btn_next.postDelayed(new Runnable() {

				@Override
				public void run() {

					AnimatorSet animatorSet3 = new AnimatorSet();
					animatorSet3.playTogether(ObjectAnimator.ofFloat(btn_next,
							"alpha", 1f, 0f));

					animatorSet3.setDuration(400);
					animatorSet3.setInterpolator(new AccelerateInterpolator());
					animatorSet3.start();

					layout_back.setVisibility(View.GONE);

				}

			}, 0);

		}

	}

	public void handleLogin(final View view) {

		tv_header_title.setText("登录");
		btn_next.setText("完 成");

		changeHeaderLayout(1);
	}

	public void handleSignup(final View view) {

		tv_header_title.setText("注册");
		btn_next.setText("下一步");

		changeHeaderLayout(2);
	}

	/**
	 * 此方法用来处理 注册的下一步 和 登录的完成
	 * 
	 * @param view
	 * @return void
	 */
	public void handleNext(final View view) {
		if (FLAG_MODE == 1) { // 登录
			email = emailText.getText().toString().trim();
			password = passwordText.getText().toString().trim();

			showProgress();
			fetchRequest(email, password);

			handler.post(new Runnable() {

				@Override
				public void run() {
					requestAvaliable = false;
					requestVariable = 0;

					Log.e("TAG", "这里执行没");

					while (requestAvaliable == false && requestVariable < 5000) {
						try {
							Thread.sleep(200);
							requestVariable += 200;

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					hideProgress();

				}
			});

		} else if (FLAG_MODE == 2) { // 注册

			email = emailText.getText().toString().trim();
			password = passwordText.getText().toString().trim();

			if (email.length() == 0 || password.length() == 0)
				Toaster.showLong(AccountLoginActivity.this,
						R.string.emptyfail_signup);
			else if (!isEmailEnabled(email))
				Toaster.showLong(AccountLoginActivity.this,
						R.string.invalid_mail_signup);
			else if (password.length() < 6)
				Toaster.showLong(AccountLoginActivity.this,
						R.string.short_password_signup);
			else if (password.length() > 10)
				Toaster.showLong(AccountLoginActivity.this,
						R.string.long_password_signup);
			else {
				Bundle bundle = new Bundle();
				bundle.putString("bundle_email", email);
				bundle.putString("bundle_password", password);
				// startActivity(AccountSignupActivity.class, bundle);
				startActivity(AccountSignupActivity.class, bundle);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
				finish();
			}

		}

	}


	/**
	 * Hide progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void hideProgress() {
		removeDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress() {
		showDialog(0);
	}

	private void fetchRequest(String email, String password) {
		String url = ManyoujiaApi.LOGIN_URL;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("LoginForm[email]", email);
		hashMap.put("LoginForm[password]", password);

		if (MineProvider.getInstance().getUserLocation().equals("")) {
			
			hashMap.put("LoginForm[latitude]", DefaultConfig.defaultLatitude);
			hashMap.put("LoginForm[longitude]", DefaultConfig.defaultLongitude);
			hashMap.put("LoginForm[currentCity]", DefaultConfig.defaultLocation);
		} else {
			hashMap.put("LoginForm[latitude]", MineProvider.getInstance().getUserLatitude());
			hashMap.put("LoginForm[longitude]",  MineProvider.getInstance().getUserLongitude());
			hashMap.put("LoginForm[currentCity]", MineProvider.getInstance().getUserLocation());
		}
		
		UserController.fetchLoginUser(context, url, hashMap, handler);

	}

	private Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				hideProgress();
				Toaster.showLong(AccountLoginActivity.this, "fail");
				break;

			case 2:
				hideProgress();
				Toaster.showLong(AccountLoginActivity.this, "用户名或密码错误");
				break;

			case 1: // success
				requestAvaliable = true;

				// 这里有个小Bug,注意一下
				FLAG_MODE = 0;

				PreferenceUtils.putString(context,
						PreferConfig.PREFER_INITIAL_SESSIONID,
						AsyncHttpLoader.SessionId);

				PreferenceUtils.putBoolean(context,
						PreferConfig.PREFER_REQUEST_NEWACCOUNT, false);

				PreferenceUtils.putString(context,
						PreferConfig.PREFER_INITIAL_EMAIL, emailText.getText()
								.toString().trim());

				startActivity(MainActivity.class);
				finish();

				break;

			}

		};
	};

	@Override
	public void onBackPressed() {

		if (FLAG_MODE != 0) {

			changeHeaderLayout(0);

		} else {
			finish();
			Log.e("TAG", "end mode: " + FLAG_MODE);
		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

}
