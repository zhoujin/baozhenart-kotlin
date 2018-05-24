package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 */
@DBTable("tb_order_list")
public class OrderList {
    public String order_id;
    public String user_id;
    public String order_sn;
    public String order_status;         //1：未付款 2：已付款 3：已发货 4：已收货 5：已取消 6：已删除
    public String order_status_tag;     //1：待付款 2:待发货  3：待收货 4：已完成 5：已取消
    public String total_goods_price;
    public String order_price;
    public String pay_code;
    public String pay_name;
    public String logistics_id;         //物流信息
    public String add_time;             //创建时间
    public String img_url;
    public String goods_name;
    public String brand_name;
    public String cat_name;
    public String goods_width;
    public String goods_height;

}
