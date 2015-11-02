package com.nightfair.mobille.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nightfair.mobille.R;
import com.nightfair.mobille.base.Activitybase;
import com.nightfair.mobille.base.BaseApplication;
import com.nightfair.mobille.bean.Blog;
import com.nightfair.mobille.bean.BuyerInfo;
import com.nightfair.mobille.bean.User;
import com.nightfair.mobille.config.AppConstants;
import com.nightfair.mobille.config.BmobConstants;
import com.nightfair.mobille.dialog.DiaLogTips;
import com.nightfair.mobille.dialog.DiaLogTips.DialogTips;
import com.nightfair.mobille.util.CollectionUtils;
import com.nightfair.mobille.util.ImageLoadOptions;
import com.nightfair.mobille.util.NetUtils;
import com.nightfair.mobille.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人资料页面
 */
@SuppressLint({ "InflateParams", "SimpleDateFormat", "ClickableViewAccessibility" })
public class SetMyInfoActivity extends Activitybase implements OnClickListener {
	private Context mContext;
	private LinearLayout layout_all;
	private Button btn_chat, btn_back, btn_add_friend;
	private RelativeLayout layout_black_tips;

	private String from = "";
	private String username = "";
	private User user;
	private CircleImageView iv_set_avator;
	private TextView tv_set_nick, tv_set_sex, tv_set_age, tv_set_star, tv_set_hometown, tv_set_address,
			tv_set_autograph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 因为魅族手机下面有三个虚拟的导航按钮，需要将其隐藏掉，不然会遮掉拍照和相册两个按钮，且在setContentView之前调用才能生效
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 14) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
		mContext = this;
		setContentView(R.layout.activity_set_info);
		initActionbar("个人资料", 0);
		from = getIntent().getStringExtra("from");// me add other
		username = getIntent().getStringExtra("username");
		initView();
	}

	private void initView() {
		tv_set_nick = (TextView) findViewById(R.id.tv_set_nick);
		tv_set_sex = (TextView) findViewById(R.id.tv_set_sex);
		tv_set_age = (TextView) findViewById(R.id.tv_set_age);
		tv_set_star = (TextView) findViewById(R.id.tv_set_star);
		tv_set_hometown = (TextView) findViewById(R.id.tv_set_homtown);
		tv_set_address = (TextView) findViewById(R.id.tv_set_address);
		tv_set_autograph = (TextView) findViewById(R.id.tv_set_autograph);
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		iv_set_avator = (CircleImageView) findViewById(R.id.iv_set_avator);
		// 黑名单提示语
		layout_black_tips = (RelativeLayout) findViewById(R.id.layout_black_tips);
		btn_chat = (Button) findViewById(R.id.btn_chat);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
		btn_add_friend.setEnabled(false);
		btn_chat.setEnabled(false);
		btn_back.setEnabled(false);

		// 不管对方是不是你的好友，均可以发送消息--BmobIM_V1.1.2修改
		btn_chat.setVisibility(View.VISIBLE);
		btn_chat.setOnClickListener(this);
		if (from.equals("add")) {// 从附近的人列表添加好友--因为获取附近的人的方法里面有是否显示好友的情况，因此在这里需要判断下这个用户是否是自己的好友
			if (mApplication.getContactList().containsKey(username)) {// 是好友
				// btn_chat.setVisibility(View.VISIBLE);
				// btn_chat.setOnClickListener(this);
				btn_back.setVisibility(View.VISIBLE);
				btn_back.setOnClickListener(this);
			} else {
				// btn_chat.setVisibility(View.GONE);
				btn_back.setVisibility(View.GONE);
				btn_add_friend.setVisibility(View.VISIBLE);
				btn_add_friend.setOnClickListener(this);
			}
		} else {// 查看他人
			// btn_chat.setVisibility(View.VISIBLE);
			// btn_chat.setOnClickListener(this);
			btn_back.setVisibility(View.VISIBLE);
			btn_back.setOnClickListener(this);
		}
		initOtherData(username);

	}

	private void initOtherData(String name) {
		userManager.queryUser(name, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				ShowLog("onError:" + arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
					btn_chat.setEnabled(true);
					btn_back.setEnabled(true);
					btn_add_friend.setEnabled(true);
					updateUser(user);
				} else {
					ShowLog("onSuccess 查无此人");
				}
			}
		});
	}

	private void updateUser(User user) {
		// 更改

		try {
			// tv_set_name.setText(user.getUsername());
			// tv_set_nick.setText(user.getNick());
			// tv_set_gender.setText(user.getSex() == true ? "男" : "女");
			RequestParams params = new RequestParams();
			params.addBodyParameter("key", AppConstants.KEY);
			params.addBodyParameter("phone", user.getUsername());
			final HttpUtils http = BaseApplication.httpUtils;
			http.configTimeout(3000);
			http.send(HttpMethod.POST, AppConstants.GETFRAENDINFO, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					System.out.println(arg1);
					NetUtils.coonFairException(arg1, mContext);
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					String resultStatus = null;
					try {
						JSONObject jsonObject = new JSONObject(arg0.result.toString());
						resultStatus = jsonObject.get("status").toString();
						if ("200".equals(resultStatus)) {
							String result = jsonObject.optString("info");
							Gson gson = new Gson();
							BuyerInfo buyerInfo = gson.fromJson(result, new TypeToken<BuyerInfo>() {
							}.getType());
							tv_set_nick.setText(buyerInfo.getNickname());
							tv_set_age.setText(buyerInfo.getAge());
							tv_set_sex.setText(buyerInfo.getSex());
							tv_set_star.setText(buyerInfo.getStar());
							tv_set_hometown.setText(buyerInfo.getHometown());
							tv_set_address.setText(buyerInfo.getAddress());
							tv_set_autograph.setText(buyerInfo.getAutograph());
							refreshAvatar(AppConstants.SERVERIP + buyerInfo.getImage());
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
		}
		// 检测是否为黑名单用户
		if (from.equals("other")) {
			if (BmobDB.create(this).isBlackUser(user.getUsername())) {
				btn_back.setVisibility(View.GONE);
				layout_black_tips.setVisibility(View.VISIBLE);
			} else {
				btn_back.setVisibility(View.VISIBLE);
				layout_black_tips.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 更新头像 refreshAvatar
	 */
	private void refreshAvatar(String avatar) {
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_set_avator, ImageLoadOptions.getOptions());
		} else {
			iv_set_avator.setImageResource(R.drawable.default_head);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat:// 发起聊天
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("user", user);
			startAnimActivity(intent);
			finish();
			break;
		case R.id.layout_head:
			showAvatarPop();
			break;
		case R.id.btn_back:// 黑名单
			showBlackDialog(user.getUsername());
			break;
		case R.id.btn_add_friend:// 添加好友
			addFriend();
			break;
		}

	}

	/**
	 * 添加好友请求
	 */
	private void addFriend() {
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("正在添加...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// 发送tag请求
		BmobChatManager.getInstance(this).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, user.getObjectId(),
				new PushListener() {

					@Override
					public void onSuccess() {
						progress.dismiss();
						ShowToast("发送请求成功，等待对方验证！");
					}

					@Override
					public void onFailure(int arg0, final String arg1) {
						progress.dismiss();
						ShowToast("发送请求成功，等待对方验证！");
						ShowLog("发送请求失败:" + arg1);
					}
				});
	}

	/**
	 * 显示黑名单提示框
	 */
	private void showBlackDialog(final String username) {
		DiaLogTips base = new DiaLogTips();
		DialogTips dialog = base.new DialogTips(this, "加入黑名单", "加入黑名单，你将不再收到对方的消息，确定要继续吗？", "确定", true, true);
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				// 添加到黑名单列表
				userManager.addBlack(username, new UpdateListener() {

					@Override
					public void onSuccess() {
						ShowToast("黑名单添加成功!");
						btn_back.setVisibility(View.GONE);
						layout_black_tips.setVisibility(View.VISIBLE);
						// 重新设置下内存中保存的好友列表
						BaseApplication.getInstance().setContactList(
								CollectionUtils.list2map(BmobDB.create(SetMyInfoActivity.this).getContactList()));
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ShowToast("黑名单添加失败:" + arg1);
					}
				});
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}

	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;

	public String filePath = "";

	@SuppressWarnings("deprecation")
	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator, null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog("点击拍照");
				layout_choose.setBackgroundColor(getResources().getColor(R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog("点击相册");
				layout_photo.setBackgroundColor(getResources().getColor(R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * 测试关联关系是否可用
	 */
	public void addBlog() {
		// BmobRelation relation = new BmobRelation();
		// blog.setObjectId("c7a9ca9c0c");
		// relation.add(blog);
		// user.setBlogs(relation);
		final Blog blog = new Blog();
		blog.setBrief("你好");
		blog.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				BmobLog.i("blog保存成功");
				User u = new User();
				u.setBlog(blog);
				updateUserData(u, new UpdateListener() {

					@Override
					public void onSuccess() {
						BmobLog.i("user更新成功");
					}

					@Override
					public void onFailure(int code, String msg) {
						BmobLog.i("code = " + code + ",msg = " + msg);
					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	private void updateUserData(User user, UpdateListener listener) {
		User current = (User) userManager.getCurrentUser(User.class);
		user.setObjectId(current.getObjectId());
		user.update(this, listener);
	}

}
