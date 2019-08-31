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

import androidx.core.content.ContentResolverCompat;
import androidx.core.content.ContextCompat;

import com.example.newstoday.R;

import java.util.ArrayList;
import java.util.List;

public class DragGridLayout extends GridLayout {
    private boolean isRemain;
    private List<String> items;
    private int margin = 30;

    private boolean isCanDrag;
    private View dragView;
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

    public void addGridItem(String content) {
        TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
//        tv.setBackgroundResource(R.drawable.textview_border);
        tv.setPadding(0, 20, 0, 20);
        tv.setBackgroundColor(ContextCompat.getColor(tv.getContext(), R.color.catArrangeTextBgColor));
        tv.setGravity(Gravity.CENTER);
        if(!isRemain){
            tv.setTextColor(0xFF9c9393);
            tv.setBackgroundResource(R.drawable.textview_unselect_border);
        }
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 4 - margin * 2;
        params.setMargins(margin, margin, margin, margin);
        tv.setLayoutParams(params);

        if(!isRemain)
            tv.setText("+"+content);
        else
            tv.setText(content.replace("+", ""));

        addView(tv);


        if (isCanDrag) {
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dragView = view;
                    view.startDrag(null, new View.DragShadowBuilder(view), null, 0);

                    return true;
                }
            });
        }
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
                    int index = indexOfChild(dragView);
                    items.remove(((TextView)dragView).getText().toString());
                    items.add(index, ((TextView)dragView).getText().toString());
                    break;
            }

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

    public void setIsRemain(boolean isRemain){
        this.isRemain = isRemain;
    }
}