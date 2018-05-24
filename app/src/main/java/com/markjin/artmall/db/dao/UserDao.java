package com.markjin.artmall.db.dao;


public class UserDao extends BaseDao {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_id integer primary key autoincrement UNIQUE," +
                "user_name TEXT NOT NULL UNIQUE,user_password TEXT NOT NULL," +
                "bind_phone NUMBER(11) ,email TEXT," +
                "gender NUMBER(1) NOT NULL DEFAULT 0,head_image TEXT," +
                "create_time DATETIME NOT NULL )";
    }
}
