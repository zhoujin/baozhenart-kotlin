package com.markjin.artmall.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.markjin.artmall.R;


public class Loadlayout extends Dialog {
	
	public Loadlayout(Context context) {
		super(context);
		this.setContentView(R.layout.common_loading);
	}
	public Loadlayout(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.common_loading);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
	}
	public Loadlayout(Context context, int theme, String hint) {
		super(context, theme);
		View view= LayoutInflater.from(context).inflate(R.layout.common_loading, null);
		((TextView)view.findViewById(R.id.tv_loading_msg)).setText(hint);
		this.setContentView(view);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
	}
	public Loadlayout(Context context, int theme, int resString) {
		super(context, theme);
		View view= LayoutInflater.from(context).inflate(R.layout.common_loading, null);
		((TextView)view.findViewById(R.id.tv_loading_msg)).setText(resString);
		this.setContentView(view);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
	}
	public Loadlayout(Activity context, int theme, boolean isCancel) {
		super(context, theme);
		this.setContentView(R.layout.common_loading);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(isCancel);
	}
}
