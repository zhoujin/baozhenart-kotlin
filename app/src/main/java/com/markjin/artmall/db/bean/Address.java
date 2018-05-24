package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

import java.io.Serializable;

@DBTable("tb_address")
public class Address implements Serializable {
    public String user_id;
    public String address_id;
    public String address_name;
    public String address_phone;
    public String province;
    public String country;
    public String district;
    public String province_id;
    public String country_id;
    public String district_id;
    public String detail_address;
    public String is_default; //0否 1是
    public String add_time;
    public String update_time;
}
