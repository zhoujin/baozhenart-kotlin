package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 *
 */

@DBTable("tb_order_goods")
public class OrderGoods {
    public String order_goods_id;
    public String order_id;
    public String goods_id;
    public String goods_name;
    public String artist_name;
    public String goods_price;
    public String brand_name;
    public String cat_name;
    public String goods_width;
    public String goods_height;
    public String img_url;
    public String goods_status;
    public String goods_status_tag;

}
