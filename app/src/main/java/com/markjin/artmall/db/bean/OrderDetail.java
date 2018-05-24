package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 *
 */

@DBTable("tb_order_detail")
public class OrderDetail {
    public String order_id;
    public String user_id;
    public String order_sn;
    public String address_id;
    public String address_name;
    public String address_phone;
    public String address_detail_all;//省市区+详情地址
    public String order_status;         //1：未付款 2：已付款 3：已发货 4：已收货 5：已取消 6：已删除
    public String order_status_tag;     //1：待付款 2:待发货  3：待收货 4：已完成 5：已取消
    public String receive_time;
    public String total_goods_price;
    public String order_price;
    public String invoice_title;
    public String invoice_content;
    public String logistics_id;
    public String pay_code;
    public String pay_name;
    public String delivery_price;    //配送费用
    public String delivery_type;    //配送类型 1：物流配送 2 自提
    public String pay_time;
    public String add_time;
    public String app_version;
}
