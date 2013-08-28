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

package com.yojiokisoft.japanesecalcfree.utils;

import com.yojiokisoft.japanesecalcfree.App;

/**
 * 定数クラス.
 */
public class MyConst {
	/** アプリケーション名（英語） */
	public static final String APP_NAME = "JapaneseCalc";

	/** 設定キー：クリックサウンド */
	public static final String PK_CLICK_SOUND = "ClickSound";

	/** 設定キー：スキン */
	public static final String PK_SKIN = "Skin";

	/** バグファイル名(キャッチした) */
	public static final String BUG_CAUGHT_FILE = "bug_caught.txt";

	/** バグファイル名(キャッチされなかった) */
	public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";

	/** キャッチしたバグファイルのフルパス */
	public static String getCaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_CAUGHT_FILE);
	}

	/** キャッチされなかったバグファイルのフルパス */
	public static String getUncaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_UNCAUGHT_FILE);
	}
}
