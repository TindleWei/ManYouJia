/**
 * @Package com.manyouren.android.config    
 * @Title: Constants.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-16 下午1:43:41 
 * @version V1.0   
 */
package com.manyou.wei.config;

import com.manyou.wei.AppData;
import com.manyou.wei.util.PreferenceUtils;

/**
 * ManYouRen Constants
 * 
 * @author firefist_wei
 * @date 2014-6-16 下午1:43:41
 * 
 */
public class MineProvider {

	private String userId = "";

	private String userName = "";

	private String userAvatar = ""; // 直接存地址

	private String userBirthday = "";

	private String userResidence = "";

	private String userGender = "";

	private String userLocation = "";

	private String userLatitude = "";

	private String userLongitude = "";

	private MineProvider() {
	}

	private static MineProvider instance = null;

	public static synchronized MineProvider getInstance() {
		if (instance == null) {
			instance = new MineProvider();
		}
		return instance;
	}

	/**
	 * @return the userId
	 */
	public String getUserId(String user_id) {
		if (userId.equals("")) {
			userId = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_USERID);
		}
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_USERID, userId);
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		if (userName.equals("")) {
			userName = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_USERNAME);
		}
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_USERNAME, userName);
		this.userName = userName;
	}

	/**
	 * @return the userAvatar
	 */
	public String getUserAvatar() {
		if (userAvatar.equals("")) {
			userAvatar = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_USERAVATAR);
		}
		return userAvatar;
	}

	/**
	 * @param userAvatar
	 *            the userAvatar to set
	 */
	public void setUserAvatar(String userAvatar) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_USERAVATAR, userAvatar);
		this.userAvatar = userAvatar;
	}

	/**
	 * @return the userBirthday
	 */
	public String getUserBirthday() {
		if (userBirthday.equals("")) {
			userBirthday = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_USERBIRTHDAY);
		}
		return userBirthday;
	}

	/**
	 * @param userBirthday
	 *            the userBirthday to set
	 */
	public void setUserBirthday(String userBirthday) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_USERBIRTHDAY, userBirthday);
		this.userBirthday = userBirthday;
	}

	/**
	 * @return the userResidence
	 */
	public String getUserResidence() {
		if (userResidence.equals("")) {
			userResidence = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_RESIDENCE);
		}
		return userResidence;
	}

	/**
	 * @param userResidence
	 *            the userResidence to set
	 */
	public void setUserResidence(String userResidence) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_RESIDENCE, userResidence);
		this.userResidence = userResidence;
	}

	/**
	 * @return the userGender
	 */
	public String getUserGender() {
		if (userGender.equals("")) {
			userGender = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_GENDER);
		}
		return userGender;
	}

	/**
	 * @param userGender
	 *            the userGender to set
	 */
	public void setUserGender(String userGender) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_GENDER, userGender);
		this.userGender = userGender;
	}

	/**
	 * @return the userLocation
	 */
	public String getUserLocation() {
		if (userLocation.equals("")) {
			userLocation = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_LOCATION,
					DefaultConfig.defaultLocation);
		}
		return userLocation;
	}

	/**
	 * @param userLocation
	 *            the userLocation to set
	 */
	public void setUserLocation(String userLocation) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_LOCATION, userLocation);
		this.userLocation = userLocation;
	}

	/**
	 * @return the userLatitude
	 */
	public String getUserLatitude() {
		if (userLatitude.equals("")) {
			userLatitude = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_LATITUDE,
					DefaultConfig.defaultLatitude);
		}
		return userLatitude;
	}

	/**
	 * @param userLatitude
	 *            the userLatitude to set
	 */
	public void setUserLatitude(String userLatitude) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_LATITUDE, userLatitude);
		this.userLatitude = userLatitude;
	}

	/**
	 * @return the userLongitude
	 */
	public String getUserLongitude() {
		if (userLongitude.equals("")) {
			userLongitude = PreferenceUtils.getString(AppData.getContext(),
					PreferConfig.PREFER_INITIAL_LONGITUDE,
					DefaultConfig.defaultLongitude);
		}
		return userLongitude;
	}

	/**
	 * @param userLongitude
	 *            the userLongitude to set
	 */
	public void setUserLongitude(String userLongitude) {
		PreferenceUtils.putString(AppData.getContext(),
				PreferConfig.PREFER_INITIAL_LONGITUDE, userLongitude);
		this.userLongitude = userLongitude;
	}

}
