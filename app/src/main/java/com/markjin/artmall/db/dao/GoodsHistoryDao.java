package com.markjin.artmall.db.dao;

/**
 * Created by Administrator on 2018/2/28.
 */

public class GoodsHistoryDao extends BaseDao{
    @Override
    public String createTable() {
        return "create table if not exists tb_goods_history(history_id integer primary key autoincrement UNIQUE," +
                "goods_id integer not null,user_id integer not null,goods_name TEXT NOT NULL," +
                "artist_name TEXT ,goods_width REAL NOT NULL,goods_height REAL NOT NULL,goods_status NUMBER NOT NULL," +
                "goods_status_tag TEXT NOT NULL,goods_price REAL NOT NULL, img_url TEXT," +
                "add_time DATETIME NOT NULL)";
    }
}
