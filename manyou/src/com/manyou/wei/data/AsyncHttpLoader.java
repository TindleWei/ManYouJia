/**
 * @Package com.manyouren.android.service.http    
 * @Title: AsyncHttpLoader.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-12 上午11:01:25 
 * @version V1.0   
 */
package com.manyou.wei.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.entity.BaseController;
import com.manyou.wei.entity.UserController;
import com.manyou.wei.util.NetworkUtils;
import com.manyou.wei.util.PreferenceUtils;

/**
 * async-http-client
 * 
 * @author firefist_wei
 * @date 2014-6-12 上午11:01:25
 * 
 */
public class AsyncHttpLoader {

	private static AsyncHttpClient client = null;
	private static final String TAG = "AsyncHttpLoader";
	public static String SessionId = null;

	/**
	 * AsyncHttpClient single
	 * 
	 * @return
	 * @return AsyncHttpClient
	 */
	public static AsyncHttpClient getClient(Context context, int backName) {

		/*
		 * if (client == null) {
		 * 
		 * }
		 */
		client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.addHeader("User-Agent", "android");

		if (backName == BaseController.CONTROLLER_LOGIN
				|| backName == BaseController.CONTROLLER_SIGNUP) {

			SessionId = null;
			Log.e("TAG", "Cookie: null");

		} else {

			client.addHeader(
					"Cookie",
					"PHPSESSID="
							+ PreferenceUtils.getString(context,
									PreferConfig.PREFER_INITIAL_SESSIONID));	
			
			Log.e("TAG", "Cookie: " + SessionId);

		}
		return client;
	}

	/**
	 * Post请求数据
	 */
	public static void fetchRequest(final Context context, final String url,
			final HashMap<String, Object> hashMap, final Handler handler,
			final int backName) {

		if (checkNetwork(context) != true) {
			Toast.makeText(context, "网络未连接，请检查网络", 1500).show();
			return;
		}

		if (hashMap != null)
			Log.e("TAG", "上传参数： " + hashMap.toString());
		if (SessionId != null)
			Log.e("TAG", "SESSION: " + SessionId);

		Log.e("TAG", url);

		RequestParams params = new RequestParams();

		// 计划上传图片的特殊处理
		/*
		 * if (backName == BaseController.CONTROLLER_PLAN_PUBLSIH) {
		 * 
		 * if (hashMap.containsKey("image[]")) { //params.put("image[]", (File)
		 * hashMap.get("image[]")); FileBody data = new FileBody((File)
		 * hashMap.get("image[]")); entity.addPart("image[]", data);
		 * hashMap.remove("image[]"); }
		 * 
		 * if (hashMap.containsKey("image[1]")) { //params.put("image[]", (File)
		 * hashMap.get("image[1]")); FileBody data = new FileBody((File)
		 * hashMap.get("image[1]")); entity.addPart("image[]", data);
		 * hashMap.remove("image[1]"); }
		 * 
		 * if (hashMap.containsKey("image[2]")) { //params.put("image[]", (File)
		 * hashMap.get("image[2]")); FileBody data = new FileBody((File)
		 * hashMap.get("image[2]")); entity.addPart("image[]", data);
		 * hashMap.remove("image[2]"); }
		 * 
		 * }
		 */

		Log.e("BUG", "params before: " + params.toString());

		parseHashMap(hashMap, params);

		Log.e("BUG", "上传参数2： " + params.toString());

		getClient(context, backName).post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int code, Header[] header,
							byte[] bytes, Throwable throwable) {
						String str = new String(bytes);
						Log.e("TAG", "post fail");
						if(handler!=null)
						handler.sendEmptyMessage(-1);

					}

