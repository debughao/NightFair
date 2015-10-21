package com.nightfair.mobille.dialog;

import com.nightfair.mobille.R;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.TextView;

public class CancalDialog extends BaseDialog implements OnClickListener {
	private Activity activity;

	private TextView mConfirmView;
	private TextView mCancalView;

	public CancalDialog(Context context, Activity activity) {
		super(context, R.style.transparentFrameWindowStyle);
		this.activity = activity;
		setContentView(R.layout.dialog_cancal);
		mConfirmView = (TextView) this.findViewById(R.id.dialog_cancal_ok);
		mCancalView = (TextView) this.findViewById(R.id.dialog_cancal_cancel);	
		mConfirmView.setOnClickListener(this);
		mCancalView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_cancal_ok:
			dismiss();
			break;
		case R.id.dialog_cancal_cancel:
			activity.finish();
			break;
		default:
			break;
		}

	}
	public void showCenter() {
		Window window = this.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		window.setAttributes(lp);
		this.show();
	}
}
