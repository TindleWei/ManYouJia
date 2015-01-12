/**   
 * @Title: PlanEntity.java 
 * @Package com.manyouren.android.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-7 下午12:22:03 
 * @version V1.0   
 */
package com.manyou.wei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.database.Cursor;

import com.google.gson.Gson;
import com.manyou.wei.dao.PlansDataHelper;

/**
 * @ClassName: PlanEntity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author firefist_wei firefist.wei@gmail.com
 * @date 2014-6-7 下午12:22:03
 * 
 */
public class PlanEntity implements Serializable {
	
	private static final HashMap<Long, PlanEntity> CACHE = new HashMap<Long, PlanEntity>();

	/** serialVersionUID */
	private static final long serialVersionUID = 4759542049129654659L;

	private long planId;
	
	private long userId;
	
	private long createTime;

	private String destination;
	
	private String city;
	
	private String startDate;
	
	private String endDate;
	
	private int vehicle;
	
	private int type;
	
	private int together;
	
	private int purpose;
	
	private String images;
	
	private String flightNumber;
	
	private String postscript;
	
	private int validate;
	
	private int likeThis;
	
	//user 
	private String username;
	
	private String birthday;
	
	private int gender;
	
	private String residence;
	
	private String avatar0;
	

	private static void addToCache(PlanEntity shot) {
        CACHE.put(shot.getPlanId(), shot);
    }

    private static PlanEntity getFromCache(long id) {
        return CACHE.get(id);
    }

    public static PlanEntity fromJson(String json) {
        return new Gson().fromJson(json, PlanEntity.class);
    }

    public static PlanEntity fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(PlansDataHelper.ShotsDBInfo._ID));
        // error 
//        PlanEntity shot = getFromCache(id);
//        if (shot != null) {
//            return shot;
//        }
        PlanEntity shot = new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(PlansDataHelper.ShotsDBInfo.JSON)),
                PlanEntity.class);
        addToCache(shot);
        return shot;
    }
	
	public PlanEntity(){
		
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the residence
	 */
	public String getResidence() {
		return residence;
	}

	/**
	 * @param residence the residence to set
	 */
	public void setResidence(String residence) {
		this.residence = residence;
	}

	/**
	 * @return the avatar0
	 */
	public String getAvatar0() {
		return avatar0;
	}

	/**
	 * @param avatar0 the avatar0 to set
	 */
	public void setAvatar0(String avatar0) {
		this.avatar0 = avatar0;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the planId
	 */
	public long getPlanId() {
		return planId;
	}

	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(long planId) {
		this.planId = planId;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getVehicle() {
		return vehicle;
	}

	public void setVehicle(int vehicle) {
		this.vehicle = vehicle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTogether() {
		return together;
	}

	public void setTogether(int together) {
		this.together = together;
	}

	public int getPurpose() {
		return purpose;
	}

	public void setPurpose(int purpose) {
		this.purpose = purpose;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public int getValidate() {
		return validate;
	}

	public void setValidate(int validate) {
		this.validate = validate;
	}

	public int getLikeThis() {
		return likeThis;
	}

	public void setLikeThis(int likeThis) {
		this.likeThis = likeThis;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public static class ShotsRequestData {
        private int page;

        private int pages;

        private int per_page;

        private int total;

        private ArrayList<PlanEntity> shots;

        public int getPage() {
            return page;
        }

        public int getPages() {
            return pages;
        }

        public int getPer_page() {
            return per_page;
        }

        public int getTotal() {
            return total;
        }

        public ArrayList<PlanEntity> getShots() {
            return shots;
        }
    }
	
	 public String toJson() {
	        return new Gson().toJson(this);
	    }

}
