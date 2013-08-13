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

package com.yojiokisoft.japanesecalc.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yojiokisoft.japanesecalc.App;
import com.yojiokisoft.japanesecalc.utils.MyConst;

/**
 * 設定情報のデータアクセス
 */
public class SettingDao {
	private static SettingDao mInstance = null;
	private static SharedPreferences mSharedPref = null;
	private static Context mContext;

	/**
	 * コンストラクタは公開しない
	 * インスタンスを取得する場合は、getInstanceを使用する.
	 */
	private SettingDao() {
	}

	/**
	 * インスタンスの取得.
	 * 
	 * @return SettingDao
	 */
	public static SettingDao getInstance() {
		if (mInstance == null) {
			mInstance = new SettingDao();
			mContext = App.getInstance().getAppContext();
			mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		return mInstance;
	}

	/**
	 * @return クリック音のリソース名
	 */
	public String getClickSound() {
		String val = mSharedPref.getString(MyConst.PK_CLICK_SOUND, "none");
		return val;
	}

	/**
	 * クリック音のセット
	 * 
	 * @param resName クリック音のリソース名
	 * @return true=正常終了
	 */
	public boolean setClickSound(String resName) {
		return mSharedPref.edit().putString(MyConst.PK_CLICK_SOUND, resName).commit();
	}

	/**
	 * @return スキンのリソース名
	 */
	public String getSkin() {
		String val = mSharedPref.getString(MyConst.PK_SKIN, "bg_image_01");
		return val;
	}

	/**
	 * スキンのセット
	 * 
	 * @param resName スキンのリソース名
	 * @return true=正常終了
	 */
	public boolean setSkin(String resName) {
		return mSharedPref.edit().putString(MyConst.PK_SKIN, resName).commit();
	}
}
