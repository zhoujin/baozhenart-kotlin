package com.markjin.artmall.db.dao;

/**
 *
 */

public class OrderListDao extends BaseDao {

    @Override
    public String createTable() {
        return "create view if not exists tb_order_list as select  a.order_id, order_sn,order_status,order_status_tag,logistics_id," +
                "order_price,pay_code,pay_name,goods_name,brand_name,cat_name,goods_width,goods_height,img_url,add_time " +
                "from tb_order_detail a,tb_order_goods b where a.order_id =b.order_id ";
    }
}
