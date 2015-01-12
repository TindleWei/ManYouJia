/**
 * @Package com.manyouren.android.service.location    
 * @Title: MyLocation.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-9 下午9:31:09 
 * @version V1.0   
 */
package com.manyouren.android.service.location;

import java.util.HashMap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.manyou.wei.AppData;
import com.manyou.wei.config.MineProvider;
import com.manyou.wei.config.PreferConfig;
import com.manyou.wei.data.AsyncHttpLoader;
import com.manyou.wei.entity.BaseController;
import com.manyou.wei.util.PreferenceUtils;
import com.manyou.wei.vendor.ManyoujiaApi;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-9 下午9:31:09
 * 
 */
public class MyLocation {

	private static LocationManagerProxy aMapLocManager = null;

	private static AMapLocation aMapLocation;// 用于判断定位超时
	private static Handler handler = new Handler();

	private static Context mContext = null;

	public static void getLocation(Context context) {
		mContext = context;

		aMapLocManager = LocationManagerProxy.getInstance(context);
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 500, 10,
				aMapLocationListener);
		handler.postDelayed(runnable, 10000);// 设置超过10秒还没有定位到就停止定位
	}

	static AMapLocationListener aMapLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				aMapLocation = location;// 判断超时机制
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				String cityCode = "";
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
						+ "\n精    度    :" + location.getAccuracy() + "米"
						+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
						+ AMapUtil.convertToTime(location.getTime())
						+ "\n城市编码:" + cityCode + "\n位置描述:" + desc + "\n省:"
						+ location.getProvince() + "\n市:" + location.getCity()
						+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
						.getAdCode());
				
				String city = location.getCity();
				if (city.endsWith("市")) {
					city = city.substring(0, city.length() - 1);
					//Log.e("TAG", city);
				}
				
				refreshLocation(geoLat, geoLng ,city);

				MineProvider.getInstance().setUserLatitude(
						String.valueOf(geoLat));

				MineProvider.getInstance().setUserLongitude(
						String.valueOf(geoLng));

				MineProvider.getInstance().setUserLocation(city);

				Log.e("TAG", str);

				stopLocation();

			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
		}

	};

	static Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (aMapLocation == null) {
				stopLocation();
			}
		}
	};

	/**
	 * 销毁定位
	 */
	public static void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(aMapLocationListener);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	private static void refreshLocation(Double geoLat, Double geoLng, String city) {
		if(!MineProvider.getInstance().getUserLatitude().toString().equals(String.valueOf(geoLat))
				|| !MineProvider.getInstance().getUserLongitude().toString().equals(String.valueOf(geoLng))){
			return;
		}
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("UserStatus[latitude]", geoLat);
		hashMap.put("UserStatus[longitude]", geoLng);
		hashMap.put("UserStatus[currentCity]", city);

		AsyncHttpLoader.fetchRequest(mContext,
				ManyoujiaApi.USER_LOCATION_REFRESH, hashMap, handler,
				BaseController.CONTROLLER_USER_LOCATION_REFRESH);

	}

}
