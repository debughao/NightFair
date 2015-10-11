package com.nightfair.mobille.view;

import com.nightfair.mobille.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewParent;
import android.widget.EditText;


@SuppressLint("ResourceAsColor")
public class MyEditText extends EditText implements OnFocusChangeListener, TextWatcher {

	/**
	 * 控件是否有焦点
	 */
	private boolean isfoucs;
	private int num;
	

	public MyEditText(Context context) {
		this(context, null);
		init();
	}

	public MyEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		
		this(context, attrs, android.R.attr.editTextStyle);
		init();

	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	
		init();
	}

	private void init() {
		
		this.setFocusable(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
		//
		
	}

	/**
	 * 焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.isfoucs = hasFocus;
		if (hasFocus) {
			num = getText().length();
			this.setSelection(num);
		}
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
	
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}


}
