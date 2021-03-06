package com.nightfair.mobille.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.base.CascadeCityActivity;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.bean.User;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.config.FilePathConfig;
import com.nightfair.mobille.dialog.AutoGraphDialog;
import com.nightfair.mobille.dialog.BaseDialog.OnConfirmListener;
import com.nightfair.mobille.dialog.CancalDialog;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.Base64Coder;
import com.nightfair.mobille.util.ErrCodeUtils;
import com.nightfair.mobille.util.FileUtils;
import com.nightfair.mobille.util.KeyBoardUtils;
import com.nightfair.mobille.util.MD5Util;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.util.StringUtils;
import com.nightfair.mobille.util.ToastUtil;
import com.nightfair.mobille.view.CustomDatePicker;
import com.nightfair.mobille.view.MyEditText;
import com.nightfair.mobille.widget.OnWheelChangedListener;
import com.nightfair.mobille.widget.WheelView;
import com.nightfair.mobille.widget.adapter.ArrayWheelAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.UpdateListener;
import android.widget.ImageView;
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
@SuppressLint({ "ResourceAsColor", "InflateParams", "HandlerLeak" })
public class PersonaDetailActivity extends CascadeCityActivity implements OnClickListener, OnWheelChangedListener {
	private PersonaDetailActivity mContext;
	private TextView tv_cancal;
	@ViewInject(R.id.personal_detail_face)
	private RelativeLayout Rl_face;
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
	private MyEditText tv_nickname;
	private TextView tv_age;
	private TextView tv_star;
	private TextView tv_hometown;
	private TextView tv_address;
	private TextView tv_autograph;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private String mAddress;
	private int addressFlag;
	private String nickname;
	private String sex;
	private String age;
	private String star;
	private String hometown;
	private String address;
	private String autograph;
	private String image;
	private BuyerInfo buyerInfo;
	private ProgressDialog dialog1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		mContext = this;
		ViewUtils.inject(this);
		ActivityUtils.setActionBarByColor(mContext, R.layout.title_bar_personal_detail, R.color.title_color);
		inintView();
		inintUserInfo();
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
		tv_nickname = (MyEditText) findViewById(R.id.et_ps_detail_nickname);
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
		tv_hometown = (TextView) findViewById(R.id.tvv_ps_detail_hometown);
		tv_address = (TextView) findViewById(R.id.tvv_ps_detail_address);
		tv_autograph = (TextView) findViewById(R.id.tvv_ps_detail_autograph);
		mySetOnClickListener(tv_cancal, Rl_face, rl_nicakname, rl_age, rl_sex, rl_star, rl_hometown, rl_autograph,
				tv_complete, rl_address, tv_complete, rl_autograph);
		setTextChangedListener(tv_nickname, tv_sex, tv_age, tv_star, tv_hometown, tv_address, tv_autograph);
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
			if (tv_complete.isEnabled())
				showCancalDialog();
			else
				finish();
			break;
		case R.id.tv_ps_detail_complete:
			UpdateInfo();
			break;
		case R.id.personal_detail_face:
			showPhotoDialog();
			break;
		case R.id.personal_detail_nicakname:// 昵称
			KeyBoardUtils.openKeybord(tv_nickname, mContext);
			break;
		case R.id.personal_detail_sex:// 性别
			showSexDialog();
			break;
		case R.id.personal_detail_age:// 年龄
			showAgeStarDialog();
			break;
		case R.id.personal_detail_star:// 星座
			showAgeStarDialog();
			break;
		case R.id.personal_detail_hometown:// 故乡
			addressFlag = 0;
			hometown = buyerInfo.getHometown();
			showCityDialog();
			break;
		case R.id.personal_detail_address:// 所在地
			addressFlag = 1;
			address = buyerInfo.getAddress();
			showCityDialog();
			break;
		case R.id.personal_detail_autograph:// 签名
			showAutographDialog();
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

