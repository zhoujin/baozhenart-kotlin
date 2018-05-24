package com.markjin.artmall.db.bean;

import java.util.List;

/**
 * home banner
 */
public class HomeBanner {
    public int errorCode;
    public List<Banner> data;
    public static class Banner{
        public String link;
        public String type;             //1专题 2：进入web view 3:个人中心 4：作品详情 5：艺术家详情
        public String image_url;
        public String data_id;
    }
}
