/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanCommentAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午3:39:52 
 * @version V1.0   
 */
package com.manyou.wei.ui.adapter;

import java.util.List;

import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.manyou.wei.R;
import com.manyou.wei.entity.PicassoService;
import com.manyou.wei.entity.PlanCommentEntity;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.DateUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午3:39:52
 * 
 */
public class PlanCommentAdapter extends
		SingleTypeAdapter<PlanCommentEntity> {

	public PlanCommentAdapter(final LayoutInflater inflater,
			final List<PlanCommentEntity> items) {
		super(inflater, R.layout.plan_comment_item);
		setItems(items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_name, R.id.iv_gender,
				  R.id.tv_content, R.id.tv_time };
	}
	
	@Override
	protected void update(final int position, final PlanCommentEntity item) {
		
		PicassoService.setCirclePhoto(UserController.getAvatarDiff(item.getAvatar()), imageView(0));
		
		setText(1, item.getUserName());
		
		setText(3, item.getContent());
		
		setText(4, DateUtils.convertServerTime(item.getCreateTime()));
		
	}

}
