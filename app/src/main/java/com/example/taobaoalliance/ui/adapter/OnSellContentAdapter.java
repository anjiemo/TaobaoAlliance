package com.example.taobaoalliance.ui.adapter;

import android.annotation.SuppressLint;
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
import com.example.taobaoalliance.model.domain.OnSellContent;
import com.example.taobaoalliance.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //绑定数据
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean, mContext);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mapData = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.clear();
        mData.addAll(mapData);
        notifyDataSetChanged();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_on_sell_cover)
        ImageView ivCover;
        @BindView(R.id.tv_on_sell_content_title)
        TextView tvTitle;
        @BindView(R.id.tv_on_sell_original_prise)
        TextView tvOriginalPrise;
        @BindView(R.id.tv_on_sell_off_prise)
        TextView tvOffPrise;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data, Context context) {
            String pictUrl = data.getPict_url();
            String targetUrl = UrlUtils.getTicketUrl(pictUrl);
            Glide.with(context).load(targetUrl).error(R.mipmap.no_image).placeholder(R.mipmap.no_image).into(ivCover);
            tvTitle.setText(data.getTitle());
            String originalPrise = data.getZk_final_price();
            tvOriginalPrise.setText(String.format("¥%1s ", originalPrise));
            tvOriginalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmount = data.getCoupon_amount();
            float originPriseFloat = Float.parseFloat(originalPrise);
            float finalPrise = originPriseFloat - couponAmount;
            tvOffPrise.setText(String.format(Locale.SIMPLIFIED_CHINESE, "券后价：%.2f", finalPrise));
        }
    }
}
