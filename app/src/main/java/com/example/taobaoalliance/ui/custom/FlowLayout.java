package com.example.taobaoalliance.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.example.taobaoalliance.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private static final String TAG = FlowLayout.class.getSimpleName();
    public static final int DEFAULT_LINE = -1;
    //后面需要转单位，目前是px不适配
    public static final float DEFAULT_HORIZONTAL_MARGIN = SizeUtils.dp2px(5f);
    public static final float DEFAULT_VERTICAL_MARGIN = SizeUtils.dp2px(5f);
    public static final int DEFAULT_BORDER_RADIUS = SizeUtils.dp2px(5f);
    public static final int DEFAULT_TEXT_MAX_LENGTH = -1;
    private int mMaxLine;
    private int mHorizontalMargin;
    private int mVerticalMargin;
    private int mTextMaxLength;
    private int mTextColor;
    private int mBorderColor;
    private float mBorderRadius;
    private List<String> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        //maxLine
        mMaxLine = typedArray.getInt(R.styleable.FlowLayout_maxLine, DEFAULT_LINE);
        if (mMaxLine < 1 && mMaxLine != DEFAULT_LINE)
            throw new IllegalArgumentException("mMaxLine must greater than or equal to 1 or equal DEFAULT_LINE");
        //itemHorizontalMargin
        mHorizontalMargin = (int) typedArray.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_HORIZONTAL_MARGIN);
        //itemVerticalMargin
        mVerticalMargin = (int) typedArray.getDimension(R.styleable.FlowLayout_itemVerticalMargin, DEFAULT_VERTICAL_MARGIN);
        //textMaxLength
        mTextMaxLength = typedArray.getInt(R.styleable.FlowLayout_textMaxLength, DEFAULT_TEXT_MAX_LENGTH);
        if (mTextMaxLength < 0 && mTextMaxLength != DEFAULT_TEXT_MAX_LENGTH)
            throw new IllegalArgumentException("mTextMaxLength must greater than or equal to 0 or equal DEFAULT_TEXT_MAX_LENGTH");
        //textColor
        mTextColor = typedArray.getColor(R.styleable.FlowLayout_textColor, ContextCompat.getColor(getContext(), R.color.text_grey));
        //borderColor
        mBorderColor = typedArray.getColor(R.styleable.FlowLayout_borderColor, ContextCompat.getColor(getContext(), R.color.text_grey));
        //borderRadius
        mBorderRadius = typedArray.getDimension(R.styleable.FlowLayout_borderRadius, DEFAULT_BORDER_RADIUS);
        Log.d(TAG, "FlowLayout: mMaxLines=========> " + mMaxLine);
        Log.d(TAG, "FlowLayout: mHorizontalMargin=========> " + mHorizontalMargin);
        Log.d(TAG, "FlowLayout: mVerticalMargin=========> " + mVerticalMargin);
        Log.d(TAG, "FlowLayout: mTextMaxLength=========> " + mTextMaxLength);
        Log.d(TAG, "FlowLayout: mTextColor=========> " + mTextColor);
        Log.d(TAG, "FlowLayout: mBorderColor=========> " + mBorderColor);
        Log.d(TAG, "FlowLayout: mBorderRadius=========> " + mBorderRadius);
        typedArray.recycle();
    }

    public FlowLayout setTextList(List<String> data) {
        mData.clear();
        mData.addAll(data);
        //根据数据创建子View，并且添加进来
        setUpChildren();
        return this;
    }

    private void setUpChildren() {
        //清空原来的内容
        removeAllViews();
        //添加子View进来
        for (String datum : mData) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text, this, false);
            //设置TextView的相关属性，边距，颜色，border之类 ...
            if (mTextMaxLength != DEFAULT_TEXT_MAX_LENGTH)
                //设置TextView的最大内容长度
                textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextMaxLength)});
            textView.setText(datum);
            textView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, datum);
                }
            });
            addView(textView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public int getContentSize() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String text);
    }

    private final List<List<View>> mLines = new ArrayList<>();

    /**
     * 这两个值来自于父控件，包含值和模式
     * int类型 ====> 4个字节 ====> 4*8 bit ====> 32位
     * 0 ====> 0
     * 1 ====> 1
     * 2 ====> 10
     * 3 ====> 11
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: mode=====> " + mode);
        Log.d(TAG, "onMeasure: size=====> " + parentWidthSize);
        Log.d(TAG, "onMeasure: AT_MOST======> " + MeasureSpec.AT_MOST);
        Log.d(TAG, "onMeasure: EXACTLY======> " + MeasureSpec.EXACTLY);
        Log.d(TAG, "onMeasure: UNSPECIFIED======> " + MeasureSpec.UNSPECIFIED);
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //先清空
        mLines.clear();
        //添加默认行
        List<View> line = new ArrayList<>();
        mLines.add(line);
        int childWidthSpace = MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.AT_MOST);
        int childHeightSpace = MeasureSpec.makeMeasureSpec(parentHeightSize, MeasureSpec.AT_MOST);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //如果该子View不可见，则进行下一个循环，不进行测量
            if (child.getVisibility() != VISIBLE) continue;
            //测量孩子，
            measureChild(child, childWidthSpace, childHeightSpace);
            //判断是否可以添加到当前行
            boolean canBeAdd = checkChildCanBeAdd(line, child, parentWidthSize);
            if (!canBeAdd) {
                //跳出循环，不再添加
                if (mMaxLine != -1 && mLines.size() >= mMaxLine) break;
                line = new ArrayList<>();
                mLines.add(line);
            }
            line.add(child);
        }
        //根据尺寸计算所有行高
        View child = getChildAt(0);
        int childHeight = child.getMeasuredHeight();
        int parentHeightTargetSize = childHeight * mLines.size()
                + mVerticalMargin * (mLines.size() + 1)
                + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(parentWidthSize, parentHeightTargetSize);
    }

    private boolean checkChildCanBeAdd(List<View> line, View child, int parentWidthSize) {
        int measuredWidth = child.getMeasuredWidth();
        int totalWidth = mHorizontalMargin + getPaddingLeft();
        for (View view : line) {
            totalWidth += view.getMeasuredWidth() + mHorizontalMargin;
        }
        totalWidth += measuredWidth + mHorizontalMargin + getPaddingRight();
        //如果超出限制宽度，则不可以再添加
        //否则可以添加
        return totalWidth <= parentWidthSize;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        if (childCount == 0) return;
        View firstChild = getChildAt(0);
        int currentLeft = mHorizontalMargin + getPaddingLeft();
        int currentTop = mVerticalMargin + getPaddingTop();
        int currentRight = mHorizontalMargin + getPaddingRight();
        int currentBottom = firstChild.getMeasuredHeight() + mVerticalMargin + getPaddingBottom();
        for (List<View> line : mLines) {
            for (View view : line) {
                //布局每一行
                int width = view.getMeasuredWidth();
                currentRight += width;
                //判断最右边的边界
                if (currentRight > getMeasuredWidth() - mHorizontalMargin) {
                    currentRight = getMeasuredWidth() - mHorizontalMargin;
                }
                view.layout(currentLeft, currentTop, currentRight, currentBottom);
                currentLeft = currentRight + mHorizontalMargin;
                currentRight += mHorizontalMargin;
            }
            currentLeft = mHorizontalMargin + getPaddingLeft();
            currentRight = mHorizontalMargin + getPaddingRight();
            currentBottom += firstChild.getMeasuredHeight() + mVerticalMargin;
            currentTop += firstChild.getMeasuredHeight() + mVerticalMargin;
        }
        for (int i = 0; i < getChildCount(); i++) {
            TextView childAt = (TextView) getChildAt(i);
            float x = childAt.getX();
            float y = childAt.getY();
            String text = childAt.getText().toString();
            Log.d(TAG, "onLayout: index " + i + " x======> " + x + " y=====> " + y + " text is ===> " + text);
        }
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public FlowLayout setMaxLine(int maxLine) {
        mMaxLine = maxLine;
        return this;
    }

    public int getHorizontalMargin() {
        return mHorizontalMargin;
    }

    public FlowLayout setHorizontalMargin(int horizontalMargin) {
        mHorizontalMargin = SizeUtils.dp2px(horizontalMargin);
        return this;
    }

    public int getVerticalMargin() {
        return mVerticalMargin;
    }

    public FlowLayout setVerticalMargin(int verticalMargin) {
        mVerticalMargin = SizeUtils.dp2px(verticalMargin);
        return this;
    }

    public int getTextMaxLength() {
        return mTextMaxLength;
    }

    public FlowLayout setTextMaxLength(int textMaxLength) {
        mTextMaxLength = textMaxLength;
        return this;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public FlowLayout setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public FlowLayout setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        return this;
    }

    public float getBorderRadius() {
        return mBorderRadius;
    }

    public FlowLayout setBorderRadius(float borderRadius) {
        mBorderRadius = SizeUtils.dp2px(borderRadius);
        return this;
    }
}
