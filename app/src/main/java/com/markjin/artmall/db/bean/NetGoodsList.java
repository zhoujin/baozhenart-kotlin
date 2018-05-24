package com.markjin.artmall.db.bean;

import java.util.List;

/**
 *
 */

public class NetGoodsList {
    public int errorCode;
    public String message;
    public String goodsCount;
    public List<Goods> data;
    public static class Goods{
        public String goods_id;
        public String goods_name;
        public Image goods_image;
        public String enable_resale;    //是否显示转售
        public static class Image{
            public String img_url;
            public String img_width;
            public String img_height;
            public String gallery_img_url;          //单排的图片地址
            public String gallery_img_width;
            public String gallery_img_height;
        }
        public String goods_weight;
        public String market_price;
        public String shop_price;
        public String goods_width;
        public String goods_height;
        public String goods_length;
        public String goods_number;     //产品库存
        public String goods_status;     //产品状态，1是正常销售，2是已售，3是非卖品，4是可议价
        public String goods_status_tag;
        public String artist_name;
        public String exhibition_status;//展览状态，1是不可展览，3是可展览，4是展览中
        public boolean is_in_cart;
        public String exhibition_agency;
        public String exhibition_agency_address;
        public int delivery_type;   //0表示没有发货图标，1表示48小时发货图标

        public String is_derivative;                //是否是衍生品
    }
}
