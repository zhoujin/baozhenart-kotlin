package com.markjin.artmall.db.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class NetGoodsDetail {
    public int errorCode;
    public String message;
    public Detail data;

    public static class Detail implements Serializable {
        public String goods_id;
        public String goods_length;
        public String goods_height;
        public String goods_width;
        public boolean is_mount;            //是否装裱
        public String goods_mount_width;
        public String goods_mount_height;
        public String goods_mount_length;
        public String goods_number;
        public String goods_desc;
        public String description_url;
        public String goods_name;
        public String brand_name;
        public String goods_weight;
        public String goods_img;
        public String img_vague;
        public String shop_price;
        public String category_name;
        public String goods_status;                   //1是正常销售，2是已售，3是非卖品，4是可议价

        public String is_derivative;                //是否是衍生品
        public String goods_status_tag;

        public String mechanism_id;
        public String exhibition_status;                //展览状态，1是不可展览，3是可展览，4是展览中
        public String exhibition_agency;                //展览机构
        public String exhibition_agency_address;        //展览机构详细地址
        public List<String> maintenance_instructions;
        public List<String> installation_instructions;
        public String delivery_type;                    ////发货类型，0是没有，1是48小时发货
        public String edition_time;                     //版次
        public String edition_number;                   //版数
        public String latitude;
        public String longitude;
        public String creation_time;                    //创作时间
        public boolean is_collect;                      //是否收藏
        public boolean is_bonus_enable;                 //是否显示红包
        public String enable_resale;
        public String year_lease_price;
        public String month_lease_price;
        public Artist artist;
        public List<Image> goods_imgs;
        public String display_evaluation;//0隐藏 1显示
        public String goods_score_display;//艺术家评分的 0隐藏 1显示
        public ArtistScore goods_score;
        public Chart chart;
        public Attribute attribute;

        public static class Artist {
            public String art_id;                        //作者id
            public String artist_graduate_school;           //毕业院校 new
            public String artist_name;                      //作者
            public String artist_head_imgurl;               //作者头像 new
            public String goods_count;                      //作品数
            public String visit;                            //浏览数
            public String fans;                             //粉丝
        }

        public static class Image {
            public String thumb_url;                   //缩略图 用户分享//太大微信无法掉起
            public String img_url;
            public String img_height;
            public String img_width;
            public String img_original;
            public String img_vague;                //下载图地址
        }

        public static class ArtistScore {
            public String artistry_score;//艺术性
            public String modality_score;//形式感
            public String creativity_score;//创造性
            public String total_score;//总分数
        }

        public static class Chart implements Serializable {
            public List<String> xdata;
            public List<String> ydata;
            public List<Data> vdata;

            public static class Data implements Serializable {
                public String year_value;
                public String price_value;
            }
        }

        public static class Attribute implements Serializable {

            public List<Data> attribute_info;
            public List<DataPrice> calculate_attribute;

            public static class Data implements Serializable {
                public String attr_id;
                public String attr_name;
                public String display_type;                 //属性显示类型，1是装裱框，2是留白
                public String selected_value_id;
                public List<Value> attr_values;

                public static class Value implements Serializable {
                    public String attr_value_id;
                    public String attr_value_name;
                    public String attr_value_image;
                    public String price;        //no use
                    public boolean is_enabled;          //留白有用
                    public boolean is_default_selected; //是否选中
                    public String tip;                  //留白的尺寸
                    public String display_type;         //1、无框 2、有框
                    public String width_white_ratio;        //留白比例
                    public String height_white_ratio;       //留白比例
                }
            }

            public static class DataPrice implements Serializable {
                public String key;
                public Info value;

                public static class Info implements Serializable {
                    public String goods_width;
                    public String goods_height;
                    public String shop_price;
                }
            }
        }
    }
}
