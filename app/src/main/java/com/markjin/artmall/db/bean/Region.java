package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 *
 */
@DBTable("tb_region")
public class Region {
    public String region_id;
    public String parent_id;
    public String region_name;
    public String region_type;
    public String agency_id;
    public String letter;
}
