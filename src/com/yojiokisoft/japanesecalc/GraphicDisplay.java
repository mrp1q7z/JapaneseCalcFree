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

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.yojiokisoft.japanesecalc.utils.MyResource;

/**
 * グラフィカルディスプレイ
 */
public class GraphicDisplay extends AbstractDisplay {
	private int mCharWidth;
	private int mCharHeight;
	private int mMemoryWidth = 10; // dp

	private int mOrientation;
	private ViewGroup mDisplayContainer;
	private ImageView[] mNum = new ImageView[DISPLAY_DIGIT + 1];
	private ImageView mMemory;
	private ImageView mTen;
	private ImageView mError;
	private ImageView mUnitOku;
	private ImageView mUnitMan;
	private ImageView mUnitSen;
	private int[] mNumResId = new int[10];

	public GraphicDisplay(ViewGroup viewGroup, Context context, int orientation) {
		mOrientation = orientation;
		mDisplayContainer = viewGroup;
		FrameLayout frameLayout = new FrameLayout(context);
		LinearLayout linearLayout = new LinearLayout(context);

		mMemoryWidth = MyResource.dpi2Px(mMemoryWidth);
		mMemory = new ImageView(context);
		mMemory.setImageResource(R.drawable.memory);
		mMemory.setVisibility(View.INVISIBLE);
		linearLayout.addView(mMemory);
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			linearLayout.setOrientation(LinearLayout.VERTICAL);
		}

