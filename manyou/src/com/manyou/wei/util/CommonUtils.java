/**
* @Package com.manyou.wei.util    
* @Title: CommonUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-15 上午12:02:01 
* @version V1.0   
*/
package com.manyou.wei.util;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by Issac on 7/18/13.
 */
public class CommonUtils {
    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}