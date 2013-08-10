package com.yojiokisoft.japanesecalc;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yojiokisoft.japanesecalc.CustomHorizontalScrollView.IScrollStateListener;

public class SkinActivity extends Activity {
	private ImageView mLeftArrow;
	private ImageView mRightArrow;
	private ImageView mBigImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin);

		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			CustomScrollView scrollView = (CustomScrollView) findViewById(R.id.horizontal_scrollview);
			scrollView.setScrollStateListener(mImageVScrolled);
		} else {
			CustomHorizontalScrollView scrollView = (CustomHorizontalScrollView) findViewById(R.id.horizontal_scrollview);
			scrollView.setScrollStateListener(mImageHScrolled);
		}
		LinearLayout imageContainer = (LinearLayout) findViewById(R.id.image_container);
		mLeftArrow = (ImageView) findViewById(R.id.left_arrow);
		mRightArrow = (ImageView) findViewById(R.id.right_arrow);
		mBigImage = (ImageView) findViewById(R.id.big_image);
		ImageButton useButton = (ImageButton) findViewById(R.id.use_button);

		useButton.setOnClickListener(mUseButtonClicked);

		BackImageDao backImageDao = new BackImageDao();
		List<BackImageEntity> items = backImageDao.queryForAll();
		int resId;
		int size = items.size();
		ImageView[] images = new ImageView[size];
		for (int i = 0; i < size; i++) {
			images[i] = new ImageView(this);
			resId = MyResource.getResourceIdByName("s_" + items.get(i).resourceName);
			images[i].setImageResource(resId);
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				images[i].setLayoutParams(new LayoutParams(120, 80 + 10));
				images[i].setPadding(0, 5, 0, 5);
			} else {
				images[i].setLayoutParams(new LayoutParams(80 + 10, 120));
				images[i].setPadding(5, 0, 5, 0);
			}
			images[i].setTag(items.get(i).resourceName);
			images[i].setOnClickListener(mImageClicked);
			imageContainer.addView(images[i]);
		}

		String resName = SettingDao.getInstance().getSkin();
		resId = MyResource.getResourceIdByName(resName);
		mBigImage.setImageResource(resId);
		mBigImage.setTag(resName);
	}

	private IScrollStateListener mImageHScrolled = new IScrollStateListener() {
		public void onScrollMostRight() {
			mRightArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollMostLeft() {
			mLeftArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollFromMostLeft() {
			mLeftArrow.setVisibility(View.VISIBLE);
		}

		public void onScrollFromMostRight() {
			mRightArrow.setVisibility(View.VISIBLE);
		}
	};

	private CustomScrollView.IScrollStateListener mImageVScrolled = new CustomScrollView.IScrollStateListener() {
		public void onScrollMostBottom() {
			mRightArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollMostTop() {
			mLeftArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollFromMostTop() {
			mLeftArrow.setVisibility(View.VISIBLE);
		}

		public void onScrollFromMostBottom() {
			mRightArrow.setVisibility(View.VISIBLE);
		}
	};

	private OnClickListener mImageClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String resName = (String) v.getTag();
			int resId = MyResource.getResourceIdByName(resName);
			mBigImage.setImageResource(resId);
			mBigImage.setTag(resName);
		}
	};

	private OnClickListener mUseButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String resName = (String) mBigImage.getTag();
			SettingDao.getInstance().setSkin(resName);
			finish();
		}
	};
}
