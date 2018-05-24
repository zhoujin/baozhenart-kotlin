package com.markjin.artmall.db.dao;

/**
 *
 */

public class RegionDao extends BaseDao {
    @Override
    public String createTable() {
        return "create table if not exists tb_region(region_id integer primary key autoincrement UNIQUE," +
                "parent_id NUMBER NOT NULL,region_name TEXT NOT NULL,region_type NUMBER NOT NULL, " +
                "agency_id NUMBER NOT NULL DEFAULT 0, letter CHAR(1) NOT NULL) ";
    }
}
