package com.markjin.artmall.db.bean;

import java.util.List;

/**
 * city
 */
public class AddressCity {
    public String region_id;                  //
    public String parent_id;
    public String region_name;
    public String region_type;                //区分：0国、1省、2市、3（区/县）

    public List<AddressArea> areas;

}
