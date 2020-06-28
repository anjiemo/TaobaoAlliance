package com.example.taobaoalliance.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendPageContentAdapter extends RecyclerView.Adapter<RecommendPageContentAdapter.InnerHolder> {

    private List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData = mData.get(position);
        holder.setData(itemData);
        View itemView = holder.itemView;
        itemView.setOnClickListener(v -> {
            if (mOnRecommendPageContentItemClickListener != null) {
                mOnRecommendPageContentItemClickListener.onContentItemClick(itemData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(RecommendContent content) {
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> uatm_tbk_item = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            mData.clear();
            mData.addAll(uatm_tbk_item);
            notifyDataSetChanged();
        }
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recommend_cover)
        ImageView ivCover;
        @BindView(R.id.recommend_off_prise)
        TextView tvOffPrise;
        @BindView(R.id.recommend_title)
        TextView tvTitle;
        @BindView(R.id.recommend_buy_btn)
        TextView tvBuyBtn;
        @BindView(R.id.recommend_original_prise)
        TextView tvOriginalPrise;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData) {
            String pictUrl = itemData.getPict_url();
            Glide.with(itemView.getContext()).load(pictUrl).error(R.mipmap.no_image).placeholder(R.mipmap.no_image).into(ivCover);
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                tvOriginalPrise.setText("晚啦，没有优惠券了");
                tvBuyBtn.setVisibility(View.GONE);
            } else {
                tvOriginalPrise.setText(String.format("原价：%1s", itemData.getZk_final_price()));
                tvBuyBtn.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                tvOffPrise.setVisibility(View.GONE);
            } else {
                tvOffPrise.setVisibility(View.VISIBLE);
                tvOffPrise.setText(itemData.getCoupon_info());
            }
            tvTitle.setText(itemData.getTitle());
        }
    }

    public void setOnRecommendPageContentItemClickListener(OnRecommendPageContentItemClickListener onRecommendPageContentItemClickListener) {
        mOnRecommendPageContentItemClickListener = onRecommendPageContentItemClickListener;
    }

    private OnRecommendPageContentItemClickListener mOnRecommendPageContentItemClickListener = null;

    public interface OnRecommendPageContentItemClickListener {
        void onContentItemClick(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item);
    }
}
