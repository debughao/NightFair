package com.nightfair.mobille.activity;

import com.lidroid.xutils.util.LogUtils;
import com.nightfair.mobille.R;
import com.nightfair.mobille.util.ActivityUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity implements OnClickListener {
	protected Context mContext;
	private TextView mTv_title;
	private ImageView iv_back;
	private ImageView mReLoadImageView;
	private WebView mWebView;
	private String url;
	private ProgressBar mProgressBar; // 进度条
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		ActivityUtils.setActionBarByColor(this, R.layout.title_bar, R.color.title_color);
		mContext = this;
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		initView();
		initWebView();
		loadurl(mWebView, url);
	}

	private void initView() {
		mTv_title = (TextView) findViewById(R.id.tv_actionbar_title);
		mTv_title.setText(R.string.actionbar_title_detail);
		iv_back = (ImageView) findViewById(R.id.iv_actionbar_back);
		mReLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
		mProgressBar = (ProgressBar) findViewById(R.id.webviewPro);
		iv_back.setOnClickListener(this);
		mReLoadImageView.setOnClickListener(this);
		mReLoadImageView.setOnClickListener(this);
		
	}

	private void initWebView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setDatabaseEnabled(true);
		// LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
		// LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
		// 总结：根据以上两种模式，建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK。
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurl(view, url);// 载入网页				
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub

				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				mWebView.setVisibility(View.GONE);
				mReLoadImageView.setVisibility(View.VISIBLE);
				
				Toast.makeText(mContext, description + " 错误代码：" + errorCode, Toast.LENGTH_SHORT).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				LogUtils.e("百分比"+progress);
				super.onProgressChanged(view, progress);
			}
		});
		/**
		 * 设置下载监听，当url为文件时候开始下载文件
		 */
		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				// download(url);
			}
			
		});

	}

	/**
	 * 载入链接
	 * 
	 * @param view
	 * @param url
	 */
	public void loadurl(final WebView view, final String url) {
		runOnUiThread(new Runnable() {

			public void run() {
				view.loadUrl(url);// 载入网页
			}
		});
	}
	/**
	 * 重新加载
	 */
	private void reload() {
		mReLoadImageView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		mWebView.reload();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_actionbar_back:
			ActivityUtils.startActivity(mContext, MainActivity.class);
			break;
		case R.id.reLoadImage:
			reload();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityUtils.startActivity(mContext, MainActivity.class);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.removeAllViews();
		mWebView.destroy();
		Log.e("程序退出:", "------- 已经退出-----");
		super.onDestroy();
	}

}
