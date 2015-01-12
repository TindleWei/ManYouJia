/**
* @Package com.manyou.wei.dao    
* @Title: PlansDataHelper.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午11:04:39 
* @version V1.0   
*/
package com.manyou.wei.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.manyou.wei.entity.Chooser;
import com.manyou.wei.entity.PlanEntity;
import com.manyou.wei.util.database.SQLiteTable;
import com.manyou.wei.util.database.Column;

/**
 * Created by Issac on 7/18/13.
 */
public class PlansDataHelper extends BaseDataHelper {
    private Chooser mChooser;

    public PlansDataHelper(Context context, Chooser chooser) {
        super(context);
        mChooser = chooser;
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.SHOTS_CONTENT_URI;
    }

    private ContentValues getContentValues(PlanEntity shot) {
        ContentValues values = new ContentValues();
        values.put(ShotsDBInfo.ID, shot.getPlanId());
        values.put(ShotsDBInfo.CATEGORY, mChooser.ordinal());
        values.put(ShotsDBInfo.JSON, shot.toJson());
        return values;
    }

    public PlanEntity query(long id) {
    	PlanEntity shot = null;
        Cursor cursor = query(null, ShotsDBInfo.CATEGORY + "=?" + " AND " + ShotsDBInfo.ID + "= ?",
                new String[] {
                        String.valueOf(mChooser.ordinal()), String.valueOf(id)
                }, null);
        if (cursor.moveToFirst()) {
            shot = PlanEntity.fromCursor(cursor);
        }
        cursor.close();
        return shot;
    }

    public void bulkInsert(List<PlanEntity> shots) {
    	
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (PlanEntity shot : shots) {
            ContentValues values = getContentValues(shot);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
            DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(ShotsDBInfo.TABLE_NAME, ShotsDBInfo.CATEGORY + "=?", new String[] {
                String.valueOf(mChooser.ordinal())
            });
            return row;
        }
    }

    public CursorLoader getCursorLoader() {

        return new CursorLoader(getContext(), getContentUri(), null, ShotsDBInfo.CATEGORY + "=?",
                new String[] {
            String.valueOf(mChooser.ordinal())
        }, ShotsDBInfo._ID + " ASC");
    }

    public static final class ShotsDBInfo implements BaseColumns {
        private ShotsDBInfo() {
        }

        public static final String TABLE_NAME = "shots";

        public static final String ID = "id";

        public static final String CATEGORY = "category";

        public static final String JSON = "json";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(CATEGORY, Column.DataType.INTEGER).addColumn(JSON, Column.DataType.TEXT);
    }
}

