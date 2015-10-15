package com.nightfair.mobille.view;

import com.nightfair.mobille.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HintEditText extends RelativeLayout implements TextWatcher {

	private EditText mEditText;
	private TextView mTextView;
	private int maxLength = 0;
	private RelativeLayout mRelativeLayout;

	public HintEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public HintEditText(Context context) {
		super(context);

	}

	public HintEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.HintEditText);
		maxLength = mTypedArray.getInt(R.styleable.HintEditText_maxLength, 0);
		mRelativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_edittext, this, true);
		mEditText = (EditText) mRelativeLayout.findViewById(R.id.edit);
		mTextView = (TextView) mRelativeLayout.findViewById(R.id.text);
		mTextView.setHint("还可输入" + maxLength + "字");
		// 限定最多可输入多少字符
		mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
		mEditText.addTextChangedListener(this);

	}

	public void initUI(Context context) {

		RelativeLayout mRelativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_edittext,
				this, true);

		mEditText = (EditText) mRelativeLayout.findViewById(R.id.edit);

		mTextView = (TextView) mRelativeLayout.findViewById(R.id.text);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		mTextView.setHint("还可输入" + (maxLength - s.toString().length()) + "字");

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
