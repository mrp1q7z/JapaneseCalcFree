package com.yojiokisoft.japanesecalc;

import android.widget.TextView;

public class StringDisplay extends AbstractDisplay {
	private TextView txt;

	public StringDisplay(TextView disp) {
		clear();
		this.txt = disp;
	}

	@Override
	public void showDisplay(boolean format) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<displayChar.size(); i++) {
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
		}
		// 符号を表示
		if (minus) {
			sb.insert(0, "-");
		} else {
			;
		}
		// 小数点のゼロは省く
		if (format && commaMode && decimalPlaces > 0) {
			StringBuffer sbOut = new StringBuffer();
			boolean commaFlag = false;
			for (int i=sb.length()-1; i>=0; i--) {
				if (commaFlag) {
					sbOut.insert(0,  sb.charAt(i));
				} else {
					if (sb.charAt(i) == '0') {
						// 小数点の最初のゼロは読み飛ばす
						;
					} else if (sb.charAt(i) == '.') {
						// 小数点部位が全てゼロならコンマは出力しない
						commaFlag = true;
					} else {
						commaFlag = true;
						sbOut.insert(0,  sb.charAt(i));
					}
				}
			}
			sb = sbOut;
		}
		System.out.println(sb);
		txt.setText(sb);
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
		for (int i=0; i<displayChar.size(); i++) {
			String str = displayChar.get(i);
			sb.append(str);
		}
		// コンマの位置を判定
		if (commaMode && decimalPlaces > 0) {
			sb.insert(sb.length() - decimalPlaces, ".");
		}
		// マイナス記号を追加
		if (minus) {
			sb.insert(0,  "-");
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
		String numberString = String.format(formatStr.toString(), Math.abs(d));
		// 数値を文字列かしてスタックに追加
		for (int i=0; i<numberString.length(); i++) {
			char chr = numberString.charAt(i);
			if (chr != '.') {
				displayChar.push(String.valueOf(chr));
				if (commaMode) {
					decimalPlaces++;
				}
			} else {
				commaMode = true;
			}
			// 桁数が表示桁数を超える部分は入れない
			if (displayChar.size() >= DISPLAY_DIGIT) {
				break;
			}
		}
		// 符号を追加
		if (d < 0) {
			minus = true;
		}
	}

	@Override
	public void setError() {
	}

	@Override
	public void clearError() {
	}

}
