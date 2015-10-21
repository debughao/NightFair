package com.nightfair.mobille.fragment;

import com.nightfair.mobille.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTab_Nearby extends Fragment
{

	private View NearbyView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (NearbyView == null) {
			NearbyView = inflater.inflate(R.layout.main_tab_nearby, container, false);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) NearbyView.getParent();
		if (parent != null) {
			parent.removeView(NearbyView);
		}
		return NearbyView;
		

	}

}
