package com.markjin.artmall.db.bean;

import java.util.List;

/**
 *  Province
 */
public class AddressProvince {
    public String region_id;
    public String parent_id;
    public String region_name;
    public String region_type;
    public List<AddressCity> citys;
}