		int margin = MyResource.dpi2Px(2);
		Pair<Integer, Integer> wh = MyResource.getScreenWidthAndHeight((Activity) context);
		int displaySize;
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			displaySize = wh.second - MyResource.getStatusBarHeight();
			mCharWidth = MyResource.dpi2Px(25);
			mCharHeight = (displaySize - mMemoryWidth - margin) / (DISPLAY_DIGIT + 1);
		} else {
			displaySize = wh.first;
			mCharWidth = (displaySize - mMemoryWidth - margin) / (DISPLAY_DIGIT + 1);
			mCharHeight = MyResource.dpi2Px(33);
		}

		for (int i = 0; i < mNumResId.length; i++) {
			mNumResId[i] = MyResource.getResourceIdByName("num" + i);
		}
		for (int i = DISPLAY_DIGIT; i >= 0; i--) {
			mNum[i] = new ImageView(context);
			mNum[i].setScaleType(ScaleType.FIT_XY);
			linearLayout.addView(mNum[i], new LinearLayout.LayoutParams(mCharWidth, mCharHeight));
		}

		mUnitOku = new ImageView(context);
		mUnitOku.setImageResource(R.drawable.unit_oku);
		mUnitMan = new ImageView(context);
		mUnitMan.setImageResource(R.drawable.unit_man);
		mUnitSen = new ImageView(context);
		mUnitSen.setImageResource(R.drawable.unit_sen);

		mTen = new ImageView(context);
		mTen.setImageResource(R.drawable.ten);

		mError = new ImageView(context);
		mError.setImageResource(R.drawable.error);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.LEFT);
		mUnitOku.setLayoutParams(layoutParams);
		mUnitMan.setLayoutParams(layoutParams);
		mUnitSen.setLayoutParams(layoutParams);
		mTen.setLayoutParams(layoutParams);

		frameLayout.addView(linearLayout);
		frameLayout.addView(mUnitOku);
		frameLayout.addView(mUnitMan);
		frameLayout.addView(mUnitSen);
		frameLayout.addView(mTen);
		frameLayout.addView(mError);
		viewGroup.addView(frameLayout);

		clear();
	}

	@Override
	public void showDisplay(boolean format) {
		StringBuffer sb = stack2String();

		// 小数点のゼロは省く
		if (format && mCommaMode && mDecimalPlaces > 0) {
			sb = omitDecimalZero(sb);
			string2Stack(sb);
		}
		System.out.println(sb);
		dispText(sb);
	}

	private void dispText(StringBuffer sb) {
		mTen.setVisibility(View.INVISIBLE);

		int margin3 = MyResource.dpi2Px(3);
		int margin1 = MyResource.dpi2Px(1);
		int marginX = (int) (mCharWidth * 0.3);

		int index = 0;
		for (int i = sb.length() - 1; i >= 0; i--) {
			char c = sb.charAt(i);
			switch (c) {
			case '-':
				mNum[index].setImageResource(R.drawable.minus);
				mNum[index].setVisibility(View.VISIBLE);
				index++;
				break;
			case '.':
				int left;
				int top;
				if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					left = mCharWidth - margin3;
					top = mCharHeight * (sb.length() - 1 - index) + mMemoryWidth - marginX;
				} else {
					left = mCharWidth * (DISPLAY_DIGIT - index + 1) + mMemoryWidth - marginX;
					top = mCharHeight - margin1;
				}
				mTen.setPadding(left, top, 0, 0);
				mTen.setVisibility(View.VISIBLE);
				break;
			default:
				int n = Integer.parseInt("" + c);
				mNum[index].setImageResource(mNumResId[n]);
				mNum[index].setVisibility(View.VISIBLE);
				index++;
				break;
			}
		}
		for (int i = index; i < DISPLAY_DIGIT + 1; i++) {
			if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
				mNum[i].setVisibility(View.GONE);
			} else {
				mNum[i].setVisibility(View.INVISIBLE);
			}
		}

		ImageView[] unit = { mUnitOku, mUnitMan, mUnitSen };
		int[] keta = { 8, 4, 3 };
		int ketaSu = mDisplayChar.size() - mDecimalPlaces;
		for (int i = 0; i < keta.length; i++) {
			if (ketaSu > keta[i]) {
				unit[i].setVisibility(View.VISIBLE);
				int left;
				int top;
				if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					left = mCharWidth - margin3;
					top = mCharHeight * (sb.length() - keta[i] - (mDecimalPlaces == 0 ? 0 : mDecimalPlaces + 1))
							+ mMemoryWidth - marginX;
				} else {
					left = mCharWidth * (DISPLAY_DIGIT - keta[i] - mDecimalPlaces + 1) + mMemoryWidth - marginX;
					top = mCharHeight - margin1;
				}
				unit[i].setPadding(left, top, 0, 0);
			} else {
				unit[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 小数点のゼロは省く
	 * @param sb
	 * @return 小数点のゼロを省いた数値
	 */
	private StringBuffer omitDecimalZero(StringBuffer sb) {
		StringBuffer sbOut = new StringBuffer();
		boolean commaFlag = false;
		for (int i = sb.length() - 1; i >= 0; i--) {
			if (commaFlag) {
				sbOut.insert(0, sb.charAt(i));
			} else {
				if (sb.charAt(i) == '0') {
					// 小数点の最初のゼロは読み飛ばす
					;
				} else if (sb.charAt(i) == '.') {
					// 小数点部位が全てゼロならコンマは出力しない
					commaFlag = true;
				} else {
					commaFlag = true;
					sbOut.insert(0, sb.charAt(i));
				}
			}
		}
		return sbOut;
	}

	/**
	 * スタックから文字列へ
	 * 例)displayChar:123 decimalPlaces:2 commaMode:true minus:true
	 * → -1.23
	 * 
	 * @return 文字列化した数値
	 */
	private StringBuffer stack2String() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mDisplayChar.size(); i++) {
			String str = mDisplayChar.get(i);
			sb.append(str);
		}
		// コンマの位置を判定
		if (mCommaMode && mDecimalPlaces > 0) {
			sb.insert(sb.length() - mDecimalPlaces, ".");
		}
		// 空の場合はゼロを表示
		if (sb.length() == 0) {
			sb.append("0");
		} else if (mMinus) {
			// 符号を表示
			sb.insert(0, "-");
		}
		return sb;
	}

	/**
	 * 文字列からスタックへ
	 * 例)-1.23
	 * → displayChar:123 decimalPlaces:2 commaMode:true minus:true
	 */
	private void string2Stack(StringBuffer sb) {
		StringBuffer sb2 = stack2String();
		if (sb.toString().equals(sb2.toString())) {
			return;
		}

		clear();

		int len = sb.length();
		for (int i = 0; i < len; i++) {
			char chr = sb.charAt(i);
			if (chr == '.') {
				mCommaMode = true;
			} else if (chr == '-') {
				mMinus = true;
			} else {
				mDisplayChar.push(String.valueOf(chr));
				if (mCommaMode) {
					mDecimalPlaces++;
				}
			}
			// 桁数が表示桁数を超える部分は入れない
			if (mDisplayChar.size() >= DISPLAY_DIGIT) {
				break;
			}
		}
	}

	@Override
	public void onInputNumber(Number num) {
		switch (num) {
		case DOUBLE_ZERO:
			addNumber(num);
			addNumber(num);
			break;
		case COMMA:
			if (!mCommaMode) {
				mCommaMode = true;
				mDecimalPlaces = 0;
			}
			break;
		default:
			addNumber(num);
			break;
		}
	}

	private void addNumber(Number num) {
		if (mDisplayChar.size() < DISPLAY_DIGIT) {
			mDisplayChar.push(num.getValue());
			if (mCommaMode) {
				mDecimalPlaces++;
			}
			mDisplayNumber = null;
		}
	}

	@Override
	public void onInputBackspace() {
		if (mDisplayChar.isEmpty()) {
			return;
		}

		mDisplayChar.pop();
		if (mCommaMode) {
			mDecimalPlaces--;
			if (mDecimalPlaces == 0) {
				mCommaMode = false;
			}
		}
		mDisplayNumber = null;
	}

	@Override
	public void clear() {
		mDisplayNumber = null;
		mCommaMode = false;
		mDecimalPlaces = 0;
		mMinus = false;
		mDisplayChar.clear();
		clearError();
		mUnitOku.setVisibility(View.INVISIBLE);
		mUnitMan.setVisibility(View.INVISIBLE);
		mUnitSen.setVisibility(View.INVISIBLE);
		mTen.setVisibility(View.INVISIBLE);
	}

	@Override
	public BigDecimal getNumber() {
		if (mDisplayNumber != null) {
			BigDecimal ret = mDisplayNumber;
			mDisplayNumber = null;
			return ret;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mDisplayChar.size(); i++) {
			String str = mDisplayChar.get(i);
			sb.append(str);
		}
		// コンマの位置を判定
		if (mCommaMode && mDecimalPlaces > 0) {
			sb.insert(sb.length() - mDecimalPlaces, ".");
		}
		// マイナス記号を追加
		if (mMinus) {
			sb.insert(0, "-");
		}

		try {
			return new BigDecimal(sb.toString());
		} catch (Exception e) {
			return new BigDecimal("0");
		}
	}

	@Override
	public void setNumber(BigDecimal d) {
		this.clear();
		StringBuffer formatStr = new StringBuffer();
		formatStr.append("%.");
		formatStr.append(String.valueOf(DISPLAY_DIGIT));
		formatStr.append("f");
		String d2 = String.format(formatStr.toString(), d.abs().doubleValue());
		// 数値を文字列かしてスタックに追加
		StringBuffer sb = new StringBuffer(d2);
		if (d.compareTo(new BigDecimal("0")) < 0) {
			sb.insert(0, "-");
		}
		StringBuffer sbOmitZero = omitDecimalZero(sb);
		string2Stack(sbOmitZero);
		mDisplayNumber = d;
	}

	@Override
	public void setError() {
		clear();
		mDisplayContainer.setBackgroundColor(Color.BLACK);
		mError.setVisibility(View.VISIBLE);
	}

	@Override
	public void clearError() {
		mDisplayContainer.setBackgroundColor(Color.WHITE);
		mError.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setMemory(double d) {
		if (d == 0) {
			mMemory.setVisibility(View.INVISIBLE);
		} else {
			mMemory.setVisibility(View.VISIBLE);
		}
	}
}