package com.markjin.artmall.ui;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.User;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.UserDao;
import com.markjin.artmall.utils.MD5Util;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ToastUtil;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private ImageView iv_show_hide;
    private Button bt_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_top_close).setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_password.addTextChangedListener(new PasswordWatcher());
        iv_show_hide = findViewById(R.id.iv_show_hide);
        iv_show_hide.setTag(false);
        iv_show_hide.setOnClickListener(this);
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_close:
                finishActivity(this);
                break;
            case R.id.bt_login:
                userLogin();
                break;
            case R.id.iv_show_hide:
                if ((boolean) iv_show_hide.getTag()) {
                    iv_show_hide.setTag(false);
                    iv_show_hide.setImageResource(R.mipmap.ic_pwd_hide);
                    et_password.setInputType(0x81);
                    et_password.setTypeface(Typeface.DEFAULT);
                    et_password.setSelection(et_password.getText().length());
                } else {
                    iv_show_hide.setTag(true);
                    iv_show_hide.setImageResource(R.mipmap.ic_pwd_show);
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_password.setTypeface(Typeface.DEFAULT);
                    et_password.setSelection(et_password.getText().length());
                }
                break;
            case R.id.tv_register:
                intentActivity(this, RegisterActivity.class);
                break;
        }
    }

    private void userLogin() {
        if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.phone_empty_hint);
            return;
        }
        if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.password_empty_hint);
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
            User where = new User();
            where.user_name = et_username.getText().toString().trim();
            where.user_password = MD5Util.MD5(et_password.getText().toString().trim().getBytes());
            List<User> list = userDao.query(where);
            if (list.size() >= 1) {
                PreferenceUtil.saveUser(list.get(0));
                ToastUtil.showMessage(this, R.string.success_login);
                finishActivity(this);
            } else {
                ToastUtil.showMessage(this, R.string.error_login);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private class PasswordWatcher implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() > 0) {
                bt_login.setBackgroundResource(R.color.colorAccent);
            } else {
                bt_login.setBackgroundResource(R.color.color_e1);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    }
}
