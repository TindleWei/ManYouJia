/**
 * @Package com.manyouren.android.controller    
 * @Title: BaseContoller.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-2 下午10:18:18 
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
import android.util.Log;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-2 下午10:18:18
 * 
 */
public class BaseController {
	
	//登陆模块

	public static final int CONTROLLER_SIGNUP = 1;

	public static final int CONTROLLER_LOGIN = 2;
	
	//计划模块

	public static final int CONTROLLER_PLAN_PUBLSIH = 3;

	//public static final int CONTROLLER_PLAN_SHOW = 4;
	
	public static final int CONTROLLER_PLAN_FILTER = 5;
	
	public static final int CONTROLLER_PLAN_COMMENT = 51;
	
	public static final int CONTROLLER_PLAN_SEND_COMMENT = 52;

	
	
	//用户模块

	public static final int CONTROLLER_USER_PLANS = 8;

	public static final int CONTROLLER_PLAN_NEXT = 9;
	
	public static final int CONTROLLER_DELETE_PLAN = 13;
	
	public static final int CONTROLLER_USER_LOCATION_REFRESH = 99;
	

	/**
	 * @Description: TODO
	 * 
	 * @param requestName
	 * @return void
	 */
	public static void handleBack(Context context, int backName, String result) {
		switch (backName) {
		case CONTROLLER_SIGNUP:
			UserController.saveUser(context, result);
			
			break;
		case CONTROLLER_LOGIN:
			UserController.saveUser(context, result);

			break;
		case CONTROLLER_PLAN_PUBLSIH:

			break;
			
		case CONTROLLER_PLAN_FILTER:
			
			break;
			
		case CONTROLLER_PLAN_NEXT:
			break;


		case CONTROLLER_USER_PLANS:
			PlanController.parseUserPlanJson(context, result);
			break;

		case CONTROLLER_DELETE_PLAN:
			break;

			
		case CONTROLLER_PLAN_SEND_COMMENT:
			
			break;
			
		case CONTROLLER_USER_LOCATION_REFRESH:
			
			break;

		default:
			break;
		}

	}

}
