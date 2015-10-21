package com.nightfair.mobille.dialog;

import com.nightfair.mobille.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class FacebookDialog extends BaseDialog implements OnClickListener {

	public FacebookDialog(Context context) {
		super(context, R.style.transparentFrameWindowStyle);
		setContentView(R.layout.dialog_facebook);
	}

	@Override
	public void onClick(View v) {
		
		
	}

}
