package com.nightfair.mobille.dialog;


import com.nightfair.mobille.R;
import com.nightfair.mobille.util.KeyBoardUtils;
import com.nightfair.mobille.view.HintEditText;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;;

public class AutoGraphDialog extends BaseDialog implements OnClickListener {
	private OnConfirmListener mOnConfirmListener;
	private Context mContext;
	private TextView mConfirmView;
	private HintEditText mUserIdView;
	public AutoGraphDialog(Context context, OnConfirmListener onConfirmListener) {
		super(context, R.style.transparentFrameWindowStyle);
		this.mContext = context;
		this.mOnConfirmListener = onConfirmListener;
		setContentView(R.layout.dialog_autograph);
		mUserIdView = (HintEditText) this.findViewById(R.id.het_autograph);
		mConfirmView = (TextView) this.findViewById(R.id.autograph_ok);
		mConfirmView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/*mOnConfirmListener.onConfirm(String.valueOf(mUserIdView.));
		KeyBoardUtils.closeKeybord(mUserIdView, mContext);*/
		dismiss();

	}

	/**
	 * 显示在底部
	 */
	public void showDialogBottom(float dimAmount) {

		Window window = this.getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height=ViewGroup.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
		this.show();
	}
}