					@Override
					public void onSuccess(int code, Header[] header,
							byte[] bytes) {
						String str = new String(bytes);
						if (str != null)
							Log.e("TAG", str);
						try {
							JSONObject response = new JSONObject(str);

							Log.e("TAG", "post success");

							if (backName == BaseController.CONTROLLER_SIGNUP) {

								if (response.getInt("errorCode") == 4) {
									// 注册失败
									handler.sendEmptyMessage(4);
									return;
								} else if (response.getInt("errorCode") == 7) {
									// 邮箱已注册
									handler.sendEmptyMessage(7);
									return;
								}

							} else if (backName == BaseController.CONTROLLER_LOGIN) {

								if (response.getInt("errorCode") == 2) {
									// 用户名或密码错误
									handler.sendEmptyMessage(2);
									return;
								}
							}

							Log.e("TAG", "AsynHttp : 1");
							if (response.getInt("errorCode") == 0) {
								Log.e("TAG", "AsynHttp : 2");
								BaseController.handleBack(context, backName,
										response.toString());
								handler.sendEmptyMessage(1);
								Log.e("TAG", "AsynHttp : 3");
							} else if (response.getInt("errorCode") == 2) {
								// 需要重新登陆
								
							} else if (response.getInt("errorCode") == 3) {
								// {"message":"数据为空","errorCode":3}

								if (backName == BaseController.CONTROLLER_PLAN_FILTER) {
									BaseController.handleBack(context,
											backName, "");
								}
								handler.sendEmptyMessage(3);
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
							Log.e("TAG", "请求异常 " + e1.toString());
							// 异常，当失败处理
							handler.sendEmptyMessage(-1);
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Log.e("TAG", "post finish");
						handler.sendEmptyMessage(0);
					}
				});

	}

	/**
	 * by get method
	 * 
	 * @param context
	 * @param url
	 * @param handler
	 * @param backName
	 * @return void
	 */
	public static String getRequest(final Context context, final String url,
			final Handler handler, final int backName) {

		Log.e("TAG", "session: " + SessionId);

		getClient(context, backName).get(url, null,
				new JsonHttpResponseHandler() {
			
			

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable, errorResponse);
						Log.e("TAG", "get fail");
						if (handler != null)
							handler.sendEmptyMessage(-1);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						
						Log.e("TAG", "get success");
						Log.e("TAG", response.toString());

						try {
							if (response.getInt("errorCode") == 0) {
								BaseController.handleBack(context, backName,
										response.toString());
								if (handler != null)
									handler.sendEmptyMessage(1);

							} else if (response.getInt("eerorCode") == 2) {

							} else {
								if (handler != null)
									handler.sendEmptyMessage(-1);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Log.e("TAG", "get finish");
						if (handler != null)
							handler.sendEmptyMessage(0);
					}
				});

		return null;
	}

	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	private static void specialHandle(RequestParams params, HashMap hashMap,
			int backName) {
		// 计划上传图片的特殊处理
		if (backName == BaseController.CONTROLLER_PLAN_PUBLSIH) {
			if (hashMap.containsKey("images[]")) {
				params.put("images[]", hashMap.get("images[]"));
				params.remove("images[]");
			}
			params.put("images[]", hashMap.get("images[]"));
			if (hashMap.containsKey("images[1]")) {
				params.put("images[1]", hashMap.get("images[1]"));
				params.remove("images[1]");
			}

			if (hashMap.containsKey("images[2]")) {
				params.put("images[2]", hashMap.get("images[2]"));
				params.remove("images[2]");
			}
		}
	}


	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	private static boolean checkNetwork(Context context) {

		NetworkUtils.NetWorkState state = new NetworkUtils(context)
				.getConnectState();
		if (state.equals(NetworkUtils.NetWorkState.NONE)) {
			Log.e("TAG", "网络未连接");
			return false;
		} else {
			Log.e("TAG", "网络正常");
			return true;
		}
	}

	/**
	 * convert hashMap key and value to requestParams
	 * 
	 * @return void
	 */
	private static void parseHashMap(HashMap<String, Object> hashMap,
			RequestParams params) {

		if (hashMap != null) {
			Iterator<Map.Entry<String, Object>> it = hashMap.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
						.next();
				if (entry.getValue() instanceof java.io.File) {
					Log.e("TAG", "there ia a file: " + entry.getKey());
					try {
						params.put(entry.getKey(), (File) entry.getValue());

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else if (entry.getValue() instanceof java.util.Date) {
					Log.e("TAG", "there ia a date: " + entry.getKey());
					params.put(entry.getKey(), (Date) entry.getValue());
				} else {
					params.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

}
