package com.markjin.artmall.db.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.markjin.artmall.db.annotion.DBFiled;
import com.markjin.artmall.db.annotion.DBTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;
    private boolean isInit = false;               //make sure init once time
    private Class<T> entityClass;
    private String tableName;
    private HashMap<String, Field> cacheMap;       //HashMap<String, Field> 表的列名，类的属性 一一对应关系

    protected synchronized boolean init(Class<T> entity, SQLiteDatabase database) {
        if (!isInit) {
            entityClass = entity;
            this.sqLiteDatabase = database;
            if (entity.getAnnotation(DBTable.class) == null) {// if no annotation
                tableName = entity.getClass().getSimpleName();
            } else {
                tableName = entity.getAnnotation(DBTable.class).value();
            }
            if (!database.isOpen()) {
                return false;
            }
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();     //get Table columnNames
            Field[] fields = entityClass.getFields();           //get Class attribution
            for (Field filed : fields) {
                filed.setAccessible(true);                      // if class attribution is private need set true
            }

            for (String name : columnNames) {
                Field valueName = null;
                for (Field field : fields) {
                    String fieldName;
                    if (field.getAnnotation(DBFiled.class) != null) {
                        fieldName = field.getAnnotation(DBFiled.class).value();
                    } else {
                        fieldName = field.getName();
                    }
                    if (name.equals(fieldName)) {     //table columnName = class attribution
                        valueName = field;
                        break;
                    }
                }
                if (valueName != null) {
                    cacheMap.put(name, valueName);
                }
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
    }

    /**
     * @param entity class
     * @return HashMap<String,String> 类的属性，属性值
     */
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> fieldsIterator = cacheMap.values().iterator();
        /**
         * 循环遍历 映射map的  Filed
         */
        while (fieldsIterator.hasNext()) {
            Field colmunToFiled = fieldsIterator.next();
            String cacheKey;
            String cacheValue = null;
            if (colmunToFiled.getAnnotation(DBFiled.class) != null) {
                cacheKey = colmunToFiled.getAnnotation(DBFiled.class).value();
            } else {
                cacheKey = colmunToFiled.getName();
            }
            try {
                if (null == colmunToFiled.get(entity)) {
                    continue;
                }
                cacheValue = colmunToFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }
        return result;
    }

    /**
     * @param map HashMap<String,String> 类的属性，属性值
     * @return ContentValues
     */
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set set = map.keySet();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                sb.append(key).append(":").append(value).append(",");
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    public abstract String createTable();

    @Override
    public long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues contentValues = getContentValues(map);
        Long result = sqLiteDatabase.insert(tableName, null, contentValues);
        return result;
    }

    public boolean sqlByTransaction(List<String> sqls) {
        sqLiteDatabase.beginTransaction();
        try {
            //批量处理操作
            for (int i = 0; i < sqls.size(); i++) {
                sqLiteDatabase.execSQL(sqls.get(i));
            }
            sqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("init db exception==", e.toString());
            return false;
        } finally {
            //结束事务
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public int delete(T entity) {
        Map map = getValues(entity);
        Condition condition = new Condition(map);
        int result = sqLiteDatabase.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
        return result;
    }

    /**
     * @param entity class
     * @param where  bean
     */
    @Override
    public int update(T entity, T where) {
        int result = -1;
        Map values = getValues(entity);
        Map whereClause = getValues(where);
        Condition condition = new Condition(whereClause);
        ContentValues contentValues = getContentValues(values);
        result = sqLiteDatabase.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());
        return result;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(String sql) {
        return null;
    }

    @Override
    public List<T> query(T where, String orderBy, String startIndex, String limit) {
        Map map = getValues(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = sqLiteDatabase.query(true,tableName, null, condition.getWhereClause()
                , condition.getWhereArgs(), null, null, orderBy, limitString);
        List<T> result = getResult(cursor, where);
        cursor.close();
        return result;
    }

    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list = new ArrayList();
        Object item;
        while (cursor.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String colomunName = (String) entry.getKey();
                    Integer colmunIndex = cursor.getColumnIndex(colomunName);
                    Field field = (Field) entry.getValue();
                    Class type = field.getType();
                    if (colmunIndex != -1) {
                        if (type == String.class) {
                            //反射方式赋值
                            field.set(item, cursor.getString(colmunIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(colmunIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(colmunIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(colmunIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(colmunIndex));
                            /*
                            不支持的类型
                             */
                        } else {
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> map) {
            ArrayList list = new ArrayList();
            StringBuilder sb = new StringBuilder();
            sb.append("1=1");
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                sb.append(" and " + entry.getKey() + " =? ");
                list.add(entry.getValue());
            }
            this.whereClause = sb.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
            StringBuilder sbArgs = new StringBuilder();
            for (int i = 0; i < whereArgs.length; i++) {
                sbArgs.append(whereArgs[i]).append("|");
            }
        }

        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }
}
