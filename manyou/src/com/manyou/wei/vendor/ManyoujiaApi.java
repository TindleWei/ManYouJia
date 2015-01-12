/**
* @Package com.manyou.wei.vendor    
* @Title: ManyoujiaApi.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-15 上午12:03:24 
* @version V1.0   
*/
package com.manyou.wei.vendor;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-15 上午12:03:24 
 *  
 */
public class ManyoujiaApi {
	
	private static final String BASE_URL = "http://manyoujia.sinaapp.com/new/";
	
	public static final String AVATAR_PREFIX = "http://manyoujia-uploads.stor.sinaapp.com/";

//	public static final String BASE_URL = "http://manyoujia.sinaapp.com/new/";

//	public static final String AVATAR_PREFIX = "http://manyoujia-uploads.stor.sinaapp.com";

    public static final String PLANS_LIST = BASE_URL + "index.php/temp";
    
    public static final String PLAN_COMMENTS_LIST = BASE_URL + "index.php/plan/detail";
    
    public static final String PLAN_SEND_COMMENT_URL = BASE_URL
			+ "index.php/plan/comment";
    
    public static final String SIGNUP_URL = BASE_URL
			+ "index.php/user/default/registration";

	public static final String LOGIN_URL = BASE_URL
			+ "index.php/user/default/login";

	//计划模块
	
	public static final String PLAN_FILTER_URL = BASE_URL + "index.php/plan";

	public static final String PLAN_PUBLISH_URL = BASE_URL
			+ "index.php/plan/publish";

	public static final String PLAN_SEARCH_URL = BASE_URL
			+ "index.php/plan/tip";
	
	
	
	
	
	//用户模块

	public static final String USER_PLAN_URL = BASE_URL
			+ "index.php/user/plan";

	public static final String USER_PLAN_DELETE = BASE_URL
			+ "index.php/user/plan/delete";

	public static final String USER_LOCATION_REFRESH = BASE_URL
			+ "index.php/user/refresh";

}
