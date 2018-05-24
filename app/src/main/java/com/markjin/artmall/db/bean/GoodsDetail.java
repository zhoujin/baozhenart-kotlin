package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

import java.io.Serializable;

@DBTable("tb_goods_detail")
public class GoodsDetail implements Serializable {
    public String goods_id;
    public String artist_id;        //省略先不做
    public String artist_name;      //省略先不做
    public String goods_name;
    public String goods_number;

    public String cat_id;  //
    public String cat_name;

    public String brand_id;//
    public String brand_name;

    public String goods_base_price;
    public String market_price;
    public String goods_price;

    public String keywords;
    public String goods_desc;//web html

    public String is_sale;   //0:下架，1：上架
    public String add_time;
    public String update_time;
    public String is_delete;   //1：删除做逻辑删除
    public String goods_width;
    public String goods_height;
    public String goods_width_mount;
    public String goods_height_mount;
    public String original_goods_width;
    public String original_goods_height;
    public String goods_status; //
    public String goods_status_tag;//
    public String img_url;
    public String img_width;
    public String img_height;
}
