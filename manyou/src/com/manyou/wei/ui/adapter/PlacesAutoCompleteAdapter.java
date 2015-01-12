/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlacesAutoCOmpleteAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-1 下午4:03:45 
 * @version V1.0   
 */
package com.manyou.wei.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.kevinsawicki.wishlist.Toaster;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.util.AutoCompleteUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-1 下午4:03:45
 * 
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements
		Filterable {

	private ArrayList<String> resultList;

	private Context context = null;

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		
		//be careful
		AutoCompleteUtils.cityMap = new HashMap<String,String>();
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {

					// Retrieve the autocomplete results.
					resultList = AutoCompleteUtils.autocomplete(constraint
							.toString());
					
					if (resultList == null || resultList.size()==0) {
						resultList = new ArrayList<String>();	
						/*resultList.add("西安袁家村");
						resultList.add("西安大雁塔");
						resultList.add("西安曲江游乐场");*/
					}
					resultList.add(0, constraint.toString());
					
					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0)
					notifyDataSetChanged();
				else
					notifyDataSetInvalidated();
			}
		};
		return filter;
	}

	
}
