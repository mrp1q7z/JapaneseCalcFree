package com.yojiokisoft.japanesecalc;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

		CustomHorizontalScrollView scrollView = (CustomHorizontalScrollView) findViewById(R.id.horizontal_scrollview);
		LinearLayout imageContainer = (LinearLayout) findViewById(R.id.image_container);
		mLeftArrow = (ImageView) findViewById(R.id.left_arrow);
		mRightArrow = (ImageView) findViewById(R.id.right_arrow);
		mBigImage = (ImageView) findViewById(R.id.big_image);
		Button useButton = (Button) findViewById(R.id.use_button);

		scrollView.setScrollStateListener(mImageScrolled);
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
			images[i].setLayoutParams(new LayoutParams(80, 120));
			images[i].setTag(items.get(i).resourceName);
			images[i].setOnClickListener(mImageClicked);
			imageContainer.addView(images[i]);
		}
	}

	private IScrollStateListener mImageScrolled = new IScrollStateListener() {
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
