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

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.model.domain.IBaseInfo;
import com.example.taobaoalliance.model.domain.ILinearItemInfo;
import com.example.taobaoalliance.utils.CornerTransform;
import com.example.taobaoalliance.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    List<ILinearItemInfo> objects = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ILinearItemInfo dataBean = objects.get(position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(v -> {
            if (mOnListItemClickListener != null) {
                mOnListItemClickListener.onItemClick(dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        objects.clear();
        objects.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加之前拿到原来的size
        int oldSize = objects.size();
        objects.addAll(contents);
        //更新UI
        notifyItemRangeChanged(oldSize, contents.size());
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

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            //ViewGroup.LayoutParams layoutParams = ivCover.getLayoutParams();
            //int width = layoutParams.width;
            //int height = layoutParams.height;
            //int coverSize = Math.max(width, height) / 2;
            String coverPath = UrlUtils.getTicketUrl(dataBean.getCover());
            //LogUtils.d(this, "coverPath============> " + coverPath);
            CornerTransform cornerTransform = new CornerTransform(context, ConvertUtils.dp2px(6))
                    .setExceptCorner(false, false, false, false);
            Glide.with(context).load(coverPath).error(R.mipmap.no_image).placeholder(R.mipmap.no_image)
                    .transform(cornerTransform).into(ivCover);
            tvTitle.setText(dataBean.getTitle());
            String finalPrice = dataBean.getFinalPrise();
            long couponAmount = dataBean.getCouponAmount();
            float resultPrise = Float.parseFloat(finalPrice) - couponAmount;
            tvFinalPrise.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%.2f", resultPrise));
            tvOffPrise.setText(String.format(context.getString(R.string.text_goods_off_prise), couponAmount));
            tvOriginalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvOriginalPrise.setText(String.format(context.getString(R.string.text_goods_original_prise), finalPrice));
            tvSellCount.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        mOnListItemClickListener = onListItemClickListener;
    }

    private OnListItemClickListener mOnListItemClickListener = null;

    public interface OnListItemClickListener {
        void onItemClick(IBaseInfo item);
    }
}
