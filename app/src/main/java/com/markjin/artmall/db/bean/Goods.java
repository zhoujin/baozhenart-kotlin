package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 *
 */

@DBTable("tb_goods")
public class Goods {

    public String goods_id;
    public String goods_name;
    public String goods_price;  //现价
    public String market_price; //市场价
    public String goods_width;
    public String goods_height;
    public String cat_name;
    public String brand_name;
    public String artist_name;
    public String goods_status;
    public String goods_status_tag;
    public String img_url;
}
