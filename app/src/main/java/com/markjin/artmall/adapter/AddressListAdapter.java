package com.markjin.artmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.Address;

import java.util.List;

import static com.markjin.artmall.ui.AddressListActivity.MSG_DEFAULT;
import static com.markjin.artmall.ui.AddressListActivity.MSG_DELETE;
import static com.markjin.artmall.ui.AddressListActivity.MSG_EDITOR;
import static com.markjin.artmall.ui.AddressListActivity.MSG_SELECT;

/**
 * 地址列表
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.Holder> {
    private List<Address> data;
    private Context context;
    private Handler handler;
    private boolean is_order = false;

    public AddressListAdapter(List<Address> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    public void setIsorder(boolean is_order) {
        this.is_order = is_order;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Address address = data.get(position);
        holder.tv_name_phone.setText(address.address_name + " " + address.address_phone);
        holder.tv_address_detail.setText(address.province + address.country + address.district + address.detail_address);
        if (is_order) {
            holder.tv_delete.setVisibility(View.GONE);
        } else {
            holder.tv_delete.setVisibility(View.VISIBLE);
        }
        holder.tv_editor.setTag(address);
        holder.tv_delete.setTag(address);
        holder.ll_default.setTag(address);
        holder.tv_delete.setTag(R.id.tv_delete, position);
        holder.tv_editor.setTag(R.id.tv_editor, position);
        holder.ll_default.setTag(R.id.ll_default, position);
        if (address.is_default.equals("1")) {
            holder.iv_default.setImageResource(R.mipmap.ic_default_select);
            holder.tv_address_default.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.iv_default.setImageResource(R.mipmap.ic_default_default);
            holder.tv_address_default.setTextColor(ContextCompat.getColor(context, R.color.color_66));
            holder.ll_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_DEFAULT;
                    msg.arg1 = (int) v.getTag(R.id.ll_default);
                    msg.obj = v.getTag();
                    handler.sendMessage(msg);
                }
            });
        }
        holder.itemView.setTag(R.id.tv_editor, position);
        holder.itemView.setTag(R.layout.item_address, address);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_order) {
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_SELECT;
                    msg.obj = view.getTag(R.layout.item_address);
                    handler.sendMessage(msg);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = (int) view.getTag(R.id.tv_editor);
                    msg.what = MSG_EDITOR;
                    msg.obj = view.getTag(R.layout.item_address);
                    handler.sendMessage(msg);
                }

            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_name_phone, tv_address_detail, tv_address_default, tv_editor, tv_delete;
        private ImageView iv_default;
        private LinearLayout ll_default;

        public Holder(View itemView) {
            super(itemView);
            tv_name_phone = itemView.findViewById(R.id.tv_name_phone);
            tv_address_detail = itemView.findViewById(R.id.tv_address_detail);
            tv_address_default = itemView.findViewById(R.id.tv_address_default);
            tv_editor = itemView.findViewById(R.id.tv_editor);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            iv_default = itemView.findViewById(R.id.iv_default);
            ll_default = itemView.findViewById(R.id.ll_default);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_DELETE;
                    msg.arg1 = (int) v.getTag(R.id.tv_delete);
                    msg.obj = v.getTag();
                    handler.sendMessage(msg);
                }
            });
            tv_editor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = (int) v.getTag(R.id.tv_editor);
                    msg.what = MSG_EDITOR;
                    msg.obj = v.getTag();
                    handler.sendMessage(msg);
                }
            });
        }
    }
}
