package com.markjin.artmall.db.dao;

/**
 *
 */

public class GoodsCartDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_goods_cart(cart_id integer primary key autoincrement UNIQUE," +
                "goods_id integer not null,user_id integer not null,goods_name TEXT NOT NULL,artist_name TEXT ," +
                "goods_width REAL NOT NULL,goods_height REAL NOT NULL,goods_status NUMBER NOT NULL," +
                "cat_name TEXT , brand_name TEXT ,goods_number Number DEFAULT 1," +
                "goods_status_tag TEXT NOT NULL,goods_price REAL NOT NULL,cart_type NUMBER NOT NULL DEFAULT 1," +
                "img_url TEXT,img_width REAL NOT NULL,img_height REAL NOT NULL," +
                "add_time DATETIME NOT NULL)";
    }
}
