package com.nightfair.mobille.util;

import android.app.Fragment;
import android.content.Intent;

public class FragmentUtils {
	/**
	 * @Title: startActivity
	 * @Description: TODO(在一个fragment启动activity)
	 * @param fragment
	 * @param activity
	 *
	 */
	public static void startActivity(Fragment fragment, Class<?> activity) {
		Intent intent = new Intent(fragment.getActivity().getApplicationContext(), activity);
		fragment.startActivity(intent);
	}

}
