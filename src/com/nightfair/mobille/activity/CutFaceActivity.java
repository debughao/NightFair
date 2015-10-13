package com.nightfair.mobille.activity;

import java.io.ByteArrayOutputStream;

import com.nightfair.mobille.R;
import com.nightfair.mobille.base.BaseActivity;
import com.nightfair.mobille.util.ActivityUtils;
import com.nightfair.mobille.util.FileUtils;
import com.nightfair.mobille.util.ImageSizeUtil;
import com.nightfair.mobille.util.ImageSizeUtil.ImageSize;
import com.nightfair.mobille.widget.ClipImageLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CutFaceActivity extends BaseActivity implements OnClickListener {

	private ClipImageLayout imageView;
	private CutFaceActivity context;
	private String path2;
	private ImageView tv_cancel;
	private Button btn_ok;
	private ClipImageLayout mClipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cut_face);
		context = this;
		ActivityUtils.setActionBarLayout(getActionBar(), context, R.layout.title_bar_face);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		inintView();
		path2 = FileUtils.getRealFilePath(context, path);
		Bitmap bm = loadImageFromLocal(path2, imageView);
		ClipImageLayout.mZoomImageView.setImageBitmap(bm);

	}

	private void inintView() {
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		imageView = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		tv_cancel = (ImageView) findViewById(R.id.tv_face_cancel);
		btn_ok = (Button) findViewById(R.id.btn_face_ok);
		tv_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	private Bitmap loadImageFromLocal(final String path, final ClipImageLayout imageView) {
		Bitmap bm;
		// 加载图片
		// 图片的压缩
		// 1、获得图片需要显示的大小
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图片
		bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
		return bm;
	}

	private Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, width, height);
		// 使用获得到的InSampleSize再次解析图片
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_face_cancel) {
			finish();
		} else if (v.getId() == R.id.btn_face_ok) {
			Bitmap bitmap = mClipImageLayout.clip();//图片裁剪			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] datas = baos.toByteArray();
			  Intent intent = new Intent();
			  intent.putExtra("data", datas); 
			  setResult(Activity.RESULT_OK, intent);  
              finish();
		}

	}
}
