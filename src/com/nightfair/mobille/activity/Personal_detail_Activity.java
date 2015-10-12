package com.nightfair.mobille.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.config.ApiUrl;
import com.nightfair.mobille.config.FilePath;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.Base64Coder;
import com.nightfair.mobille.util.KeyBoardUtils;
import com.nightfair.mobille.util.MD5Util;
import com.nightfair.mobille.util.NetWorkUtil;
import com.nightfair.mobille.view.MyEditText;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, 1);

					}
				}).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
								new File(FilePath.getCameraStore(mContent), MD5Util.MD5("headface") + ".png")));
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	/**
	 * 处理选择照片事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println(requestCode + "-->" + resultCode + "-->" + data);
		switch (requestCode) {
		// 直接从相册获取时
		case 1:

			if (data != null) {
				System.out.println(data.getData());
				photoZoom(data.getData().toString());
			} else {
				System.out.println("无选择");
			}

			break;
		// 调用相机拍照时
		case 2:
			if (resultCode == -1) {
				photoZoom(FilePath.getCameraStore(mContent) + File.separator + MD5Util.MD5("headface") + ".png");
			} else {
				System.out.println("并无拍照");
			}
			break;
		// 裁剪后的图片上传
		case 3:
			ActivityUtils.showShortToast(mContent, "裁剪成功");

			byte[] bytes=data.getByteArrayExtra("data");
			final String picStr = new String(Base64Coder.encodeLines(bytes));
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("picStr", picStr));
					params.add(new BasicNameValuePair("picName", "dengzi"));
					final String result = NetWorkUtil.httpPost(ApiUrl.UserUploadHd,params);
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(mContent, result, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}).start();
			
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void photoZoom(String path) {
		/* Bitmap bitmap = BitmapFactory.decodeFile(path); */
		Intent intent = new Intent("com.nightfair.camera.action.CROP");
		intent.putExtra("path", path);
		startActivityForResult(intent, 3);
	}

	@Override
	public void finish() {
		flag = 2;
		super.finish();
	}

}