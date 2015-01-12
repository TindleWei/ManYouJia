/**
 * @Package com.manyouren.android.ui.user    
 * @Title: SigupSlideActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-12 下午3:00:17 
 * @version V1.0   
 */
package com.manyou.wei.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.manyou.wei.R;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.ui.fragment.AccountSignupFragment;
import com.manyou.wei.util.ActivityForResultUtil;
import com.manyou.wei.util.PhotoUtils;
import com.manyou.wei.util.PreferenceUtils;
import com.manyou.wei.vendor.ManyoujiaApi;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-8-12 下午3:00:17
 * 
 */
public class AccountSignupActivity extends FragmentActivity {

	private static final int NUM_PAGES = 3;

	private ViewPager mPager;

	private PagerAdapter mPagerAdapter;

	public static File mHeadFile = null;

	public static String mHeadPath = "";

	public static Bitmap mHeadBitmap = null;

	public static String email = "";

	public static String password = "";

	public static String name = "";

	public static String birthday = "";

	public static int gender = -1;

	public static String liveland = "";

	private Context context;

	private volatile boolean requestAvaliable = false; // flag to determine

	private volatile long requestVariable = 0; // time number to count

	private SignupFirstFragment firstFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_slide);

		context = this;

		email = getIntent().getStringExtra("bundle_email");
		password = getIntent().getStringExtra("bundle_password");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(
				getResources().getDrawable(R.drawable.ic_actionbar_logo_white));
		getActionBar().setTitle("注册 1/3");

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.slide_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.
				invalidateOptionsMenu();
			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_signup_slide, menu);

		/*
		 * menu.findItem(R.id.action_previous).setEnabled(
		 * mPager.getCurrentItem() > 0);
		 */

		if (mPager.getCurrentItem() == 0) {
			menu.findItem(R.id.action_previous).setTitle("返回").setEnabled(true);
		}

		// Add either a "next" or "finish" button to the action bar, depending
		// on which page
		// is currently selected.
		MenuItem item = menu
				.add(Menu.NONE,
						R.id.action_next,
						Menu.NONE,
						(mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) ? R.string.action_finish
								: R.string.action_next);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			final Intent homeIntent = new Intent(this,
					AccountLoginActivity.class);
			startActivity(homeIntent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			finish();
			return true;

		case R.id.action_previous:
			// Go to the previous step in the wizard. If there is no previous
			// step,
			// setCurrentItem will do nothing.
			if (mPager.getCurrentItem() == 0) {

				startActivity(new Intent(this, AccountLoginActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
				finish();
				return true;
			}

			getActionBar().setTitle("注册  " + (mPager.getCurrentItem()) + "/3");

			mPager.setCurrentItem(mPager.getCurrentItem() - 1);

			return true;

		case R.id.action_next:

			if (mPager.getCurrentItem() == 0) {
				name = firstFragment.getText();
				if (mHeadBitmap == null) {
					Toaster.showShort(AccountSignupActivity.this, "头像为空");
					return true;
				}
				if (name.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "昵称为空");
					return true;
				}
			} else if (mPager.getCurrentItem() == 1) {
				if (gender == -1) {
					Toaster.showShort(AccountSignupActivity.this, "性别为空");
					return true;
				}
				if (birthday.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "生日为空");
					return true;
				}
			} else if (mPager.getCurrentItem() == 2) {
				if (liveland.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "现居地为空");
					return true;
				}
			}

			if (mPager.getCurrentItem() + 1 == 3) {

				handleSignup();

			} else {

				// Advance to the next step in the wizard. If there is no next
				// step,
				// setCurrentItem
				// will do nothing.
				mPager.setCurrentItem(mPager.getCurrentItem() + 1);

				if (mPager.getCurrentItem() + 1 <= 3) {
					getActionBar().setTitle(
							"注册  " + (mPager.getCurrentItem() + 1) + "/3");
				}
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (firstFragment == null) {
					firstFragment = SignupFirstFragment.getInstance();
				}
				return firstFragment;
				// return mFragment0;
			case 1:
				return AccountSignupFragment.create(position);
				// return mFragment1;
			case 2:
				return AccountSignupFragment.create(position);
				// return mFragment2;
			}
			throw new IllegalStateException("No fragment at position "
					+ position);
			// return SignupSlideFragment.create(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment f = (Fragment) super.instantiateItem(container, position);

			return f;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public void handleSignup() {
		showProgress();
		// 空头像时
		// 处理
		fetchRequest();

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

	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(getText(R.string.message_signing_up));
		dialog.setIndeterminate(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		return dialog;
	}

	private void fetchRequest() {
		String url = ManyoujiaApi.SIGNUP_URL;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("RegistrationForm[username]", name);
		hashMap.put("RegistrationForm[email]", AccountSignupActivity.email);
		hashMap.put("RegistrationForm[password]",
				AccountSignupActivity.password);
		hashMap.put("RegistrationForm[gender]", AccountSignupActivity.gender
				+ "");

		hashMap.put("RegistrationForm[residence]", liveland.toString());
		hashMap.put("RegistrationForm[birthday]", birthday.toString());

		File avatarFile = new File(AccountSignupActivity.mHeadPath);
		if (avatarFile.exists()) {
			hashMap.put("RegistrationForm[avatar]", avatarFile);
		}

		hashMap.put("RegistrationForm[IEMI]", "iemi");
		hashMap.put("RegistrationForm[latitude]", MineProvider.getInstance().getUserLatitude());
		hashMap.put("RegistrationForm[longitude]", MineProvider.getInstance().getUserLongitude());

		hashMap.put("RegistrationForm[currentCity]", MineProvider.getInstance().getUserLocation());

		Log.e("TAG", hashMap.toString());
		
		UserController.fetchSignupUser(context, url, hashMap, handler);

	}

	@Override
	public void onBackPressed() {

		if (mPager.getCurrentItem() != 0) {

			getActionBar().setTitle("注册  " + (mPager.getCurrentItem()) + "/3");

			mPager.setCurrentItem(mPager.getCurrentItem() - 1);

		} else {

			startActivity(new Intent(this, AccountLoginActivity.class));
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			finish();
		}

	}

	public static Map<String, String> greenMap = null;

	private Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				hideProgress();
				Toaster.showLong(AccountSignupActivity.this, "fail");
				break;

			case 4:
				hideProgress();
				Toaster.showLong(AccountSignupActivity.this, "注册失败");
				break;

			case 7:
				hideProgress();
				Toaster.showLong(AccountSignupActivity.this, "邮箱已注册");
				break;

			case 1: // success

				requestAvaliable = false;

				hideProgress();
				Toaster.showLong(AccountSignupActivity.this, "注册成功");

				PreferenceUtils.putString(context,
						PreferConfig.PREFER_INITIAL_SESSIONID,
						AsyncHttpLoader.SessionId);

				PreferenceUtils.putBoolean(context,
						PreferConfig.PREFER_REQUEST_NEWACCOUNT, false);

				PreferenceUtils.putString(context,
						PreferConfig.PREFER_INITIAL_EMAIL,
						AccountSignupActivity.email);

				startActivity(new Intent(context, MainActivity.class));
				finish();

				break;

			}
		}

	};

	public static class SignupFirstFragment extends Fragment {

		private ImageView iv_avatar;

		private EditText et_name;

		private Context context;

		public SignupFirstFragment() {

		}

		public static SignupFirstFragment getInstance() {
			return new SignupFirstFragment();
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			context = getActivity();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_1, container, false);
			iv_avatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
			et_name = (EditText) rootView.findViewById(R.id.et_name);

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			if (AccountSignupActivity.mHeadBitmap != null) {
				iv_avatar.setImageBitmap(PhotoUtils.toRoundBitmap(AccountSignupActivity.mHeadBitmap));
			}

			iv_avatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {

					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(
							intent,
							ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
					/*
					 * new AlertDialog.Builder(context) .setTitle("修改头像")
					 * .setItems(new String[] { "拍照上传", "上传相册中的照片" }, new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick( DialogInterface dialog,
					 * int which) { Intent intent = null; switch (which) { case
					 * 0: break; case 1:
					 * 
					 * break; } } }).create().show();
					 */

				}
			});
		}

		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			if (resultCode == Activity.RESULT_OK) {
				switch (requestCode) {
				/**
				 * 通过照相修改头像
				 */
				case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:

					break;
				/**
				 * 通过本地修改头像
				 */
				case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
					Uri uri = null;
					if (data == null) {
						Toast.makeText(context, "取消上传", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (resultCode == Activity.RESULT_OK) {
						if (!Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							Toast.makeText(context, "SD不可用", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						uri = data.getData();
						startPhotoZoom(uri);
					} else {
						Toast.makeText(context, "照片获取失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				/**
				 * 裁剪修改的头像
				 */
				case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
					if (data == null) {
						Toast.makeText(context, "取消上传", Toast.LENGTH_SHORT)
								.show();
						return;
					} else {
						saveCropPhoto(data);
					}
					break;

				}
			}
		}

		/**
		 * 系统裁剪照片
		 * 
		 * @param uri
		 */
		private void startPhotoZoom(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("scale", true);
			intent.putExtra("noFaceDetection", true);
			intent.putExtra("return-data", true);
			startActivityForResult(intent,
					ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP);
		}

		/**
		 * 保存裁剪的照片
		 * 
		 * @param data
		 */
		private void saveCropPhoto(Intent data) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap bitmap = extras.getParcelable("data");
				// bitmap = PhotoUtil.toRoundCorner(bitmap, 15);
				if (bitmap != null) {
					AccountSignupActivity.mHeadBitmap = bitmap;
					iv_avatar.setImageBitmap(PhotoUtils.toRoundBitmap(bitmap));
					savePhoto(bitmap);
				}
			} else {
				Toast.makeText(context, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
			}
		}

		private void savePhoto(Bitmap bitmap) {

			AccountSignupActivity.mHeadPath = PhotoUtils
					.savePhotoToSDCard(bitmap);

			CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 100;
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AccountSignupActivity.mHeadPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (bitmap.compress(format, quality, stream)) {
				AccountSignupActivity.mHeadFile = new File(
						AccountSignupActivity.mHeadPath);
			}

		}

		public String getText() {
			return et_name.getText().toString().trim();
		}
	}

}
