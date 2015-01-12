/**
* @Package com.manyou.wei    
* @Title: AppData.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午9:48:35 
* @version V1.0   
*/
package com.manyou.wei;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.util.PreferenceUtils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/** 
 *
 * @author firefist_wei
 * @date 2014-9-14 下午9:48:35 
 *  
 */
public class AppData extends Application{
	
	private static AppData instance;
	
	private static Context sContext;
	
	//把下面这些变量放到一个单例的静态类中
	public static String USER_ID = "";
	public static String PLACE_NOW = "";
	public static String LONGITUDE = ""; //精度 108.951933
	public static String LATITUDE = "";  //纬度    34.172777

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
			instance = this;
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
    
    public static AppData getInstance() {
		return instance;
	}

    //把下面的也放到一个静态单例中
    //有Session的RequestQueue
	private static RequestQueue mRequestQueue;
	
	private static DefaultHttpClient mHttpClient;
	
	public RequestQueue getRequestQueue() {
	    // lazy initialize the request queue, the queue instance will be
	    // created when it is accessed for the first time
	    if (mRequestQueue == null) {
	        // Create an instance of the Http client. 
	        // We need this in order to access the cookie store
	        mHttpClient = new DefaultHttpClient();
	        // create the request queue
	        mRequestQueue = Volley.newRequestQueue(this, new HttpClientStack(mHttpClient));
	    }
	    return mRequestQueue;
	}

	/**
	 * Method to set a cookie
	 */
	public void setCookie() {
		org.apache.http.client.CookieStore cs = mHttpClient.getCookieStore();
	    // create a cookie
	    cs.addCookie(new org.apache.http.impl.cookie.BasicClientCookie2("Cookie", "PHPSESSID="
				+ PreferenceUtils.getString(sContext,
						PreferConfig.PREFER_INITIAL_SESSIONID)));
	    Log.e("BUG",PreferenceUtils.getString(sContext,
				PreferConfig.PREFER_INITIAL_SESSIONID));
	}
	
	public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
                String cookie = headers.get(SET_COOKIE_KEY);
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                }
            }
    }
	private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "PHPSESSID=";
	
	 public final void addSessionCookie(Map<String, String> headers) {
	        String sessionId = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_SESSIONID,
					AsyncHttpLoader.SessionId);
	        if (sessionId.length() > 0) {
	            StringBuilder builder = new StringBuilder();
	            builder.append(SESSION_COOKIE);
	            builder.append("=");
	            builder.append(sessionId);
	            if (headers.containsKey(COOKIE_KEY)) {
	                builder.append("; ");
	                builder.append(headers.get(COOKIE_KEY));
	            }
	            headers.put(COOKIE_KEY, builder.toString());
	        }
	    }


}
