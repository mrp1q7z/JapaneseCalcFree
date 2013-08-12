package com.yojiokisoft.japanesecalc;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GraphicDisplay extends AbstractDisplay {
	private final int CHAR_WIDTH_PORT = 36;
	private final int CHAR_HEIGHT_PORT = 50;
	private final int CHAR_WIDTH_LAND = 37;
	private final int CHAR_HEIGHT_LAND = 32;

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

		mMemory = new ImageView(context);
		mMemory.setImageResource(R.drawable.memory);
		mMemory.setVisibility(View.INVISIBLE);
		linearLayout.addView(mMemory);
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			linearLayout.setOrientation(LinearLayout.VERTICAL);
		}

		for (int i = 0; i < mNumResId.length; i++) {
			mNumResId[i] = MyResource.getResourceIdByName("num" + i);
		}
		for (int i = DISPLAY_DIGIT; i >= 0; i--) {
			mNum[i] = new ImageView(context);
			mNum[i].setScaleType(ScaleType.FIT_XY);
			if (i == DISPLAY_DIGIT) {
				if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					linearLayout.addView(mNum[i], new LinearLayout.LayoutParams(CHAR_WIDTH_LAND, CHAR_HEIGHT_LAND));
				} else {
					linearLayout.addView(mNum[i], new LinearLayout.LayoutParams((int)(CHAR_WIDTH_PORT * 0.56), CHAR_HEIGHT_PORT));
				}
			} else {
				if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					linearLayout.addView(mNum[i], new LinearLayout.LayoutParams(CHAR_WIDTH_LAND, CHAR_HEIGHT_LAND));
				} else {
					linearLayout.addView(mNum[i], new LinearLayout.LayoutParams(CHAR_WIDTH_PORT, CHAR_HEIGHT_PORT));
				}
			}
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
		if (format && commaMode && decimalPlaces > 0) {
			sb = omitDecimalZero(sb);
			string2Stack(sb);
		}
		System.out.println(sb);
		dispText(sb);
	}

	private void dispText(StringBuffer sb) {
		mTen.setVisibility(View.INVISIBLE);

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
					left = CHAR_WIDTH_LAND - 5;
					top = CHAR_HEIGHT_LAND * (sb.length() - 1 - index) + 5;
				} else {
					left = CHAR_WIDTH_PORT * (DISPLAY_DIGIT - index) + (int) (CHAR_WIDTH_PORT * 0.6);
					top = CHAR_HEIGHT_PORT - 2;
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
		int ketaSu = displayChar.size() - decimalPlaces;
		for (int i = 0; i < keta.length; i++) {
			if (ketaSu > keta[i]) {
				unit[i].setVisibility(View.VISIBLE);
				int left;
				int top;
				if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					left = CHAR_WIDTH_LAND - 5;
					top = CHAR_HEIGHT_LAND * (sb.length() - keta[i] - (decimalPlaces == 0 ? 0 : decimalPlaces + 1)) + 5;
				} else {
					left = CHAR_WIDTH_PORT * (DISPLAY_DIGIT - keta[i] - decimalPlaces) + (int) (CHAR_WIDTH_PORT * 0.6);
					top = CHAR_HEIGHT_PORT - 2;
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
		for (int i = 0; i < displayChar.size(); i++) {
			String str = displayChar.get(i);
			sb.append(str);
		}
		// コンマの位置を判定
		if (commaMode && decimalPlaces > 0) {
			sb.insert(sb.length() - decimalPlaces, ".");
		}
		// 空の場合はゼロを表示
		if (sb.length() == 0) {
			sb.append("0");
		} else if (minus) {
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
				commaMode = true;
			} else if (chr == '-') {
				minus = true;
			} else {
				displayChar.push(String.valueOf(chr));
				if (commaMode) {
					decimalPlaces++;
				}
			}
			// 桁数が表示桁数を超える部分は入れない
			if (displayChar.size() >= DISPLAY_DIGIT) {
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
			if (!commaMode) {
				commaMode = true;
				decimalPlaces = 0;
			}
			break;
		default:
			addNumber(num);
			break;
		}
	}

	private void addNumber(Number num) {
		if (displayChar.size() < DISPLAY_DIGIT) {
			displayChar.push(num.getValue());
			if (commaMode) {
				decimalPlaces++;
			}
		}
	}

	@Override
	public void onInputBackspace() {
		if (displayChar.isEmpty()) {
			return;
		}

		displayChar.pop();
		if (commaMode) {
			decimalPlaces--;
			if (decimalPlaces == 0) {
				commaMode = false;
			}
		}
	}

	@Override
	public void clear() {
		commaMode = false;
		decimalPlaces = 0;
		minus = false;
		displayChar.clear();
		clearError();
		mUnitOku.setVisibility(View.INVISIBLE);
		mUnitMan.setVisibility(View.INVISIBLE);
		mUnitSen.setVisibility(View.INVISIBLE);
		mTen.setVisibility(View.INVISIBLE);
	}

	@Override
	public double getNumber() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < displayChar.size(); i++) {
			String str = displayChar.get(i);
			sb.append(str);
		}
		// コンマの位置を判定
		if (commaMode && decimalPlaces > 0) {
			sb.insert(sb.length() - decimalPlaces, ".");
		}
		// マイナス記号を追加
		if (minus) {
			sb.insert(0, "-");
		}

		try {
			return Double.parseDouble(sb.toString());
		} catch (Exception e) {
			return 0d;
		}
	}

	@Override
	public void setNumber(double d) {
		this.clear();
		StringBuffer formatStr = new StringBuffer();
		formatStr.append("%.");
		formatStr.append(String.valueOf(DISPLAY_DIGIT));
		formatStr.append("f");
		String d2 = String.format(formatStr.toString(), Math.abs(d));
		// 数値を文字列かしてスタックに追加
		StringBuffer sb = new StringBuffer(d2);
		if (d < 0) {
			sb.insert(0, "-");
		}
		StringBuffer sbOmitZero = omitDecimalZero(sb);
		string2Stack(sbOmitZero);
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