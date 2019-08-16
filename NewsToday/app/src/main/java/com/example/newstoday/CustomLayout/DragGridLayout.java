package com.example.newstoday.CustomLayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.newstoday.R;

import java.util.ArrayList;
import java.util.List;

public class DragGridLayout extends GridLayout {
    private List<String> items;
    private int margin = 15;

    // 是否可以拖拽
    private boolean isCanDrag;
    // 记录被拖拽的View
    private View dragView;
    // 存放所有条目的矩形区域
    private List<Rect> rects;

    public void setItems(List<String> items) {
        this.items = items;
        for (int i = 0; i < items.size(); i++) {
            addGridItem(items.get(i));
        }
    }

    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
        if (isCanDrag) {
            setOnDragListener(onDragListener);
        }
    }

    public DragGridLayout(Context context) {
        this(context, null);
    }

    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 给GridLayout添加item
     */
    public void addGridItem(String content) {
        TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        tv.setBackgroundResource(R.drawable.textview_border);
        tv.setGravity(Gravity.CENTER);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 4 - margin * 2;
        params.setMargins(margin, margin, margin, margin);
        tv.setLayoutParams(params);

        tv.setText(content);
        addView(tv);


        if (isCanDrag) {
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dragView = view;
                    // 产生浮动的阴影效果
                    // 只有第二个参数有用，其他传空即可
                    view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                    // true 响应长按事件
                    return true;
                }
            });
        }
        // 设置条目的点击事件
        tv.setOnClickListener(onClickListener);
    }

    private OnDragListener onDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                // 按下
                case DragEvent.ACTION_DRAG_STARTED:
                    dragView.setEnabled(false);
                    initRects();
                    break;
                // 移动
                case DragEvent.ACTION_DRAG_LOCATION:
                    int exchangeIndex = getExchangeIndex(dragEvent);
                    if (exchangeIndex > -1 && dragView != getChildAt(exchangeIndex)) {
                        removeView(dragView);
                        addView(dragView, exchangeIndex);
                    }
                    break;
                // 弹起
                case DragEvent.ACTION_DRAG_ENDED:
                    dragView.setEnabled(true);
                    break;
            }

            // true 响应拖拽事件
            return true;
        }
    };

    private void initRects() {
        rects = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            // 创建矩形
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            rects.add(rect);
        }
    }

    private int getExchangeIndex(DragEvent dragEvent) {
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if (rect.contains((int) dragEvent.getX(), (int) dragEvent.getY())) {
                return i;
            }
        }
        return -1;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onDragItemClickListener != null) {
                onDragItemClickListener.onDragItemClick((TextView) view);
            }
        }
    };

    //接口
    public interface OnDragItemClickListener {
        void onDragItemClick(TextView tv);
    }

    private OnDragItemClickListener onDragItemClickListener;

    public void setOnDragItemClickListener(OnDragItemClickListener onDragItemClickListener) {
        this.onDragItemClickListener = onDragItemClickListener;
    }

    public List<String> getItems() {
        return items;
    }
}