package com.markjin.artmall.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * register/editor
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_register_editor;
    private AppCompatEditText et_email, et_user_name, et_password;
    private RelativeLayout rl_phone_bind;
    private AppCompatTextView tv_bind_phone;
    private RadioGroup rg_gender;
    private ImageView iv_user_header, iv_phone_bind_arrow;
    private String gender = "0";
    private int register_editor = 0; //0:默认注册   1:编辑
    private String header_path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_register_close).setOnClickListener(this);
        tv_register_editor = findViewById(R.id.tv_register_editor);
        et_user_name = findViewById(R.id.et_user_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password_register);
        rg_gender = findViewById(R.id.rg_gender);
        rl_phone_bind = findViewById(R.id.rl_phone_bind);
        tv_bind_phone = findViewById(R.id.tv_bind_phone);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male) {
                    gender = "0";
                } else if (checkedId == R.id.rb_female) {
                    gender = "1";
                }
            }
        });
        iv_user_header = findViewById(R.id.iv_user_header);
        iv_phone_bind_arrow = findViewById(R.id.iv_phone_bind_arrow);
        findViewById(R.id.bt_register).setOnClickListener(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("editor")) {
            rl_phone_bind.setVisibility(View.VISIBLE);
            tv_register_editor.setText(R.string.editor);
            register_editor = 1;
            et_password.setVisibility(View.GONE);
            if (PreferenceUtil.isLogin()) {
                et_user_name.setText(PreferenceUtil.getUserInfo().user_name);
                et_email.setText(PreferenceUtil.getUserInfo().email);
                if (!TextUtils.isEmpty(PreferenceUtil.getUserInfo().bind_phone)) {
                    tv_bind_phone.setText(PreferenceUtil.getUserInfo().bind_phone);
                    iv_phone_bind_arrow.setVisibility(View.INVISIBLE);
                } else {
                    iv_phone_bind_arrow.setVisibility(View.VISIBLE);
                }
            }
        } else {
            tv_register_editor.setText(R.string.register);
            et_password.setVisibility(View.VISIBLE);
            rl_phone_bind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_close:
                finishActivity(this);
                break;
            case R.id.bt_register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        User user = new User();
        if (TextUtils.isEmpty(et_user_name.getText().toString().trim())) {
            ToastUtil.showMessage(this, R.string.phone_empty_hint);
            return;
        }
        user.user_name = et_user_name.getText().toString().trim();
        if (!TextUtils.isEmpty(et_email.getText().toString().trim())) {
            user.email = et_email.getText().toString().trim();
        }
        user.gender = gender;
        UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        if (register_editor == 0) {
            if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
                ToastUtil.showMessage(this, R.string.password_empty_hint);
                return;
            }
            if (et_password.getText().toString().trim().length() < 6) {
                ToastUtil.showMessage(this, R.string.password_short_hint);
                return;
            }
            if (et_password.getText().toString().trim().length() > 18) {
                ToastUtil.showMessage(this, R.string.password_length_hint);
                return;
            }
            user.create_time = String.valueOf(System.currentTimeMillis());
            user.user_password = MD5Util.MD5(et_password.getText().toString().trim().getBytes());
            User check = new User();
            check.user_name = user.user_name;
            List<User> result = userDao.query(check);
            if (result != null && result.size() == 0) {
                userDao.insert(user);
                User where = new User();
                where.user_name = user.user_name;
                where.user_password = user.user_password;
                List<User> users = userDao.query(where);
                if (users.size() == 1) {
                    PreferenceUtil.saveUser(users.get(0));
                    finishActivity(RegisterActivity.this);
                    ToastUtil.showMessage(RegisterActivity.this, R.string.success_register);
                }
            } else {
                ToastUtil.showMessage(RegisterActivity.this, R.string.user_already_register);
            }
        } else {
            user.user_id = PreferenceUtil.getUserInfo().user_id;
            header_path = PreferenceUtil.getString("head_image");
            User where = new User();
            where.user_id = PreferenceUtil.getUserInfo().user_id;
            int row = userDao.update(user, where);
            if (row == 1) {
                ToastUtil.showMessage(this, R.string.success_update);
                finishActivity(this);
            } else {
                ToastUtil.showMessage(this, R.string.error_update);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
