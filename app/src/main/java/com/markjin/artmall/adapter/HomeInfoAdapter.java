package com.markjin.artmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.HomeData;
import com.markjin.artmall.utils.ImageSpanPlus;
import com.markjin.artmall.utils.ScreenUtils;

import java.util.List;

/**
 *
 */

public class HomeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeData.Data.Subject> data;
    private ImageSpanPlus imageSpan;

    public HomeInfoAdapter(Context context, List<HomeData.Data.Subject> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        imageSpan = new ImageSpanPlus(parent.getContext(), R.mipmap.ic_news_particulars);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_recyler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            final HomeData.Data.Subject topic = data.get(position);
            ((Holder) holder).iv_topic.setBackgroundResource(R.mipmap.ic_image_default);
            GlideApp.with(context).load(topic.image_url).skipMemoryCache(false)
                    .error(R.mipmap.ic_image_default)
                    .into(((Holder) holder).iv_topic);
            StringBuilder sb = new StringBuilder();
            sb.append(topic.title).append("    ");
            SpannableString ss = new SpannableString(sb);
            ss.setSpan(imageSpan, sb.length() - 1, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            ((Holder) holder).tv_topic_title.setText(ss);
            ((Holder) holder).tv_topic_desc.setText(topic.description);
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private ImageView iv_topic;
        private TextView tv_topic_title;
        private TextView tv_topic_desc;

        public Holder(View itemView) {
            super(itemView);
            iv_topic = itemView.findViewById(R.id.iv_topic);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(context) - 160, (ScreenUtils.getScreenWidth(context) - 160) / 2);
            iv_topic.setLayoutParams(params);
            tv_topic_title = itemView.findViewById(R.id.tv_topic_title);
            tv_topic_desc = itemView.findViewById(R.id.tv_topic_desc);
        }
    }
}
