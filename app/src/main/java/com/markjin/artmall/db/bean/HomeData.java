package com.markjin.artmall.db.bean;

import java.util.List;

/**
 * 首页数据
 */

public class HomeData {
    public int errorCode;
    public String message;
    public Data data;

    public static class Data {

        public List<Recommend> daily_recommend;
        public List<Artist> recommend_artist;
        public List<Subject> recommend_subject;
        public List<HotLabel> recommend_label;
        public List<Soft> recommend_decoration;
        public List<NewGoods> new_goods;
        public List<Video> recommend_video;

        public static class Recommend {
            public String goods_id;
            public String goods_height;
            public String goods_width;
            public String goods_length;
            public String goods_name;
            public String shop_price;
            public String goods_desc;
            public String goods_status;
            public String goods_status_tag;
            public Image goods_image;
            public String artist_name;
            public String recommend_content;
            public String recommend_month;
            public String recommend_month_text;
            public String recommend_day;
            public String recommend_id;
            public String recommend_type;   //类型  1画作 2展览 3名画
            public String recommend_name;   //type类型  1画作 2展览
            public String recommend_type_image;

            public static class Image {
                public String img_width;
                public String img_url;
                public String img_height;
                public String thumb_url;                        //缩略图 用户分享//太大微信无法掉起
                public String img_original;
                public String original_goods_width;
                public String original_goods_height;
            }
        }

        public static class Artist {
            public String artist_id;
            public String artist_name;                          //艺术家名
            public String visit_count;                          //访问数
            public String fans;                                 //粉丝/关注数
            public String head_imgurl;
            public String graduate_school;                      //毕业学校
            public String goods_count;                             //商品数量
            public boolean is_attention;                        //是否关注
            public List<Goods> goods_list;

            public static class Goods {
                public String goods_id;
                public Image goods_image;

                public static class Image {
                    public String img_url;
                    public String img_width;
                    public String img_height;
                }
            }
        }

        public static class Subject {
            public String type;//该对象类型，1是正常的专题对象，2是推荐作品对象，作品数据在recommend_data数组里
            public String subject_id;
            public String title;
            public String description;
            public String image_url;
        }

        public static class HotLabel {
            public String label_id;
            public String label_name;
            public String label_desc;
            public String label_image;
            public String label_type; //1：标签 2：关键字
        }

        public static class Soft {
            public String decoration_id;
            public String style_name;
            public String style_desc;
            public String style_image;
            public List<Goods> goods_list;

            public static class Goods {
                public String goods_id;
                public String goods_name;
                public Image goods_image;
                public String shop_price;
                public String market_price;
                public String goods_status;
                public String goods_status_tag;

                public static class Image {
                    public String img_url;
                    public String gallery_img_url;
                }
            }
        }

        public static class NewGoods {
            public String goods_id;
            public String goods_name;
            public String shop_price;
            public Image goods_image;
            public String goods_status;                   //1是正常销售，2是已售，3是非卖品，4是可议价
            public String goods_status_tag;

            public static class Image {
                public String img_url;
                public String img_width;
                public String img_height;
            }
        }

        public static class Video {
            public String video_id;
            public String video_url;
            public String video_image;
            public String video_type;   //1艺术家 2：专题 3：其他
            public String video_name;
            public String video_intro;
            public String relation_id;  //艺术家id 或专题id
        }
    }
}
