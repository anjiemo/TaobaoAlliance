package com.example.taobaoalliance.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.model.domain.IBaseInfo;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.utils.Constants;
import com.example.taobaoalliance.utils.CornerTransform;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendPageContentAdapter extends RecyclerView.Adapter<RecommendPageContentAdapter.InnerHolder> {

    private List<RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData = mData.get(position);
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
            List<RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> uatm_tbk_item = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
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

        public void setData(RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData) {
            Context context = itemView.getContext();
            String pictUrl = itemData.getPict_url();
            CornerTransform cornerTransform = new CornerTransform(context, ConvertUtils.dp2px(6))
                    .setExceptCorner(false, false, false, false);
            String coverPath = UrlUtils.getCoverPath(pictUrl, 220);
            LogUtils.d(this, String.format("Cover url is ========> %s", coverPath));
            Glide.with(context).load(coverPath).error(R.mipmap.no_image).placeholder(R.mipmap.no_image)
                    .transform(cornerTransform).into(ivCover);
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
        void onContentItemClick(IBaseInfo item);
    }
}
