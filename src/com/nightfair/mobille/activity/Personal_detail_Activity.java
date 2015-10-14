package com.nightfair.mobille.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.config.ApiUrl;
import com.nightfair.mobille.config.FilePath;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.Base64Coder;
import com.nightfair.mobille.util.FileUtils;
import com.nightfair.mobille.util.KeyBoardUtils;
import com.nightfair.mobille.util.MD5Util;
import com.nightfair.mobille.view.CustomDatePicker;
import com.nightfair.mobille.view.MyEditText;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

/**
 * 
 * @ClassName: Personal_detail_Activity
 * @Description: 个人资料编辑
 * @author debughao
 * @date 2015年10月10日21:43:50
 */
@SuppressLint({ "ResourceAsColor", "InflateParams", "HandlerLeak" })
public class Personal_detail_Activity extends BaseActivity implements OnClickListener {
	private Personal_detail_Activity mContext;
	private TextView tv_cancal;
	@ViewInject(R.id.personal_detail_face)
	private RelativeLayout Rl_face;
	private MyEditText et_nickname;
	private RelativeLayout rl_nicakname;
	private TextView tv_complete;
	private ImageView iv_face;
	private Dialog dialog;
	private TextView tv_sex;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_age;
	private RelativeLayout rl_star;
	private RelativeLayout rl_hometown;
	private RelativeLayout rl_address;
	private RelativeLayout rl_autograph;
	private TextView tv_age;
	private TextView tv_star;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		mContext = this;
		ViewUtils.inject(this);
		ActivityUtils.setActionBarByColor(mContext, R.layout.title_bar_personal_detail, R.color.title_color);
		inintView();
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Calendar year = (Calendar) msg.obj;
			switch (msg.what) {
			case 1:
				String age = calculateDatePoor(year);
				String StarSeat = getStarSeat(year.get(Calendar.MONTH) + 1, year.get(Calendar.DAY_OF_MONTH));
				tv_age.setText(age);
				tv_star.setText(StarSeat);
				break;
			default:
				break;
			}

		}
	};

	private void inintView() {
		tv_cancal = (TextView) findViewById(R.id.tv_ps_detail_cancel);
		et_nickname = (MyEditText) findViewById(R.id.et_ps_detail_nickname);
		rl_nicakname = (RelativeLayout) findViewById(R.id.personal_detail_nicakname);
		rl_sex = (RelativeLayout) findViewById(R.id.personal_detail_sex);
		rl_age = (RelativeLayout) findViewById(R.id.personal_detail_age);
		rl_star = (RelativeLayout) findViewById(R.id.personal_detail_star);
		rl_hometown = (RelativeLayout) findViewById(R.id.personal_detail_hometown);
		rl_address = (RelativeLayout) findViewById(R.id.personal_detail_address);
		rl_autograph = (RelativeLayout) findViewById(R.id.personal_detail_autograph);
		tv_complete = (TextView) findViewById(R.id.tv_ps_detail_complete);
		iv_face = (ImageView) findViewById(R.id.iv_personal_face);
		tv_sex = (TextView) findViewById(R.id.tvv_ps_detail_sex);
		tv_age = (TextView) findViewById(R.id.tvv_ps_detail_age);
		tv_star = (TextView) findViewById(R.id.tvv_ps_detail_star);
		mySetOnClickListener(tv_cancal, Rl_face, rl_nicakname, rl_age, rl_sex, rl_star, rl_hometown, rl_autograph,
				tv_complete, rl_address, tv_complete);
		setTextChangedListener(et_nickname, tv_sex, tv_age, tv_star);
	}

	/**
	 * 
	 * @Title: setTextChangedListener
	 * 
	 * @Description: 监听文本内容有无改变
	 * 
	 */

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
			showPhotoDialog();
			break;
		case R.id.personal_detail_nicakname:
			KeyBoardUtils.openKeybord(et_nickname, mContext);
			break;
		case R.id.personal_detail_sex:
			showSexDialog();
			break;
		case R.id.personal_detail_age:
			showAgeStarDialog();
			break;
		case R.id.personal_detail_star:
			showAgeStarDialog();
			break;
		case R.id.personal_detail_hometown:
			showSexDialog();
			break;
		case R.id.personal_detail_address:
			showSexDialog();
			break;
		case R.id.personal_detail_autograph:
			showSexDialog();
			break;
		case R.id.openCamera:// 打开相机
			openCamera();
			break;
		case R.id.openPhones:// 打开图库
			openPhones();
			break;
		case R.id.cancel:// 取消
			dialog.cancel();
			break;
		default:
			break;
		}

	}

	private void openPhones() {
		dialog.dismiss();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 1);
	}

	private void openCamera() {
		dialog.dismiss();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(FilePath.getCameraStore(mContext), MD5Util.MD5("headface") + ".png")));
		startActivityForResult(intent, 2);
	}

	@SuppressWarnings("deprecation")
	private void showPhotoDialog() {
		View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
		view.findViewById(R.id.openCamera).setOnClickListener(this);
		view.findViewById(R.id.openPhones).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	private void showSexDialog() {

		View view = getLayoutInflater().inflate(R.layout.sex_choose_dialog, null);
		view.findViewById(R.id.open_man).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_sex.setText("男");

			}
		});
		view.findViewById(R.id.open_women).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_sex.setText("女");

			}
		});
		view.findViewById(R.id.open_sercet).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_sex.setText("保密");

			}
		});
		view.findViewById(R.id.sex_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	private void showAgeStarDialog() {
		View view = getLayoutInflater().inflate(R.layout.age_star_choose_dialog, null);
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		CustomDatePicker datePicker = (CustomDatePicker) view.findViewById(R.id.cdp_age);
		datePicker.setMaxDate(new Date().getTime());
		final Calendar inintTime = Calendar.getInstance();
		inintTime.setTimeInMillis(System.currentTimeMillis());
		int year1 = inintTime.get(Calendar.YEAR);
		int month1 = inintTime.get(Calendar.MONTH);
		int day1 = inintTime.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year1, month1 + 1, day1, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inintTime.set(year, monthOfYear, dayOfMonth);
				Message message = new Message();
				message.what = 1;
				message.obj = inintTime;
				handler.sendMessage(message);
			}
		});
		view.findViewById(R.id.age_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
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

			if (resultCode == RESULT_OK) {
				photoZoom(FilePath.getCameraStore(mContext) + File.separator + MD5Util.MD5("headface") + ".png");
			} else {
				System.out.println("并无拍照");
			}
			break;
		// 裁剪后的图片上传
		case 3:
			// ActivityUtils.showShortToast(mContent, "裁剪成功");
			if (resultCode == RESULT_OK) {
				final byte[] b = data.getByteArrayExtra("data");
				final String picStr = new String(Base64Coder.encodeLines(b));
				RequestParams params = new RequestParams();// 默认utf-8 编码
				// 看样子NameValuePair是http里面的对象，查了多个网页只找到他的用法，apache官方文档也没有太多介绍
				// 暂时先记着他的用法就可以，具体用法请自行百度。
				List<NameValuePair> params2 = new ArrayList<NameValuePair>();

				params2.add(new BasicNameValuePair("picStr", picStr));
				params.addBodyParameter(params2);
				HttpUtils http = new HttpUtils();
				http.send(HttpMethod.POST, ApiUrl.UserUploadHd, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("头像上传成功");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						System.out.println("成功");
						Toast.makeText(mContext, "头像上传成功", Toast.LENGTH_SHORT).show();
						Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
						if (null != bitmap) {
							iv_face.setImageBitmap(bitmap);
							FileUtils.writeSDCard(FilePath.getHeadStore(mContext), MD5Util.MD5("headface") + ".png",
									bitmap);
						}
					}

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}
				});
			} else {
				System.out.println("并无拍照");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 
	 * @Title: photoZoom
	 * 
	 * @Description: 裁剪图片处理
	 * 
	 * @param path
	 *            图片路径
	 * 
	 * @return void 返回类型
	 */
	private void photoZoom(String path) {
		/* Bitmap bitmap = BitmapFactory.decodeFile(path); */
		Intent intent = new Intent("com.nightfair.camera.action.CROP");
		intent.putExtra("path", path);
		startActivityForResult(intent, 3);
	}

	public static final String calculateDatePoor(Calendar birthyear) {
		int age = 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		final int nowYear = c.get(Calendar.YEAR);
		int nowMonth = c.get(Calendar.MONTH) + 1;
		int nowday = c.get(Calendar.DAY_OF_MONTH);
		int year = birthyear.get(Calendar.YEAR);
		int mounth = birthyear.get(Calendar.MONTH) + 1;
		int day = birthyear.get(Calendar.DAY_OF_MONTH);
		if (year >= nowYear)
			age = 0;
		else {
			if (year < nowYear) {
				age = nowYear - year;
				if (mounth < nowMonth)
					age = nowYear - year + 1;
				else if (mounth == nowMonth) {
					if (day <= nowday)
						age = nowYear - year + 1;
				}
			}
		}
		return String.valueOf(age);
	}

	/**
	 * 通过日期来确定星座
	 * 
	 * @param mouth
	 * @param day
	 * @return
	 */
	public static String getStarSeat(int mouth, int day) {
		String starSeat = null;

		if ((mouth == 3 && day >= 21) || (mouth == 4 && day <= 19)) {
			starSeat = "白羊座";
		} else if ((mouth == 4 && day >= 20) || (mouth == 5 && day <= 20)) {
			starSeat = "金牛座";
		} else if ((mouth == 5 && day >= 21) || (mouth == 6 && day <= 21)) {
			starSeat = "双子座";
		} else if ((mouth == 6 && day >= 22) || (mouth == 7 && day <= 22)) {
			starSeat = "巨蟹座";
		} else if ((mouth == 7 && day >= 23) || (mouth == 8 && day <= 22)) {
			starSeat = "狮子座";
		} else if ((mouth == 8 && day >= 23) || (mouth == 9 && day <= 22)) {
			starSeat = "处女座";
		} else if ((mouth == 9 && day >= 23) || (mouth == 10 && day <= 23)) {
			starSeat = "天秤座";
		} else if ((mouth == 10 && day >= 24) || (mouth == 11 && day <= 22)) {
			starSeat = "天蝎座";
		} else if ((mouth == 11 && day >= 23) || (mouth == 12 && day <= 21)) {
			starSeat = "射手座";
		} else if ((mouth == 12 && day >= 22) || (mouth == 1 && day <= 19)) {
			starSeat = "摩羯座";
		} else if ((mouth == 1 && day >= 20) || (mouth == 2 && day <= 18)) {
			starSeat = "水瓶座";
		} else {
			starSeat = "双鱼座";
		}
		return starSeat;
	}

	@Override
	public void finish() {
		flag = 2;
		super.finish();
	}

}