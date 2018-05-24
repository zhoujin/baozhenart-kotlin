package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

/**
 * 商品图片
 */
@DBTable("tb_goods_images")
public class GoodsImage {
    public String image_id;
    public String goods_id;
    public String img_url;
    public String img_width;
    public String img_height;
    public String img_original_url;

}
