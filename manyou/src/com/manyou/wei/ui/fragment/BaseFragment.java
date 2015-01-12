/**
* @Package com.manyou.wei.ui.fragment    
* @Title: BaseFragment.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午10:30:49 
* @version V1.0   
*/
package com.manyou.wei.ui.fragment;

import com.android.volley.Request;
import com.manyou.wei.data.RequestManager;

import android.support.v4.app.Fragment;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-14 下午10:30:49 
 *  
 */
public class BaseFragment extends Fragment {


    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }
}