	private void showCancalDialog() {
		CancalDialog cancalDialog = new CancalDialog(mContext, mContext);
		cancalDialog.showCenter();
	}

	private void inintUserInfo() {
		buyerInfo = BaseApplication.mBuyerDao.queryinfo(BaseApplication.userid);
		LogUtils.e("用户id" + BaseApplication.userid + "-->" + buyerInfo);
		RequestQueue queue = Volley.newRequestQueue(mContext);
		String image_url = BaseApplication.buyerInfo.getImage();
		ImageRequest imageRequest = new ImageRequest((AppConstants.SERVERIP + image_url), new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap arg0) {
				iv_face.setImageBitmap(arg0);
			}
		}, 0, 0, Config.RGB_565, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				iv_face.setImageResource(R.drawable.my_dd_icon_default);
			}
		});
		queue.add(imageRequest);

		if (buyerInfo != null) {
			tv_nickname.setText(buyerInfo.getNickname());
			tv_age.setText(buyerInfo.getAge());
			tv_sex.setText(buyerInfo.getSex());
			tv_star.setText(buyerInfo.getStar());
			tv_hometown.setText(buyerInfo.getHometown());
			tv_address.setText(buyerInfo.getAddress());
			tv_autograph.setText(buyerInfo.getAutograph());
			/*
			 * 设置完成按钮点击无动作和文本颜色（并不是不可点击)
			 */
			tv_complete.setTextColor(getResources().getColor(R.color.not_click_white));
			tv_complete.setEnabled(false);
		}

	}

	/**
	 * 
	 * @Title: UpdateInfo
	 * 
	 * @Description: TODO(更新个人资料)
	 * @param 参数
	 * @return void 返回类型
	 * 
	 */
	private void UpdateInfo() {
		BaseApplication.buyerInfo.setNickname(nickname = tv_nickname.getText().toString().trim());
		BaseApplication.buyerInfo.setSex(sex = tv_sex.getText().toString());
		BaseApplication.buyerInfo.setAge(age = tv_age.getText().toString());
		BaseApplication.buyerInfo.setStar(star = tv_star.getText().toString());
		BaseApplication.buyerInfo.setHometown(hometown = tv_hometown.getText().toString());
		BaseApplication.buyerInfo.setAddress(address = tv_address.getText().toString());
		BaseApplication.buyerInfo.setAutograph(autograph = tv_autograph.getText().toString());
		/**
		 * buyerInfo保存本地数据库,数据持久化
		 */
		buyerInfo = new BuyerInfo(nickname, sex, age, star, hometown, address, autograph, image);
		LogUtils.e("详情页" + buyerInfo + BaseApplication.userid);
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", AppConstants.KEY);
		params.addBodyParameter("action", "info");
		params.addBodyParameter("nickname", nickname);
		params.addBodyParameter("sex", sex);
		params.addBodyParameter("age", age);
		params.addBodyParameter("star", star);
		params.addBodyParameter("hometown", hometown);
		params.addBodyParameter("address", address);
		params.addBodyParameter("autograph", autograph);
		HttpUtils httpUtils = BaseApplication.httpUtils;
		httpUtils.send(HttpMethod.POST, AppConstants.USERUPDATE, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (NetUtils.getHttpException(arg1).equals("org.apache.http.conn.ConnectTimeoutException"))
					ToastUtil.showCenter(mContext, ErrCodeUtils.NORMAL_LOGIN_TIMEOUT);
				else
					ToastUtil.showCenter(mContext, ErrCodeUtils.NORMAL_LOGIN_NONNETWORK);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject jsonObject = new JSONObject(arg0.result);
					int status = jsonObject.getInt("status");
					String result = jsonObject.getString("result");
					if (status == 200) {
						updateUsernick(tv_nickname.getText().toString().trim());
						BaseApplication.mBuyerDao.insertInfo(BaseApplication.buyerInfo, BaseApplication.userid);
						ToastUtil.show(mContext, result);
						Intent intent = new Intent();
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

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
				Uri.fromFile(new File(FilePathConfig.getCameraStore(mContext), MD5Util.MD5("headface") + ".png")));
		startActivityForResult(intent, 2);
	}

	/**
	 * @Title: showPhotoDialog
	 * 
	 * @Description: TODO(处理照片选择)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void showPhotoDialog() {
		View view = getLayoutInflater().inflate(R.layout.dialog_photo_choose, null);
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

	/**
	 * @Title: showPhotoDialog
	 * 
	 * @Description: TODO(处理性别选择)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void showSexDialog() {

		View view = getLayoutInflater().inflate(R.layout.dialog_sex_choose, null);
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

	/**
	 * @Title: showPhotoDialog
	 * 
	 * @Description: TODO(处理年龄及星座选择)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void showAgeStarDialog() {
		View view = getLayoutInflater().inflate(R.layout.dialog_age_star_choose, null);
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
	 * @Title: showPhotoDialog
	 * 
	 * @Description: TODO(处理地理位置选择)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void showCityDialog() {
		View view = getLayoutInflater().inflate(R.layout.dialog_city_choose, null);
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		Button mBtnConfirm = (Button) view.findViewById(R.id.city_ok);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAddress = mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName;
				if (!TextUtils.isEmpty(mAddress)) {
					switch (addressFlag) {
					case 0:
						tv_hometown.setText(mAddress);
						dialog.dismiss();
						break;
					case 1:
						tv_address.setText(mAddress);
						dialog.dismiss();
						break;
					default:
						break;
					}
				}
				dialog.dismiss();
			}
		});
		initdata();
		setUpListener();
		setUpData();
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * @Title: showAutographDialog
	 * 
	 * @Description: TODO(处理个人签名填写)
	 * 
	 */
	private void showAutographDialog() {
		AutoGraphDialog dialog = new AutoGraphDialog(mContext, autograph, new OnConfirmListener() {

			@Override
			public void onConfirm(String result) {
				tv_autograph.setText(result);
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 
	 * @Title: photoZoom
	 * 
	 * @Description:TODO(裁剪图片处理）
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

	/**
	 * 
	 * @Title: calculateDatePoor
	 * 
	 * @Description: TODO(计算年龄)
	 * 
	 * @param birthyear
	 * 
	 * @return String 返回类型
	 */
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
	 * @Description:TODO(通过日期来确定星座)
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
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities(0, 0);
		} else if (wheel == mViewCity) {
			updateAreas(0);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}

	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setUpData() {

		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		mViewProvince.setCurrentItem(pPosition);
		updateCities(cPosition, aPosition);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities(int cPosition, int aPosition) {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(cPosition);
		updateAreas(aPosition);
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas(int aPosition) {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(aPosition);
		int dCurrent = mViewDistrict.getCurrentItem();
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[dCurrent];
	}

	private void initdata() {
		initProvinceDatas();
		if (addressFlag == 0) {
			if (!TextUtils.isEmpty(hometown)) {
				String[] hometown1 = StringUtils.convertStrToArry(hometown);
				// System.out.println("故乡"+hometown);
				pPosition = StringUtils.getPosition(hometown1[0], mProvinceDatas);
				cPosition = StringUtils.getPosition(hometown1[1], mCitisDatasMap.get(hometown1[0]));
				aPosition = StringUtils.getPosition(hometown1[2], mDistrictDatasMap.get(hometown1[1]));
				// System.out.println("故乡"+pPosition+"--->"+cPosition+"--->"+cPosition);
			} else {
				System.out.println("noting get form address");
			}
		} else if (addressFlag == 1) {
			if (!TextUtils.isEmpty(address)) {
				String[] address1 = StringUtils.convertStrToArry(address);
				// System.out.println("所在地"+address);
				pPosition = StringUtils.getPosition(address1[0], mProvinceDatas);
				cPosition = StringUtils.getPosition(address1[1], mCitisDatasMap.get(address1[0]));
				aPosition = StringUtils.getPosition(address1[2], mDistrictDatasMap.get(address1[1]));
				// System.out.println("所在地"+pPosition+"--->"+cPosition+"--->"+cPosition);
			} else {
				LogUtils.i("noting get form address");
			}
		}
	}

	/**
	 * 处理选择照片事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 直接从相册获取时
		case 1:

			if (data != null) {
				photoZoom(data.getData().toString());
			} else {
				LogUtils.i("no did for headface choose a action");
			}

			break;
		// 调用相机拍照时
		case 2:

			if (resultCode == RESULT_OK) {
				photoZoom(FilePathConfig.getCameraStore(mContext) + File.separator + MD5Util.MD5("headface") + ".png");
			} else {
				LogUtils.i("no did for headface choose a action");
			}
			break;
		// 裁剪后的图片上传
		case 3:
			// ActivityUtils.showShortToast(mContent, "裁剪成功");
			if (resultCode == RESULT_OK) {
				showDialog("头像上传中...");
				final byte[] b = data.getByteArrayExtra("data");
				final String picStr = new String(Base64Coder.encodeLines(b));
				RequestParams params = new RequestParams();// 默认utf-8 编码
				// 看样子NameValuePair是http里面的对象，查了多个网页只找到他的用法，apache官方文档也没有太多介绍
				// 暂时先记着他的用法就可以，具体用法请自行百度。
				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params.addBodyParameter("action", "headface");
				params.addBodyParameter("key", AppConstants.KEY);
				params2.add(new BasicNameValuePair("picStr", picStr));
				params.addBodyParameter(params2);
				HttpUtils http = BaseApplication.httpUtils;
				http.send(HttpMethod.POST, AppConstants.USERUPDATE, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						hideDialog();
						NetUtils.coonFairException(arg1, mContext);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							System.out.println(jsonObject);
							int status = jsonObject.getInt("status");
							String result = jsonObject.getString("result");
							if (status == 200) {
								Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
								String imageurl = jsonObject.getString("imageurl");
								Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
								if (null != bitmap) {
									hideDialog();
									iv_face.setImageBitmap(bitmap);
									FileUtils.writeSDCard(FilePathConfig.getTemp(mContext),
											MD5Util.MD5("headface") + ".png", bitmap);

									BaseApplication.buyerInfo.setImage(imageurl);
									updateUserAvatar(AppConstants.SERVERIP + imageurl);
									BaseApplication.mBuyerDao.insertInfo(BaseApplication.buyerInfo,
											BaseApplication.userid);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}
				});
			} else {
				LogUtils.i("no did for headface take a photo");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateUserAvatar(final String url) {
		User u = new User();
		u.setAvatar(url);
		updateUserData(u, new UpdateListener() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(int code, String msg) {

			}
		});
	}

	private void updateUsernick(final String nick) {
		User u = new User();
		u.setNick(nick);
		updateUserData(u, new UpdateListener() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(int code, String msg) {

			}
		});
	}

	private void updateUserData(User user, UpdateListener listener) {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		User current = (User) userManager.getCurrentUser(User.class);
		user.setObjectId(current.getObjectId());
		user.update(this, listener);
	}

	public void showDialog(String message) {
		try {
			if (dialog1 == null) {
				dialog1 = new ProgressDialog(this);
				dialog1.setCancelable(false);
			}
			dialog1.setCanceledOnTouchOutside(false);
			dialog1.setCancelable(true);
			dialog1.setMessage(message);
			dialog1.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	public void hideDialog() {
		if (dialog1 != null && dialog1.isShowing())
			try {
				dialog1.dismiss();
			} catch (Exception e) {
			}
	}

	@Override
	public void finish() {
		super.finish();
	}
}