package com.nightfair.mobille.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 自定义ListView子类，继承ListView
 * 
 * ScrollView中嵌入ListView,让ListView全显示出来 
 */
public class FullyListView extends ListView {

	/*PullToRefreshScrollView parentScrollView;
    public PullToRefreshScrollView getParentScrollView() {
        return parentScrollView;
    }
    public void setParentScrollView(PullToRefreshScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }*/
	
	public FullyListView(Context context) {
		super(context);
	}

	public FullyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FullyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置不滚动 
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setParentScrollAble(false);
                break;
            case MotionEvent.ACTION_MOVE:
            	setParentScrollAble(true);
                break;
            case MotionEvent.ACTION_UP:
            	setParentScrollAble(false);
            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }*/

   /* *//**
     * @param flag
     *//*
    private void setParentScrollAble(boolean flag) {
        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }*/
    
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	getParent().requestDisallowInterceptTouchEvent(true);
    	return super.dispatchTouchEvent(ev);
    }*/

	
}