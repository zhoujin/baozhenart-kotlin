package com.markjin.artmall.db.dao;

/**
 *
 */

public class AddressDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_address(address_id integer primary key autoincrement UNIQUE," +
                "user_id integer NOT NULL,province_id NUMBER NOT NULL,country_id NUMBER NOT NULL, " +
                "district_id NUMBER NOT NULL, address_name TEXT NOT NULL, " +
                "address_phone TEXT NOT NULL, province TEXT NOT NULL, country TEXT NOT NULL, " +
                "add_time TIMESTAMP NOT NULL, update_time DATETIME NOT NULL," +
                "district TEXT NOT NULL, detail_address TEXT NOT NULL, is_default NUMBER DEFAULT 0)";
    }
}
