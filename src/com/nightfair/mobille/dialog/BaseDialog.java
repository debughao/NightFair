package com.nightfair.mobille.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * 
* @ClassName: BaseDialog
* @Description: TODO(对话框基础类)
* @author debughao
* @date 2015年10月15日
 */

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 监听确定
	 */
	public abstract interface OnConfirmListener {

		public abstract void onConfirm(String result);
	}

	/**
	 * 监听取消
	 */
	public abstract interface OnCancleListener {

		public abstract void onCancle(String result);
	}
	
	/**
	 * 监听删除
	 */
	public abstract interface OnDeleteListener {

		public abstract void onDelete(String result);
	}

	/**
	 * 监听变化
	 */
	public abstract interface OnChangListener {

		public abstract void onChang(String result);
	}

}
