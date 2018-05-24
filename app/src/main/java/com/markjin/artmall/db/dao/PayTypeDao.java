package com.markjin.artmall.db.dao;

/**
 * pay type dao
 */

public class PayTypeDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_pay_type(pay_id integer primary key autoincrement UNIQUE," +
                "pay_code TEXT not null,pay_name TEXT not null,pay_icon IMAGE" +
                ")";
    }
}
