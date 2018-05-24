package com.markjin.artmall.db.dao;

/**
 * 商品图片表
 */

public class ImageDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_goods_images(image_id integer primary key autoincrement UNIQUE," +
                "img_url TEXT NOT NULL,goods_id NUMBER NOT NULL,img_original_url TEXT NOT NULL, " +
                "img_width REAL NOT NULL, img_height REAL NOT NULL)";
    }
}
