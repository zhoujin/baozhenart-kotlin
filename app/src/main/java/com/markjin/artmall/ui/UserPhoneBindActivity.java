package com.markjin.artmall.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.User;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.UserDao;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ToastUtil;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 用户手机绑定
 */

public class UserPhoneBindActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_phone_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bind);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.phone_bind);

        et_phone = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);
        tv_phone_code = findViewById(R.id.tv_phone_code);

        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.bt_bind_phone).setOnClickListener(this);
        tv_phone_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.tv_phone_code://获取验证码
                getCode();
                break;
            case R.id.bt_bind_phone://确认绑定
                bindPhone();
                break;
        }
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_phone_code.setClickable(true);
            tv_phone_code.setText(R.string.get_code_again);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_phone_code.setClickable(false);
            tv_phone_code.setText(getResources().getString(R.string.seconds, String.valueOf(millisUntilFinished / 1000)));
        }
    }

    private void getCode() {
        new MyCount(60000, 1000).start();
        ToastUtil.showMessage(UserPhoneBindActivity.this, R.string.success_code_send_hint);
    }

    private void bindPhone() {
        if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.phone_empty_hint);
            return;
        }
        if (et_phone.getText().toString().trim().length() != 11) {
            ToastUtil.showMessage(this, R.string.phone_error_hint);
            return;
        }
        if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.code_empty_hint);
            return;
        }
        User where = new User();
        where.bind_phone = et_phone.getText().toString().trim();
        UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        List<User> result = userDao.query(where);
        if (result.size() == 0) {
            User userWhere = new User();
            userWhere.user_id = PreferenceUtil.getUserInfo().user_id;
            where.user_id = PreferenceUtil.getUserInfo().user_id;
            int row = userDao.update(where, userWhere);
            if (row == 1) {
                PreferenceUtil.getString("bind_phone", where.bind_phone);
                finishActivity(this);
                ToastUtil.showMessage(this, R.string.success_bind);
            } else {
                ToastUtil.showMessage(this, R.string.error_bind);
            }
        } else {
            ToastUtil.showMessage(this, R.string.phone_bind_already);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
