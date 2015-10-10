package com.nightfair.mobille.activity;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;

import android.os.Bundle;

public class Personal_Collecyion_Activity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal__collection);
	}

	@Override
	public void finish() {
		flag = 2;
		super.finish();
	}
}
