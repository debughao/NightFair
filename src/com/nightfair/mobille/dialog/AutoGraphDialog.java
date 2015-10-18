package com.nightfair.mobille.dialog;

import com.nightfair.mobille.R;
import com.nightfair.mobille.util.KeyBoardUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;;

public class AutoGraphDialog extends BaseDialog implements OnClickListener {
	private Context mContext;
	private TextView mConfirmView;
	private TextView mCancalView;
	private EditText edit;
	private OnConfirmListener mConfirmListener;
	private String mAutoGraph;

	public AutoGraphDialog(Context context,String autoGraph, OnConfirmListener onConfirmListener) {
		super(context, R.style.transparentFrameWindowStyle);
		this.mContext = context;
		this.mConfirmListener = onConfirmListener;
		this.mAutoGraph=autoGraph;
		setContentView(R.layout.dialog_autograph);
		mConfirmView = (TextView) this.findViewById(R.id.autograph_ok);
		mCancalView = (TextView) this.findViewById(R.id.autograph_cancel);
		edit = (EditText) findViewById(R.id.edit);
		edit.setText(mAutoGraph);
		mConfirmView.setOnClickListener(this);
		mCancalView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.autograph_ok:

			mConfirmListener.onConfirm(String.valueOf(edit.getText()));
			KeyBoardUtils.closeKeybord(edit, mContext);
			this.dismiss();
			break;
		case R.id.autograph_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}


}
