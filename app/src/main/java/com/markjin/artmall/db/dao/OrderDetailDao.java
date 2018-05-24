package com.markjin.artmall.db.dao;

/**
 *
 */

public class OrderDetailDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_order_detail(order_id integer primary key autoincrement UNIQUE," +
                "user_id integer not null,order_sn TEXT(21) not null,address_id integer not null," +
                "address_name TEXT not null,address_phone TEXT not null,address_detail_all TEXT not null," +
                "order_status NUMBER not null,order_status_tag TEXT not null,receive_time DATETIME," +
                "total_goods_price REAL NOT NULL,order_price REAL NOT NULL,invoice_title TEXT,invoice_content TEXT," +
                "logistics_id NUMBER NOT NULL default -1,pay_code TEXT NOT NULL,pay_name TEXT NOT NULL,delivery_type REAL NOT NULL default 1," +
                "delivery_price REAL NOT NULL default 0,pay_time DATETIME ,add_time DATETIME,app_version TEXT NOT NULL" +
                ");";
    }
}
