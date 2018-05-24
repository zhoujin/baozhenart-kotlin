package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

@DBTable("tb_goods_category")
public class GoodsCategory {
    public String cat_id;
    public String cat_name;

    public String image_default;
    public String iamge_select;

}
