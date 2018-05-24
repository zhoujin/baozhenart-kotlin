package com.markjin.artmall.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.User;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.db.dao.UserDao;
import com.markjin.artmall.utils.PreferenceUtil;

import java.util.List;

/**
 *
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private SwipeRefreshLayout refresh_layout;
    private TextView tv_top_title, tv_login_register;
    private ImageView iv_user_header;
    private LinearLayoutCompat ll_phone_bind, ll_admin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceUtil.isLogin()) {
            refreshLoginUser();
        } else {
            tv_top_title.setText(R.string.title_user);
            tv_login_register.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_top_title = view.findViewById(R.id.tv_title);
        tv_login_register = view.findViewById(R.id.tv_login_register);
        iv_user_header = view.findViewById(R.id.iv_user_header);
        refresh_layout = view.findViewById(R.id.refresh_layout_user);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUserInfo();
            }
        });
        view.findViewById(R.id.iv_right).setOnClickListener(this);
        view.findViewById(R.id.iv_user_header).setOnClickListener(this);
        view.findViewById(R.id.ll_address_manager).setOnClickListener(this);
        view.findViewById(R.id.ll_user_order).setOnClickListener(this);
        view.findViewById(R.id.ll_user_collect).setOnClickListener(this);
        view.findViewById(R.id.ll_user_history).setOnClickListener(this);
        ll_admin = view.findViewById(R.id.ll_admin);
        ll_admin.setOnClickListener(this);
        ll_phone_bind = view.findViewById(R.id.ll_phone_bind);
        ll_phone_bind.setOnClickListener(this);
    }

    private void refreshUserInfo() {
        Log.e("refreshUserInfo","====refreshUserInfo==");
        if (PreferenceUtil.isLogin()) {
            User user = new User();
            user.user_id = PreferenceUtil.getUserInfo().user_id;
            Log.e("refreshUserInfo","======");
            refresh_layout.setRefreshing(false);
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
                List<User> result = userDao.query(user);
                if (result.size() == 1) {
                    PreferenceUtil.saveUser(result.get(0));
                    refreshLoginUser();
                }
            }
        } else {
            Log.e("refreshUserInfo","+++");
            refresh_layout.setRefreshing(false);
        }
    }

    private void refreshLoginUser() {
        tv_login_register.setVisibility(View.INVISIBLE);
        tv_top_title.setText(TextUtils.isEmpty(PreferenceUtil.getString("bind_phone")) ? PreferenceUtil.getString("user_name") : PreferenceUtil.getString("bind_phone"));
        if (!TextUtils.isEmpty(PreferenceUtil.getString("bind_phone"))) {
            ll_phone_bind.setVisibility(View.GONE);
        } else {
            ll_phone_bind.setVisibility(View.VISIBLE);
        }
        if (PreferenceUtil.getUserInfo().user_name.equals("13520862956")) {
            ll_admin.setVisibility(View.VISIBLE);
        } else {
            ll_admin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_right:
                intent = new Intent(v.getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_user_header:
                if (PreferenceUtil.isLogin()) {
                    Bundle bundle = new Bundle();
                    intent = new Intent(getActivity(), RegisterActivity.class);
                    bundle.putInt("editor", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_address_manager:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(v.getContext(), AddressListActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_user_order:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(v.getContext(), OrderListActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_user_collect:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(getContext(), GoodsCollectActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_user_history:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(getContext(), GoodsHistoryActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_phone_bind:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(getContext(), UserPhoneBindActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_admin:
                if (PreferenceUtil.isLogin()) {
                    intent = new Intent(getContext(), ManagerGoodsActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
