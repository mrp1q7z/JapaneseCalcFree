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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * カード詳細アダプター
 */
public class SkinPagerAdapter extends PagerAdapter {
	private ImageView mBigImage;
	private List<BackImageEntity> mList;
	private LayoutInflater mInflter;

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 */
	public SkinPagerAdapter(Context context, List<BackImageEntity> list) {
		mInflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
	}

	/**
	 * @see PagerAdapter#instantiateItem(ViewGroup, int)
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		FrameLayout layout = (FrameLayout) this.mInflter.inflate(R.layout.page_skin, null);

		mBigImage = (ImageView) layout.findViewById(R.id.big_image);

		BackImageEntity item = mList.get(position);
		String resName = item.resourceName;
		int resId = MyResource.getResourceIdByName(resName);
		mBigImage.setImageResource(resId);
		mBigImage.setTag(resName);

		// コンテナに追加
		container.addView(layout);

		return layout;
	}

	/**
	 * @see PagerAdapter#destroyItem(ViewGroup, int, Object)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// コンテナから View を削除
		container.removeView((View) object);
	}

	/**
	 * @see PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// リストのアイテム数を返す
		return mList.size();
	}

	/**
	 * @see PagerAdapter#isViewFromObject(View, Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// Object 内に View が存在するか判定する
		return view == (FrameLayout) object;
	}

	/**
	 * カードの取得.
	 * 
	 * @param position
	 * @return
	 */
	public BackImageEntity getSkin(int position) {
		return mList.get(position);
	}
}