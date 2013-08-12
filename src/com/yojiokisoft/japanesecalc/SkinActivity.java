/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.japanesecalc;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yojiokisoft.japanesecalc.CustomHorizontalScrollView.IScrollStateListener;

/**
 * 外観設定アクティビティ
 */
public class SkinActivity extends Activity {
	private ImageView mLeftArrow;
	private ImageView mRightArrow;
	private ViewPager mPager;
	private Button mUseButton;
	private String mSkinResName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin);

		mSkinResName = SettingDao.getInstance().getSkin();

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
		mPager = (ViewPager) findViewById(R.id.pager);
		mUseButton = (Button) findViewById(R.id.use_button);

		mUseButton.setOnClickListener(mUseButtonClicked);

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
			images[i].setTag(i);
			images[i].setOnClickListener(mImageClicked);
			imageContainer.addView(images[i]);
		}

		SkinPagerAdapter adapter = new SkinPagerAdapter(this, items);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(mPageChanged);
		setUseButtonText(items.get(0).resourceName);
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
			int position = (Integer) v.getTag();
			mPager.setCurrentItem(position);

			BackImageEntity item = ((SkinPagerAdapter) mPager.getAdapter()).getSkin(position);
			setUseButtonText(item.resourceName);
		}
	};

	private OnPageChangeListener mPageChanged = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			BackImageEntity item = ((SkinPagerAdapter) mPager.getAdapter()).getSkin(position);
			setUseButtonText(item.resourceName);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};

	private void setUseButtonText(String resName) {
		if (mSkinResName.equals(resName)) {
			mUseButton.setText("使用中");
			mUseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_big_on, 0, 0, 0);
		} else {
			mUseButton.setText(getString(R.string.use_this));
			mUseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_big_off, 0, 0, 0);
		}
	}

	private OnClickListener mUseButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = mPager.getCurrentItem();
			BackImageEntity item = ((SkinPagerAdapter) mPager.getAdapter()).getSkin(position);
			if (mSkinResName.equals(item.resourceName)) {
				return;
			}
			SettingDao.getInstance().setSkin(item.resourceName);
			finish();
		}
	};
}
