package com.nightfair.mobille.activity;

import java.io.File;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.KeyBoardUtils;
import com.nightfair.mobille.view.MyEditText;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: Personal_detail_Activity
 * @Description: 个人资料编辑
 * @author debughao
 * @date 2015年10月10日21:43:50
 */
@SuppressLint("ResourceAsColor")
public class Personal_detail_Activity extends BaseActivity implements OnClickListener {
	private Personal_detail_Activity mContent;
	private TextView tv_cancal;
	@ViewInject(R.id.personal_detail_face)
	private RelativeLayout Rl_face;
	private MyEditText et_nickname;
	private RelativeLayout rl_nicakname;
	private TextView tv_complete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		mContent = this;
		ViewUtils.inject(this);
		ActivityUtils.setActionBarByColor(mContent, R.layout.title_bar_personal_detail, R.color.title_color);
		inintView();
	}

	private void inintView() {
		tv_cancal = (TextView) findViewById(R.id.tv_ps_detail_cancel);
		et_nickname = (MyEditText) findViewById(R.id.et_ps_detail_nickname);
		rl_nicakname = (RelativeLayout) findViewById(R.id.personal_detail_nicakname);
		tv_complete = (TextView) findViewById(R.id.tv_ps_detail_complete);
		mySetOnClickListener(tv_cancal, Rl_face, rl_nicakname, tv_complete);

		setTextChangedListener(et_nickname);
	}

	private void setTextChangedListener(TextView... v) {
		for (TextView view : v) {
			view.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					tv_complete.setTextColor(getResources().getColor(R.color.white));
					tv_complete.setEnabled(true);

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {

				}

			});
		}

	}

	private void mySetOnClickListener(View... v) {
		for (View view : v) {
			view.setOnClickListener(this);
		}

	}

	@Override
	@OnClick({ R.id.personal_detail_face })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ps_detail_cancel:
			finish();
			break;
		case R.id.tv_ps_detail_complete:
			finish();
			break;
		case R.id.personal_detail_face:
			ShowPickDialog();
			break;
		case R.id.personal_detail_nicakname:
			KeyBoardUtils.openKeybord(et_nickname, mContent);
			break;
		default:
			break;
		}

	}

	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this).setTitle("设置头像...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent intent = new Intent(Intent.ACTION_PICK, null);

						/**
						 * 下面这句话，与其它方式写是一样的效果，如果：
						 * intent.setData(MediaStore.Images.Media.
						 * EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");设置数据类型
						 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如：
						 * "image/jpeg 、 image/png等的类型"
						 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
						 */
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, 1);

					}
				}).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						/**
						 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
						 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
						 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
						 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
						 */
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	/**
	 * 处理选择照片事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		// 直接从相册获取时
		case 1:
			System.out.println("相册"+data);
			/* photoZoom(); */
			System.out.println(data.getData());
			break;
		// 调用相机拍照时
		case 2:
			System.out.println("相机"+data);
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void photoZoom() {

	}

	@Override
	public void finish() {
		flag = 2;
		super.finish();
	}

}