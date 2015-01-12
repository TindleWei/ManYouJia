/**
* @Package com.manyou.wei.entity    
* @Title: Chooser.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午10:44:56 
* @version V1.0   
*/
package com.manyou.wei.entity;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-14 下午10:44:56 
 *  
 */
public enum Chooser {
	
	All("All"),;
    private String mDisplayName;

    Chooser(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

}
