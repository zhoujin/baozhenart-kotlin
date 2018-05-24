package com.markjin.artmall.db.dao;

/**
 * 商品表
 */

public class GoodsDetailDao extends BaseDao {

    @Override
    public String createTable() {
        return "create table if not exists tb_goods_detail(goods_id integer primary key autoincrement UNIQUE," +
                "goods_name TEXT NOT NULL,goods_number NUMBER NOT NULL,cat_id integer NOT NULL, " +
                "cat_name TEXT NOT NULL, brand_name TEXT NOT NULL,img_url TEXT NOT NULL," +
                "img_height REAL,img_width REAL," +
                "brand_id integer NOT NULL, goods_base_price REAL NOT NULL, goods_price REAL NOT NULL, " +
                "market_price REAL NOT NULL, keywords TEXT ," +
                "artist_id integer, artist_name TEXT ," +
                "goods_desc TEXT, is_sale NUMBER NOT NULL DEFAULT 1, add_time DATETIME NOT NULL," +
                "update_time DATETIME NOT NULL,is_delete NUMBER NOT NULL,goods_width REAL NOT NULL," +
                "goods_height REAL NOT NULL,goods_width_mount REAL,goods_height_mount REAL，" +
                "original_goods_width REAL NOT NULL,original_goods_height REAL NOT NULL," +
                "goods_status NUMBER NOT NULL,goods_status_tag TEXT NOT NULL)";
    }
}
