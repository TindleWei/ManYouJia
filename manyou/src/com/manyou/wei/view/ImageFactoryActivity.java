package com.manyou.wei.view;

import com.manyou.wei.R;
import com.manyou.wei.base.MyBaseActivity;
import com.manyou.wei.util.PhotoUtils;
import com.manyou.wei.view.HeaderLayout.HeaderStyle;
import com.manyou.wei.view.HeaderLayout.onRightImageButtonClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ViewFlipper;

public class ImageFactoryActivity extends MyBaseActivity {
	private HeaderLayout mHeaderLayout;
	private ViewFlipper mVfFlipper;
	private Button mBtnLeft;
	private Button mBtnRight;

	private ImageFactoryCrop mImageFactoryCrop;
	private ImageFactoryFliter mImageFactoryFliter;
	private String mPath;
	private String mNewPath;
	private int mIndex = 0;
	private String mType;

	public static final String TYPE = "type";
	public static final String CROP = "crop";
	public static final String FLITER = "fliter";
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imagefactory);
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.imagefactory_header);
		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
		mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
		mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
	}

	@Override
	protected void initEvent() {
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 0) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					if (FLITER.equals(mType)) {
						setResult(RESULT_CANCELED);
						finish();
					} else {
						mIndex = 0;
						initImageFactory();
						mVfFlipper.setInAnimation(ImageFactoryActivity.this,
								R.anim.right_in);
						mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
								R.anim.right_out);
						mVfFlipper.showPrevious();
					}
				}
			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 1) {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryFliter
							.getBitmap());
					Intent intent = new Intent();
					intent.putExtra("path", mNewPath);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
							.cropAndSave());
					mIndex = 1;
					initImageFactory();
					mVfFlipper.setInAnimation(ImageFactoryActivity.this,
							R.anim.left_in);
					mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
							R.anim.left_out);
					mVfFlipper.showNext();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mIndex == 0) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			if (FLITER.equals(mType)) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				mIndex = 0;
				initImageFactory();
				mVfFlipper.setInAnimation(ImageFactoryActivity.this,
						R.anim.right_in);
				mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
						R.anim.right_out);
				mVfFlipper.showPrevious();
			}
		}
	}

	@Override
	protected void init() {
		mPath = getIntent().getStringExtra("path");
		mType = getIntent().getStringExtra(TYPE);
		mNewPath = new String(mPath);
		if (CROP.equals(mType)) {
			mIndex = 0;
		} else if (FLITER.equals(mType)) {
			mIndex = 1;
			mVfFlipper.showPrevious();
		}
		initImageFactory();
	}

	private void initImageFactory() {
		switch (mIndex) {
		case 0:
			if (mImageFactoryCrop == null) {
				mImageFactoryCrop = new ImageFactoryCrop(this,
						mVfFlipper.getChildAt(0));
			}
			mImageFactoryCrop.init(mPath, mScreenWidth, mScreenHeight);
			mHeaderLayout.setTitleRightImageButton("裁切图片", null,
					R.drawable.ic_topbar_rotation,
					new OnRightImageButtonClickListener());
			mBtnLeft.setText("取    消");
			mBtnRight.setText("确    认");

			break;

		case 1:
			if (mImageFactoryFliter == null) {
				mImageFactoryFliter = new ImageFactoryFliter(this,
						mVfFlipper.getChildAt(1));
			}
			mImageFactoryFliter.init(mNewPath);
			mHeaderLayout.setTitleRightImageButton("图片滤镜", null,
					R.drawable.ic_topbar_rotation,
					new OnRightImageButtonClickListener());
			mBtnLeft.setText("取    消");
			mBtnRight.setText("完    成");
			break;
		}
	}

	private class OnRightImageButtonClickListener implements
			onRightImageButtonClickListener {

		@Override
		public void onClick() {
			switch (mIndex) {
			case 0:
				if (mImageFactoryCrop != null) {
					mImageFactoryCrop.Rotate();
				}
				break;

			case 1:
				if (mImageFactoryFliter != null) {
					mImageFactoryFliter.Rotate();
				}
				break;
			}
		}
	}
}
