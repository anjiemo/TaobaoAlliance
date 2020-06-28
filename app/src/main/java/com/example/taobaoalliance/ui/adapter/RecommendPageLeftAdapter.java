package com.example.taobaoalliance.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendPageLeftAdapter extends RecyclerView.Adapter<RecommendPageLeftAdapter.InnerHolder> {

    private List<RecommendPageCategory.DataBean> mData = new ArrayList<>();
    private int mCurrentRecommendPosition = 0;
    private Context mContext;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView tvLeftCategory = holder.tvLeftCategory;
        if (mCurrentRecommendPosition == position) {
            tvLeftCategory.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorEEEEEE));
        } else {
            tvLeftCategory.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
        RecommendPageCategory.DataBean dataBean = mData.get(position);
        tvLeftCategory.setText(dataBean.getFavorites_title());
        tvLeftCategory.setOnClickListener(v -> {
            if (mOnLeftItemClickListener != null && mCurrentRecommendPosition != position) {
                //修改当前选中的位置
                mCurrentRecommendPosition = position;
                mOnLeftItemClickListener.onLeftItemClick(dataBean);
                notifyDataSetChanged();
            }
        });
    }


    public void setOnLeftItemClickListener(OnLeftItemClickListener onLeftItemClickListener) {
        mOnLeftItemClickListener = onLeftItemClickListener;
    }

    private OnLeftItemClickListener mOnLeftItemClickListener = null;

    public interface OnLeftItemClickListener {
        void onLeftItemClick(RecommendPageCategory.DataBean item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(RecommendPageCategory categories) {
        List<RecommendPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0 && mOnLeftItemClickListener != null) {
            mOnLeftItemClickListener.onLeftItemClick(mData.get(mCurrentRecommendPosition));
        }
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_category)
        TextView tvLeftCategory;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
