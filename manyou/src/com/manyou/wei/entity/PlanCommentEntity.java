/**
* @Package com.manyouren.android.entity    
* @Title: PlanCommentEntity.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-4 下午3:26:09 
* @version V1.0   
*/
package com.manyou.wei.entity;

import java.io.Serializable;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-4 下午3:26:09 
 *  
 */
public class PlanCommentEntity implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 7500691663211390285L;

	private long commentId;
	
	private long planId;
	
	private long createTime;
	
	private long userId;
	
	private String content;
	
	private long replyId;
	
	private String avatar0;
	
	private String username;
	
	public PlanCommentEntity(){
		
	}
	
	/** 
	* Description: 
	*
	* @param commentId
	* @param planId
	* @param createTime
	* @param userId
	* @param content
	* @param replyId
	* @param avatar0
	* @param username 
	*/
	public PlanCommentEntity(long commentId, long planId, long createTime,
			long userId, String content, long replyId, String avatar0,
			String username) {
		super();
		this.commentId = commentId;
		this.planId = planId;
		this.createTime = createTime;
		this.userId = userId;
		this.content = content;
		this.replyId = replyId;
		this.avatar0 = avatar0;
		this.username = username;
	}



	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar0;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar0 = avatar;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return username;
	}



	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.username = userName;
	}



	/**
	 * @return the commentId
	 */
	public long getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(long commentId) {
		this.commentId = commentId;
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
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the replyId
	 */
	public long getReplyId() {
		return replyId;
	}

	/**
	 * @param replyId the replyId to set
	 */
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}

}
