/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: GalleryUrlActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-8 下午5:26:24 
 * @version V1.0   
 */
package com.manyou.wei.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manyou.wei.R;
import com.manyou.wei.view.touchgallery.GalleryViewPager;
import com.manyou.wei.view.touchgallery.UrlPagerAdapter;
import com.manyou.wei.view.touchgallery.BasePagerAdapter.OnItemChangeListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.Window;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-8 下午5:26:24
 * 
 */
public class GalleryUrlActivity extends FragmentActivity {

	public static final String GALLERY_URLS = "gallery_urls";
	
	public static final String GALLERY_INDEX = "gallery_index";
	
	private GalleryViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_galleryurl);

		String[] urls = getIntent().getStringArrayExtra(GALLERY_URLS);
		
//		Log.e("TAG", "wtf 0 "+ urls[0]);
//		Log.e("TAG", "wtf 1 "+ urls[1]);
//		Log.e("TAG", "wtf 2 "+ urls[2]);
		
		int index =getIntent().getIntExtra(GALLERY_INDEX, 0);
		
		List<String> items = new ArrayList<String>();
		
		for(int i=0;i<urls.length;i++){
			if(urls[i]!=null)
				items.add(urls[i]);
		}

		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {

			}
		});

		mViewPager = (GalleryViewPager) findViewById(R.id.touchgallery_viewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);

		mViewPager.setCurrentItem(index);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
