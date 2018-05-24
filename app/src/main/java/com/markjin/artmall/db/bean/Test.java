package com.markjin.artmall.db.bean;

import android.os.Bundle;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.db.dao.BaseDao;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.UserDao;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Administrator on 2017/12/12.
 */

public class Test extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = new User();
        user.user_id = "1";
        user.user_name = "test";
        user.user_password = "123456";
        BaseDao baseDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        baseDao.insert(user);
    }
}
