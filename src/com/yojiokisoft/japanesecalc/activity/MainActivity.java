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

package com.yojiokisoft.japanesecalc.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.yojiokisoft.japanesecalc.Calc;
import com.yojiokisoft.japanesecalc.MyUncaughtExceptionHandler;
import com.yojiokisoft.japanesecalc.Number;
import com.yojiokisoft.japanesecalc.Operation;
import com.yojiokisoft.japanesecalc.R;
import com.yojiokisoft.japanesecalc.dao.SettingDao;
import com.yojiokisoft.japanesecalc.utils.MyResource;

/**
 * メインアクティビティ
 */
public class MainActivity extends Activity {
	private Calc mCalc = new Calc();
	private SoundPool mSound;
	private int mSoundId;
	private String mCurrentSoundName;
	private String mCurrentSkinName;
	private LinearLayout mButtonContainer;

	/**
	 * 初期処理
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// キャッチされない例外をキャッチするデフォルトのハンドラを設定する
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mCurrentSoundName = SettingDao.getInstance().getClickSound();
		int resId = MyResource.getResourceIdByName(mCurrentSoundName, "raw");
		if (resId == 0) {
			mSoundId = 0;
		} else {
			mSoundId = mSound.load(getApplicationContext(), resId, 0);
		}

		ImageButton btnClear = (ImageButton) findViewById(R.id.clear);
		btnClear.setOnLongClickListener(mClearButtonClicked);

		ImageButton btnAllClear = (ImageButton) findViewById(R.id.allclear);
		btnAllClear.setOnLongClickListener(mAllClearButtonClicked);

		mButtonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
		mCurrentSkinName = SettingDao.getInstance().getSkin();
		resId = MyResource.getResourceIdByName(mCurrentSkinName);
		mButtonContainer.setBackgroundResource(resId);

		int orientation = getResources().getConfiguration().orientation;
		LinearLayout displayContainer = (LinearLayout) findViewById(R.id.displayContainer);
		mCalc.setDisplay(displayContainer, this, orientation);
	}

	/**
	 * 前面に表示された
	 */
	@Override
	protected void onResume() {
		super.onResume();

		String newSoundName = SettingDao.getInstance().getClickSound();
		if (!mCurrentSoundName.equals(newSoundName)) {
			if (mSoundId != 0) {
				mSound.unload(mSoundId);
			}

			mCurrentSoundName = newSoundName;
			int resId = MyResource.getResourceIdByName(mCurrentSoundName, "raw");
			if (resId == 0) {
				mSoundId = 0;
			} else {
				mSoundId = mSound.load(getApplicationContext(), resId, 0);
			}
		}

		String newSkinName = SettingDao.getInstance().getSkin();
		if (!mCurrentSkinName.equals(newSkinName)) {
			mCurrentSkinName = newSkinName;
			int resId = MyResource.getResourceIdByName(mCurrentSkinName);
			if (resId != 0) {
				mButtonContainer.setBackgroundResource(resId);
			}
		}
	}

	/**
	 * メニューの作成
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * メニューが選択された
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.set_skin:
			intent = new Intent(getApplicationContext(), SkinActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		case R.id.set_sound:
			intent = new Intent(getApplicationContext(), SoundActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * データの一時保存
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		String state = mCalc.getInstanceState();
		outState.putString("SAVE_NUMBER", state);
	}

	/**
	 * 一時保存されたデータを復元
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		String state = savedInstanceState.getString("SAVE_NUMBER");
		mCalc.restoreInstanceState(state);
	}

	/**
	 * 終了処理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSound.release();
	}

	/**
	 * クリアボタンの長押し
	 */
	private OnLongClickListener mClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			mCalc.onButtonClear();
			return false;
		}
	};

	/**
	 * オールクリアボタンの長押し
	 */
	private OnLongClickListener mAllClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			mCalc.onButtonClearMemory();
			mCalc.onButtonAllClear();
			return false;
		}
	};

	/**
	 * 電卓の各ボタンのクリック
	 * 
	 * @param view
	 */
	public void onClickButton(View view) {
		if (mSoundId != 0) {
			mSound.stop(mSoundId);
			mSound.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
		}

		switch (view.getId()) {
		case R.id.zero:
			mCalc.onButtonNumber(Number.ZERO);
			break;
		case R.id.doublezero:
			mCalc.onButtonNumber(Number.DOUBLE_ZERO);
			break;
		case R.id.one:
			mCalc.onButtonNumber(Number.ONE);
			break;
		case R.id.two:
			mCalc.onButtonNumber(Number.TWO);
			break;
		case R.id.three:
			mCalc.onButtonNumber(Number.TREE);
			break;
		case R.id.four:
			mCalc.onButtonNumber(Number.FORE);
			break;
		case R.id.five:
			mCalc.onButtonNumber(Number.FIVE);
			break;
		case R.id.six:
			mCalc.onButtonNumber(Number.SIX);
			break;
		case R.id.seven:
			mCalc.onButtonNumber(Number.SEVEN);
			break;
		case R.id.eight:
			mCalc.onButtonNumber(Number.EIGHT);
			break;
		case R.id.nine:
			mCalc.onButtonNumber(Number.NINE);
			break;
		case R.id.plus:
			mCalc.onButtonOp(Operation.PLUS);
			break;
		case R.id.minus:
			mCalc.onButtonOp(Operation.MINUS);
			break;
		case R.id.times:
			mCalc.onButtonOp(Operation.TIMES);
			break;
		case R.id.divide:
			mCalc.onButtonOp(Operation.DIVIDE);
			break;
		case R.id.comma:
			mCalc.onButtonNumber(Number.COMMA);
			break;
		case R.id.allclear:
			mCalc.onButtonAllClear();
			break;
		case R.id.clear:
			mCalc.onButtonBackspace();
			break;
		case R.id.equal:
			mCalc.onButtonEquale();
			break;
		case R.id.sign:
			mCalc.changeSign();
			break;
		case R.id.percent:
			mCalc.onButtonPercent();
			break;
		case R.id.memoryPlus:
			mCalc.onButtonMemoryPlus();
			break;
		case R.id.memoryMinus:
			mCalc.onButtonMemoryMinus();
			break;
		case R.id.clearMemory:
			mCalc.onButtonClearMemory();
			break;
		case R.id.returnMemory:
			mCalc.onButtonReturnMemory();
			break;
		default:
			break;
		}
	}
}
