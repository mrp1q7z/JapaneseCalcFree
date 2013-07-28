package com.yojiokisoft.japanesecalc;

import android.app.Application;
import android.content.Context;

/**
 * 独自のアプリケーションクラス
 */
public class App extends Application {
	// 唯一のアプリケーションインスタンス
	private static App sInstance;

	// アプリケーションコンテキスト
	private static Context sAppContext;

	/**
	 * コンストラクタ
	 */
	public App() {
		sInstance = this;
	}

	/**
	 * @return 唯一のアプリケーションインスタンス
	 */
	public static App getInstance() {
		if (sAppContext == null) {
			sAppContext = sInstance.getApplicationContext();
		}
		return sInstance;
	}

	/**
	 * @return アプリケーションコンテキスト
	 */
	public Context getAppContext() {
		return sAppContext;
	}

	/**
	 * @return アプリケーションデータの保存領域のパス
	 */
	public String getAppDataPath() {
		return sAppContext.getFilesDir().toString();
	}
}