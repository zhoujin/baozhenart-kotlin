package com.markjin.artmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markjin.artmall.GlideApp;
import com.markjin.artmall.R;
import com.markjin.artmall.db.bean.HomeData;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.StringUtil;

import java.util.List;

/**
 *
 */

public class HomeArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeData.Data.Artist> data;


    public HomeArtistAdapter(Context context, List<HomeData.Data.Artist> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_artist_lv, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            final HomeData.Data.Artist artist
                    = data.get(position);
            if (artist.goods_list != null) {
                if (artist.goods_list.size() > 0) {
                    GlideApp.with(context).load(artist.goods_list.get(0).goods_image.img_url).skipMemoryCache(false).error(R.mipmap.ic_image_default)
                            .into(((Holder) holder).iv_artist);
                } else {
                    GlideApp.with(context).load("").skipMemoryCache(false).error(R.mipmap.ic_image_default)
                            .into(((Holder) holder).iv_artist);
                }
            } else {
                GlideApp.with(context).load("").skipMemoryCache(false).error(R.mipmap.ic_image_default)
                        .into(((Holder) holder).iv_artist);
            }
            GlideApp.with(context).load(artist.head_imgurl).skipMemoryCache(false).error(R.mipmap.ic_image_default)
                    .into(((Holder) holder).iv_author_head);
            ((Holder) holder).tv_author_name.setText(artist.artist_name);
            ((Holder) holder).tv_author_browse.setText(context.getString(R.string.browse_count, artist.visit_count));
            if (!StringUtil.isEmpty(artist.fans)) {
                ((Holder) holder).tv_author_fans.setText(context.getString(R.string.attention_count, artist.fans));
            } else {
                ((Holder) holder).tv_author_fans.setText(context.getString(R.string.attention_count, "0"));
            }
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private ImageView iv_author_head;
        private TextView tv_author_name;
        private TextView tv_author_browse;
        private TextView tv_author_fans;
        private ImageView iv_artist;

        public Holder(View itemView) {
            super(itemView);
            iv_author_head = itemView.findViewById(R.id.iv_author_head);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            tv_author_browse = itemView.findViewById(R.id.tv_author_browse);
            tv_author_fans = itemView.findViewById(R.id.tv_author_fans);
            iv_artist = itemView.findViewById(R.id.iv_artist);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(context) * 3 / 5, ScreenUtils.getScreenWidth(context) * 21 / 40);
            iv_artist.setLayoutParams(params);
        }
    }
}
