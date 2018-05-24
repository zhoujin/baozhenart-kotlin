package com.markjin.artmall.db.bean;

import com.markjin.artmall.db.annotion.DBTable;

@DBTable("tb_user")
public class User {
    public String user_id;
    public String user_name;
    public String user_password;
    public String bind_phone;
    public String email;
    public String gender;
    public String head_image;
    public String create_time;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", bind_phone='" + bind_phone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", head_image='" + head_image + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
