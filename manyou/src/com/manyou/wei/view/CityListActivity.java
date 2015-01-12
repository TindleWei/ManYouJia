package com.manyou.wei.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manyou.wei.R;
import com.manyou.wei.base.MyBaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

public class CityListActivity extends MyBaseActivity {
	private ArrayList<String> mItems;
	private IndexableListView mListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_city);

		mItems = new ArrayList<String>();
		mItems.add("西安");
		mItems.add("北京");
		mItems.add("上海");
		mItems.add("广州");
		mItems.add("深圳");
		mItems.add("杭州");
		mItems.add("天津");
		mItems.add("南京");
		mItems.add("成都");
		mItems.add("武汉");
		mItems.add("重庆");
		mItems.add("沈阳");
		mItems.add("大连");
		mItems.add("青岛");
		mItems.add("苏州");
		mItems.add("宁波");
		mItems.add("无锡");
		mItems.add("长沙");
		mItems.add("郑州");
		mItems.add("厦门");
		mItems.add("哈尔滨");
		mItems.add("济南");
		mItems.add("长春");

		Collections.sort(mItems);

		ContentAdapter adapter = new ContentAdapter(this,
				android.R.layout.simple_list_item_1, mItems);

		mListView = (IndexableListView) findViewById(R.id.listview);
		mListView.setAdapter(adapter);
		mListView.setFastScrollEnabled(true);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("CityName", mItems.get(position));
				setResult(RESULT_OK, intent);
				finish();

			}
		});
	}

	private class ContentAdapter extends ArrayAdapter<String> implements
			SectionIndexer {

		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public ContentAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be
			// selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (StringMatcher.match(
									String.valueOf(getItem(j).charAt(0)),
									String.valueOf(k)))
								return j;
						}
					} else {
						if (StringMatcher.match(
								String.valueOf(getItem(j).charAt(0)),
								String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
}