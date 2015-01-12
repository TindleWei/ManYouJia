/**
 * @Package com.manyouren.android.controller    
 * @Title: PlanController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午3:56:28 
 * @version V1.0   
 */
package com.manyou.wei.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.vendor.ManyoujiaApi;


/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午3:56:28
 * 
 */
public class PlanController {
	
	public static String[] DayOfWeek = { "星期天", "星期一", "星期二", "星期三", "星期四",
		"星期五", "星期六" };

	public static List<String> list_for = new ArrayList<String>();
	static {
		list_for.add("游玩");
		list_for.add("休闲");
		list_for.add("度假");
	}
	
	public static String[] tip_for = {
		"游览赏玩,高兴就好",
		"身心的调节与放松，是一种心灵的体验",
		"以度假和休闲为主要目的假日外出"
	};


	public static List<String> list_with = new ArrayList<String>();
	static {
		list_with.add("自己");
		list_with.add("朋友");
		list_with.add("情侣");
	}
	
	public static String[] tip_with = {
		"自己一个人独自旅行",
		"与朋友一起，希望结识更多的人",
		"情侣，可以与其他情侣结伴同行",
	};

	public static List<String> list_seek = new ArrayList<String>();
	static {
		list_seek.add("建议");
		list_seek.add("伙伴");
		list_seek.add("搭车");
	}
	
	public static String[] tip_seek = {
		"希望得到旅行的建议和帮助",
		"寻找一起出行的伙伴",
		"可以拼车一同出行",
	};

	/**
	 * 将json中的images路径存入List
	 * 
	 */
	public static List<String> getPlanImages(String jsonString) {
		// 0:origin 1:thumb
		List<String> list = new ArrayList<String>();
		try {

			JSONArray imagesArray = new JSONObject(jsonString)
					.getJSONArray("origin");
			
			for (int i = 0; i < imagesArray.length(); i++) {
				list.add(ManyoujiaApi.AVATAR_PREFIX + imagesArray.getString(i));
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 用来判断是否分页
		public static Boolean isFirstPage = true;

		// 某个计划的评论数据
		public static List<PlanCommentEntity> commentList = new ArrayList<PlanCommentEntity>();

		public static List<PlanEntity> userplanList = new ArrayList<PlanEntity>();
		/**
		 * 发布计划
		 */
		public static void fetchPublishPlan(Context context, String url,
				HashMap<String, Object> hashMap, Handler handler) {
			AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
					BaseController.CONTROLLER_PLAN_PUBLSIH);
		}

		/**
		 * 获取计划
		 */
		public static boolean fetchFilterPlan(Context context, String suffixUrl,
				HashMap<String, Object> hashMap, Handler handler) {

			if (suffixUrl == null) {
				isFirstPage = true;
				AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.PLAN_FILTER_URL,
						hashMap, handler, BaseController.CONTROLLER_PLAN_FILTER);

			} else {
				isFirstPage = false;
				AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.PLAN_FILTER_URL
						+ suffixUrl, hashMap, handler,
						BaseController.CONTROLLER_PLAN_FILTER);
			}
			return true;
		}

		/**
		 * 获取某个用户的计划
		 */
		public static boolean fetchUserPlan(Context context,
				HashMap<String, Object> hashMap, Handler handler) {
			AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.USER_PLAN_URL,
					hashMap, handler, BaseController.CONTROLLER_USER_PLANS);
			return true;
		}

		/**
		 * 删除计划
		 */
		public static boolean deletePlan(Context context,
				HashMap<String, Object> hashMap, Handler handler) {
			AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.USER_PLAN_DELETE,
					hashMap, handler, BaseController.CONTROLLER_DELETE_PLAN);
			return true;
		}

	

		/**
		 * 发送评论
		 */
		public static boolean sendPlanComment(Context context,
				HashMap<String, Object> hashMap, Handler handler) {
			AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.PLAN_SEND_COMMENT_URL,
					hashMap, handler, BaseController.CONTROLLER_PLAN_SEND_COMMENT);
			return true;
		}



		public static void parseCommentJson(Context context, String jsonString) {
			String json = null;
			try {
				json = (new JSONObject(jsonString).getJSONArray("message")).toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Gson gson = new Gson();
			commentList = gson.fromJson(json,
					new TypeToken<List<PlanCommentEntity>>() {
					}.getType());

		}
		
		public static void parseUserPlanJson(Context context, String jsonString) {
			String json = null;
			try {
				json = (new JSONObject(jsonString).getJSONArray("message")).toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Gson gson = new Gson();
			userplanList = gson.fromJson(json,
					new TypeToken<List<PlanEntity>>() {
					}.getType());

		}


}
