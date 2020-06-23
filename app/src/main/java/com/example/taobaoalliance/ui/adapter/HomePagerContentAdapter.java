package com.example.taobaoalliance.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.utils.UrlUtils;
import com.vondear.rxtool.RxTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    List<HomePagerContent.DataBean> objects = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = objects.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        objects.clear();
        objects.addAll(contents);
        notifyDataSetChanged();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        ImageView ivCover;
        @BindView(R.id.goods_title)
        TextView tvTitle;
        @BindView(R.id.goods_off_prise)
        TextView tvOffPrise;
        @BindView(R.id.goods_after_off_prise)
        TextView tvFinalPrise;
        @BindView(R.id.goods_original_prise)
        TextView tvOriginalPrise;
        @BindView(R.id.goods_sell_count)
        TextView tvSellCount;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();
            Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(ivCover);
            tvTitle.setText(dataBean.getTitle());
            String finalPrice = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();
            float resultPrise = Float.parseFloat(finalPrice) - couponAmount;
            tvFinalPrise.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", resultPrise));
            tvOffPrise.setText(String.format(context.getString(R.string.text_goods_off_prise), couponAmount));
            tvOriginalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvOriginalPrise.setText(String.format(context.getString(R.string.text_goods_original_prise), finalPrice));
            tvSellCount.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }
}