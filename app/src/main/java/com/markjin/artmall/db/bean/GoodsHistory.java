package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 */

@DBTable("tb_goods_history")
public class GoodsHistory {
    public String history_id;
    public String user_id;
    public String goods_id;
    public String goods_name;
    public String artist_name;      //省略先不做
    public String goods_width;
    public String goods_height;
    public String goods_price;
    public String img_url;
    public String goods_status;
    public String goods_status_tag;
    public String add_time;
}
