package com.nightfair.mobille.fragment;

import com.nightfair.mobille.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTab_Chat extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.main_tab_chat, container,
				false);
		return newsLayout;
	}

}
