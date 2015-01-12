/**
 * @Package com.manyou.wei.ui.adapter    
 * @Title: DrawerAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-14 下午10:55:29 
 * @version V1.0   
 */
package com.manyou.wei.ui.adapter;

import java.util.List;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.entity.Chooser;
import com.manyou.wei.entity.PicassoService;
import com.manyou.wei.entity.PlanController;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.DateUtils;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Issac on 7/18/13.
 */
public class DrawerAdapter extends SingleTypeAdapter<PlanEntity> {

	Context context = null;

	public DrawerAdapter(final LayoutInflater inflater,
			final List<PlanEntity> items) {
		super(inflater, R.layout.listitem_drawer);
		setItems(items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.tv_destination, R.id.tv_startdate,
				R.id.tv_plan_type, R.id.tv_createTime };
	}

	@Override
	protected void update(int position, PlanEntity item) {

		String placeStr = item.getDestination();
		if (placeStr.length() > 6) {
			placeStr = placeStr.substring(0, 6) + "···";
		}

		setText(0, placeStr);

		setText(1,
				DateUtils.getPlanDigitalDate(item.getStartDate(),
						item.getEndDate()));
		String for_with_seek = PlanController.list_for.get(item.getType())
				+ "-" + PlanController.list_with.get(item.getTogether()) + "-"
				+ PlanController.list_seek.get(item.getPurpose());
		setText(2, for_with_seek);

		setText(3, "" + DateUtils.convertServerTime(item.getCreateTime()));

	}
}
