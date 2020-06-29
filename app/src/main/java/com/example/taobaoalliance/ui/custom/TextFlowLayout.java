package com.example.taobaoalliance.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CustomViewStyleable")
public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private List<String> mTextList = new ArrayList<>();
    private int mSelfWidth;
    private int mItemHeight;

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿到相关属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = typedArray.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        typedArray.recycle();
        LogUtils.d(this, "mItemHorizontalSpace ======> " + mItemHorizontalSpace);
        LogUtils.d(this, "mItemVerticalSpace ======> " + mItemVerticalSpace);
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public void setTextList(List<String> textList) {
        mTextList = textList;
        //遍历你饿哦让
        for (String text : mTextList) {
            //添加子View
            //View item = LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, true);
            //等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            addView(item);
        }
    }

    //这个是描述所有的行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这个是描述单行
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LogUtils.d(this, "mSelfWidth ====> " + mSelfWidth);
        //测量
        //LogUtils.d(this,"onMeasure ====> " + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        if (childCount == 0) return;
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要测量
                continue;
            }
            //测量前
            LogUtils.d(this, "before height =====> " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //测量后
            LogUtils.d(this, "after height =====> " + itemView.getMeasuredHeight());
            if (line == null) {
                //说明当前行为空，可以添加进来
                line = createNewLine(itemView);
            } else {
                //判断是否可以继续再添加
                if (canBeAdd(itemView, line)) {
                    //可以添加，添加进去
                    line.add(itemView);
                } else {
                    //新创建一行
                    line = createNewLine(itemView);
                }
            }
//            if (line != null && canBeAdd(itemView, line)) {
//                //可以添加，添加进去
//                line.add(itemView);
//            } else {
//                //新创建一行
//                createNewLine(itemView);
//            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    /**
     * 新创建一行
     *
     * @param itemView
     * @return
     */
    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加新数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有已经添加的子View宽度+(line.size() + 1) * mItemHorizontalSpace + itemView.getMeasuredWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //叠加所有已经添加控件的宽度
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        LogUtils.d(this, "totalWidth =====> " + totalWidth);
        LogUtils.d(this, "mSelfWidth ====> " + mSelfWidth);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        //LogUtils.d(this,"onMeasure ====> " + getChildCount());
        int topOffSet = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffSet = (int) mItemHorizontalSpace;
            for (View view : views) {
                //每一行里的每一个Item
                view.layout(leftOffSet, topOffSet, leftOffSet + view.getMeasuredWidth(), topOffSet + view.getMeasuredHeight());
                //
                leftOffSet += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffSet += mItemHeight + mItemHorizontalSpace;
        }
    }
}
