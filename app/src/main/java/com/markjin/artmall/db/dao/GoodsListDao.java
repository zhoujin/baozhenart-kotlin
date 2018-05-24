package com.markjin.artmall.db.dao;

/**
 * goods list
 */

public class GoodsListDao extends BaseDao {

    @Override
    public String createTable() {
        return "create view if not exists tb_goods as select a.goods_id,goods_name,cat_name,brand_name,goods_price,market_price,goods_status,goods_status_tag,goods_width,goods_height,a.img_url,add_time from tb_goods_detail a,tb_goods_images b where a.goods_id =b.goods_id ";
    }
}
