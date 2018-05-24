package com.markjin.artmall.db.dao;

/**
 * 订单中 作品列表
 */

public class OrderGoodsDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_order_goods (order_goods_id integer primary key autoincrement UNIQUE," +
                "order_id integer not null,goods_id integer not null,goods_name TEXT NOT NULL,goods_price REAL NOT NULL, " +
                "cat_name TEXT NOT NULL, brand_name TEXT NOT NULL,img_url TEXT ," +
                "goods_width REAL NOT NULL,goods_height REAL NOT NULL,artist_name TEXT ,goods_status NUMBER NOT NULL,goods_status_tag TEXT NOT NULL" +
                ")";
    }
}
