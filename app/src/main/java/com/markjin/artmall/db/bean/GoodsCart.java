package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 *
 */
@DBTable("tb_goods_cart")
public class GoodsCart {
    public String cart_id;
    public String user_id;
    public String goods_id;
    public String goods_number;
    public String goods_name;
    public String goods_width;
    public String goods_height;
    public String cat_name;
    public String brand_name;
    public String img_url;
    public String img_width;
    public String img_height;
    public String artist_name;      //省略先不做
    public String goods_price;
    public String goods_status;
    public String goods_status_tag;
    public String cart_type;
    public String add_time;
}
