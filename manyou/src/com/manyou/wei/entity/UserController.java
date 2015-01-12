/**
* @Package com.manyou.wei.entity    
* @Title: UserController.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-15 上午12:39:11 
* @version V1.0   
*/
package com.manyou.wei.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.manyou.wei.AppData;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.util.PreferenceUtils;
import com.manyou.wei.vendor.ManyoujiaApi;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-15 上午12:39:11 
 *  
 */
public class UserController {
	
	/**
	 * @Description: TODO
	 * 
	 * @return
	 * @return UserEntity
	 */
	public static UserEntity fetchLoginUser(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.LOGIN_URL, hashMap,
				handler, BaseController.CONTROLLER_LOGIN);
		return null;
	}

	public static UserEntity fetchSignupUser(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
				BaseController.CONTROLLER_SIGNUP);
		return null;
	}


	public static void refreshLocation(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, ManyoujiaApi.USER_LOCATION_REFRESH,
				hashMap, handler, BaseController.CONTROLLER_SIGNUP);
	}

	/**
	 * 通过UserId获取用户信息
	 * 
	 */
	public static void fectchUserById(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, "", hashMap, handler, 0);
	}

	public static void saveSessionId(String sessionId) {
		AsyncHttpLoader.SessionId = sessionId;
		
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_SESSIONID,
				AsyncHttpLoader.SessionId);
	}
	
	public static void saveUser(Context context, String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString).getJSONObject("message");

			MineProvider mine = MineProvider.getInstance();
			
			mine.setUserAvatar(getAvatarDiff(jsonObject.getString("avatar0")));
			
			mine.setUserId(jsonObject.getString("userId"));

			mine.setUserName(jsonObject.getString("username"));
	
			mine.setUserGender(jsonObject.getString("gender"));
			
			mine.setUserResidence(jsonObject.getString("residence"));
			
			mine.setUserBirthday(jsonObject.getString("birthday"));

			// This is very important, you know
			saveSessionId(jsonObject.getString("sessionID"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int getAgeFromDateString(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date old = (Date) sdf.parse(str);
			Date now = new Date();
			long diff = now.getTime() - old.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			int year = (int) days / 365;
			return year + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getAgeFromDate(Date date) {
		Date old = date;
		Date now = new Date();
		long diff = now.getTime() - old.getTime();
		long days = diff / (1000 * 60 * 60 * 24);
		int year = (int) days / 365;
		return year + 1;
	}
	
	/** 
	* @Description: 获取不同分辨率的头像
	*/
	public static String getAvatarDiff(String jsonString){
		return getAvatarDiff(jsonString, 0);
	}
	
	public static String getAvatarDiff(String jsonString, int type){
		//0:avatar  1:thumb
		String avatar = null;
		try {
			avatar = ManyoujiaApi.AVATAR_PREFIX +  new JSONObject(jsonString).getString(
					type==0?"origin":"thumb");
		} catch (JSONException e) {
			e.printStackTrace();
			return "no avatar";
		}
		return avatar;
	}
	

}
