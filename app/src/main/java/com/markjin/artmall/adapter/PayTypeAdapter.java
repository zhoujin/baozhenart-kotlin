package com.markjin.artmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.PayType;

import java.util.List;

/**
 *
 */

public class PayTypeAdapter extends BaseAdapter {
    private List<PayType> data;
    private int index = 0;

    public PayTypeAdapter(List<PayType> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public PayType getSelect() {
        return data.get(index);
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_type, viewGroup, false);
        ImageView iv_pay_icon = view.findViewById(R.id.iv_pay_icon);
        TextView tv_pay_name = view.findViewById(R.id.tv_pay_name);
        ImageView iv_select = view.findViewById(R.id.iv_select);

        if (data.get(i).pay_code.equalsIgnoreCase("wxpay")) {
            iv_pay_icon.setImageResource(R.mipmap.ic_pay_wx);
        } else if (data.get(i).pay_code.equalsIgnoreCase("alipay")) {
            iv_pay_icon.setImageResource(R.mipmap.ic_pay_alipay);
        } else if (data.get(i).pay_code.equalsIgnoreCase("bank")) {
            iv_pay_icon.setImageResource(R.mipmap.ic_pay_bank);
        }
        tv_pay_name.setText(data.get(i).pay_name);
        if (index == i) {
            iv_select.setImageResource(android.R.drawable.radiobutton_on_background);
        } else {
            iv_select.setImageResource(android.R.drawable.radiobutton_off_background);
        }
        view.setTag(i);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = (int) view.getTag();
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
