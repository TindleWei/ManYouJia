/**
 * @Package com.manyouren.android.ui.user    
 * @Title: SignupSlideFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-12 下午3:14:27 
 * @version V1.0   
 */
package com.manyou.wei.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.manyou.wei.R;
import com.manyou.wei.ui.AccountSignupActivity;
import com.manyou.wei.util.ActivityForResultUtil;
import com.manyou.wei.view.CityListActivity;
import com.manyouren.android.service.location.MyLocation;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-8-12 下午3:14:27
 * 
 */
public class AccountSignupFragment extends Fragment implements OnClickListener {

	public static final String ARG_PAGE = "page";

	private int mPageNumber;

	private EditText et_birthday;

	private EditText et_gender;

	private EditText et_liveland;

	private Button btn_done;

	private Context context = null;


	/*
	 * private String mHeadPath = Environment.getExternalStorageDirectory()
	 * .toString() + File.separator + "ManYouRen/My/myhead.jpg";
	 */
	

	public static AccountSignupFragment create(int pageNumber) {

		AccountSignupFragment fragment = new AccountSignupFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);

		return fragment;
	}

	public AccountSignupFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = null;

		Log.e("TAG", mPageNumber + "!!!!!!");
		switch (mPageNumber) {
		case 1:

			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_2, container, false);

			if (et_gender == null)
				et_gender = (EditText) rootView.findViewById(R.id.et_gender);
			if (et_birthday == null)
				et_birthday = (EditText) rootView
						.findViewById(R.id.et_birthday);

			break;
		case 2:

			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_3, container, false);

			if (et_liveland == null)
				et_liveland = (EditText) rootView
						.findViewById(R.id.et_liveland);

			break;
		}

		return rootView;
	}

	public int getPageNumber() {
		return mPageNumber;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initEvent(mPageNumber);
	}

	private void initEvent(int pageNumber) {

		switch (pageNumber) {
		case 1:
			et_birthday.setOnClickListener(this);
			et_gender.setOnClickListener(this);
			break;
		case 2:
			et_liveland.setOnClickListener(this);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_avatar:

			new AlertDialog.Builder(context)
					.setTitle("修改头像")
					.setItems(new String[] { "拍照上传", "上传相册中的照片" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = null;
									switch (which) {
									case 0:
										break;
									case 1:
										intent = new Intent(Intent.ACTION_PICK,
												null);
										intent.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
										startActivityForResult(
												intent,
												ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
										break;
									}
								}
							}).create().show();

			break;

		case R.id.et_birthday:

			DialogDatePicker myDateFragment = new DialogDatePicker();
			myDateFragment.show(getActivity().getFragmentManager(), null);

			break;

		case R.id.et_gender:

			new AlertDialog.Builder(context)
					.setTitle("性别")
					.setSingleChoiceItems(new String[] { "男", "女" }, 0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										et_gender.setText("男");
										AccountSignupActivity.gender = 1;
									} else {
										et_gender.setText("女");
										AccountSignupActivity.gender = 0;
									}
									dialog.dismiss();
								}
							}).show();
			break;

		case R.id.et_liveland:
			startActivityForResult(new Intent(getActivity(),
					CityListActivity.class), 31);
			getActivity().overridePendingTransition(R.anim.left_in,
					R.anim.left_out);

			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case 31:
				et_liveland.setText(data.getStringExtra("CityName"));
				AccountSignupActivity.liveland = et_liveland.getText().toString();
				break;
			}
		}
	}



	class DialogDatePicker extends DialogFragment {

		private Calendar calendar;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog dialog = null;
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(
					getActivity(),
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							String birthday = (year
									+ "-"
									+ ((monthOfYear + 1) < 10 ? "0"
											+ (monthOfYear + 1)
											: (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0"
									+ dayOfMonth
									: dayOfMonth));
							et_birthday.setText(birthday);
							AccountSignupActivity.birthday = birthday;
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));

			return dialog;
		}

	}

}
