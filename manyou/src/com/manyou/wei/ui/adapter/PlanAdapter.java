/**
* @Package com.manyou.wei.ui.adapter    
* @Title: PlanAdapter.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午11:43:23 
* @version V1.0   
*/
package com.manyou.wei.ui.adapter;


import java.text.SimpleDateFormat;

import com.android.volley.toolbox.ImageLoader;
import com.manyou.wei.AppData;
import com.manyou.wei.R;
import com.manyou.wei.data.RequestManager;
import com.manyou.wei.entity.PlanController;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.DateUtils;
import com.manyou.wei.util.TimeUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Issac on 7/18/13.
 */
public class PlanAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;

    private ListView mListView;

    private BitmapDrawable mDefaultAvatarBitmap = (BitmapDrawable) AppData.getContext()
            .getResources().getDrawable(R.drawable.gravatar_image);

    public PlanAdapter(Context context, ListView listView) {
        super(context, null, false);
        mLayoutInflater = ((Activity) context).getLayoutInflater();
        mListView = listView;
    }

    @Override
    public PlanEntity getItem(int position) {
        mCursor.moveToPosition(position);
        return PlanEntity.fromCursor(mCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.inflate(R.layout.listitem_plan, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = getHolder(view);

        if (holder.avartarRequest != null) {
            holder.avartarRequest.cancelRequest();
        }

//        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
//                + mListView.getHeaderViewsCount()));

        PlanEntity item = PlanEntity.fromCursor(cursor);
        
        String avatarUrl = UserController.getAvatarDiff(item.getAvatar0());
        
        try{
        	holder.avartarRequest = RequestManager.loadImage(avatarUrl,
                    RequestManager.getImageListener(holder.iv_avatar, mDefaultAvatarBitmap,
                            mDefaultAvatarBitmap));
        }catch(Exception e){
        	Log.e("TAG PlanAdapter",e.toString());
        }
        
        
        String placeStr = item.getDestination();
		if (placeStr.length() > 6) {
			placeStr = placeStr.substring(0, 6) + "···";
		}
        
		if (AppData.PLACE_NOW.equals(item.getCity())) {

			holder.tv_destination.setText(placeStr);
			holder.tv_city.setText("");
		} else {
			
			holder.tv_destination.setText(AppData.PLACE_NOW + " 到 " + item.getCity());
			holder.tv_city.setText("");
		}
		
        holder.tv_destination.setText(item.getDestination());
        
        try {
			holder.tv_date.setText(DateUtils.getPlanDigitalDate(new SimpleDateFormat("yyyy-MM-dd").parse(item
					.getStartDate()),
					new SimpleDateFormat("yyyy-MM-dd").parse(item
							.getEndDate())));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

        holder.tv_note.setText(item.getPostscript());
        
        holder.tv_plan_from.setText("出发城市 " + item.getResidence());

        String for_with_seek = PlanController.list_for.get(item.getType()) +"-"+
        		PlanController.list_with.get(item.getTogether()) +"-"+
        		PlanController.list_seek.get(item.getPurpose());
        holder.tv_type.setText(for_with_seek);
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class Holder {
    	
        public ImageView iv_avatar;

        public TextView tv_destination;

        public TextView tv_date;

        public TextView tv_note;

        public TextView tv_type;

        public TextView tv_plan_from;
        
        public TextView tv_city;

        public ImageLoader.ImageContainer avartarRequest;

        public Holder(View view) {
        	iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        	tv_destination = (TextView) view.findViewById(R.id.tv_destination);
        	tv_date = (TextView) view.findViewById(R.id.tv_date);
        	tv_note = (TextView) view.findViewById(R.id.tv_note);
        	tv_type = (TextView) view.findViewById(R.id.tv_type);
        	tv_plan_from = (TextView) view.findViewById(R.id.tv_plan_from);
        	tv_city = (TextView) view.findViewById(R.id.tv_city);
        }
    }
}

