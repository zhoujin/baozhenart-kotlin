package com.markjin.artmall.db.dao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.markjin.artmall.AppContext;
import com.markjin.artmall.WelcomeActivity;

import java.io.File;

/**
 *
 */

public class BaseDaoFactory {
//    public final String TAG = this.getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;
    private String DATA_BASE_PATH;

    public BaseDaoFactory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            DATA_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mark/";
            File file = new File(DATA_BASE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        openDatabase();
    }

    private void openDatabase() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DATA_BASE_PATH + "mark.db", null);
    }

    /**
     * @param Class<T>       EntityDao
     * @param entityClass<M> BeanClass
     * @return BaseDao
     */
    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> Class, Class<M> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = Class.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
    private static BaseDaoFactory instance = new BaseDaoFactory();

    public static BaseDaoFactory getInstance() {
        return instance;
    }

}
