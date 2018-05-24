package com.markjin.artmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.R;
import com.markjin.artmall.adapter.AddressListAdapter;
import com.markjin.artmall.db.bean.Address;
import com.markjin.artmall.db.dao.AddressDao;
import com.markjin.artmall.db.dao.BaseDaoFactory;
import com.markjin.artmall.utils.PreferenceUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * bundle->is_order
 */

public class AddressListActivity extends BaseActivity implements View.OnClickListener {
    public static final int MSG_DELETE = 1000;
    public static final int MSG_EDITOR = 1001;
    public static final int MSG_DEFAULT = 1002;
    public static final int MSG_SELECT = 1003;
    private SwipeRefreshLayout refresh_layout;
    private List<Address> data = new ArrayList<>();
    private RecyclerView rv_address;
    private AddressListAdapter adapter;
    private Button bt_address_add;
    private View empty_layout;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DEFAULT:
                    if (msg.obj != null) {
                        Address update = (Address) msg.obj;
                        AddressDao addressDefault = BaseDaoFactory.getInstance().getDataHelper(AddressDao.class, Address.class);
                        //全部设置为0
                        Address all = new Address();
                        all.is_default = "0";
                        Address setDefault = new Address();
                        setDefault.user_id = PreferenceUtil.getUserInfo().user_id;
                        addressDefault.update(all, setDefault);
                        //单独设置为1
                        update.is_default = "1";
                        Address where = new Address();
                        where.address_id = update.address_id;
                        where.user_id = PreferenceUtil.getUserInfo().user_id;
                        if (addressDefault.update(update, where) == 1) {
                            data.clear();
                            adapter.notifyDataSetChanged();
                            initData(data.size());
                        }
                    }
                    break;
                case MSG_DELETE:
                    AddressDao addressDelete = BaseDaoFactory.getInstance().getDataHelper(AddressDao.class, Address.class);
                    Address delete = new Address();
                    delete.user_id = PreferenceUtil.getUserInfo().user_id;
                    delete.address_id = ((Address) msg.obj).address_id;
                    addressDelete.delete(delete);
                    data.remove(msg.arg1);
                    adapter.notifyItemRemoved(msg.arg1);
                    break;
                case MSG_EDITOR:
                    if (msg.obj != null) {
                        Address address = (Address) msg.obj;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("editor", address);
                        intentActivity(AddressListActivity.this, AddressAddActivity.class, bundle);
                    }
                    break;
                case MSG_SELECT:
                    if (msg.obj != null) {
                        Address address = (Address) msg.obj;
                        Intent intent = new Intent();
                        intent.putExtra("address", address);
                        setResult(Activity.RESULT_OK, intent);
                        finishActivity(AddressListActivity.this);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        adapter.notifyDataSetChanged();
        initData(data.size());
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.title_address_manager);
        findViewById(R.id.iv_left).setOnClickListener(this);
        rv_address = findViewById(R.id.rv_address);
        bt_address_add = findViewById(R.id.bt_address_add);
        bt_address_add.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_address.setLayoutManager(layoutManager);
        adapter = new AddressListAdapter(data, handler);
        rv_address.setAdapter(adapter);
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_layout.setRefreshing(false);
                data.clear();
                adapter.notifyDataSetChanged();
                initData(data.size());
            }
        });
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("is_order")) {
            adapter.setIsorder(true);
            bt_address_add.setVisibility(View.GONE);
        } else {
            adapter.setIsorder(false);
            bt_address_add.setVisibility(View.VISIBLE);
        }
        empty_layout = findViewById(R.id.empty_layout);
        initData(data.size());
    }

    private void initData(int offset) {
        AddressDao addressDao = BaseDaoFactory.getInstance().getDataHelper(AddressDao.class, Address.class);
        Address where = new Address();
        where.user_id = PreferenceUtil.getUserInfo().user_id;
        List<Address> list = addressDao.query(where, " add_time desc ", String.valueOf(offset), "10");
        if (list != null && list.size() > 0) {
            data.addAll(list);
            adapter.notifyDataSetChanged();
        }
        if (data.size() == 0) {
            empty_layout.setVisibility(View.VISIBLE);
            refresh_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.GONE);
            refresh_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            case R.id.bt_address_add:
                intentActivity(AddressListActivity.this, AddressAddActivity.class);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
