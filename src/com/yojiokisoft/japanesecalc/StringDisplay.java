package com.yojiokisoft.japanesecalc;

import android.widget.TextView;

public class StringDisplay extends AbstractDisplay {
	private TextView txt;
	private TextView mMemory;

	public StringDisplay(TextView disp, TextView memoryMark) {
		clear();
		this.txt = disp;
		mMemory = memoryMark;
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
		txt.setText(sb);
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
		}
	}

	@Override
	public void clear() {
		commaMode = false;
		decimalPlaces = 0;
		minus = false;
		displayChar.clear();
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
	}

	@Override
	public void clearError() {
	}

	@Override
	public void setMemory(double d) {
		if (d == 0) {
			mMemory.setText("");
		} else {
			mMemory.setText("M");
		}
	}

}
