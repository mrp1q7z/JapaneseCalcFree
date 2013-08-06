package com.yojiokisoft.japanesecalc;

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

public class MainActivity extends Activity {
	private Calc calc = new Calc();
	private SoundPool mSound;
	private int mSoundId;
	private String mSoundName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundName = SettingDao.getInstance().getClickSound();
		int resId = MyResource.getResourceIdByName(mSoundName, "raw");
		if (resId == 0) {
			mSoundId = 0;
		} else {
			mSoundId = mSound.load(getApplicationContext(), resId, 0);
		}

		ImageButton btnClear = (ImageButton) findViewById(R.id.clear);
		btnClear.setOnLongClickListener(mClearButtonClicked);

		ImageButton btnAllClear = (ImageButton) findViewById(R.id.allclear);
		btnAllClear.setOnLongClickListener(mAllClearButtonClicked);

		LinearLayout displayContainer = (LinearLayout) findViewById(R.id.displayContainer);
		calc.setDisplay(displayContainer, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		String newSoundName = SettingDao.getInstance().getClickSound();
		if (mSoundName.equals(newSoundName)) {
			return;
		}
		
		if (mSoundId != 0) {
			mSound.unload(mSoundId);
		}

		mSoundName = newSoundName;
		int resId = MyResource.getResourceIdByName(mSoundName, "raw");
		if (resId == 0) {
			mSoundId = 0;
		} else {
			mSoundId = mSound.load(getApplicationContext(), resId, 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.set_sound:
			Intent intent = new Intent(getApplicationContext(), SoundActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSound.release();
	}

	private OnLongClickListener mClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			calc.onButtonClear();
			return false;
		}
	};

	private OnLongClickListener mAllClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			calc.onButtonClearMemory();
			calc.onButtonAllClear();
			return false;
		}
	};

	public void onClickButton(View view) {
		if (mSoundId != 0) {
			mSound.stop(mSoundId);
			mSound.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
		}

		switch (view.getId()) {
		case R.id.zero:
			calc.onButtonNumber(Number.ZERO);
			break;
		case R.id.doublezero:
			calc.onButtonNumber(Number.DOUBLE_ZERO);
			break;
		case R.id.one:
			calc.onButtonNumber(Number.ONE);
			break;
		case R.id.two:
			calc.onButtonNumber(Number.TWO);
			break;
		case R.id.three:
			calc.onButtonNumber(Number.TREE);
			break;
		case R.id.four:
			calc.onButtonNumber(Number.FORE);
			break;
		case R.id.five:
			calc.onButtonNumber(Number.FIVE);
			break;
		case R.id.six:
			calc.onButtonNumber(Number.SIX);
			break;
		case R.id.seven:
			calc.onButtonNumber(Number.SEVEN);
			break;
		case R.id.eight:
			calc.onButtonNumber(Number.EIGHT);
			break;
		case R.id.nine:
			calc.onButtonNumber(Number.NINE);
			break;
		case R.id.plus:
			calc.onButtonOp(Operation.PLUS);
			break;
		case R.id.minus:
			calc.onButtonOp(Operation.MINUS);
			break;
		case R.id.times:
			calc.onButtonOp(Operation.TIMES);
			break;
		case R.id.divide:
			calc.onButtonOp(Operation.DIVIDE);
			break;
		case R.id.comma:
			calc.onButtonNumber(Number.COMMA);
			break;
		case R.id.allclear:
			calc.onButtonAllClear();
			break;
		case R.id.clear:
			calc.onButtonBackspace();
			break;
		case R.id.equal:
			calc.onButtonEquale();
			break;
		case R.id.sign:
			calc.changeSign();
			break;
		case R.id.percent:
			calc.onButtonPercent();
			break;
		case R.id.memoryPlus:
			calc.onButtonMemoryPlus();
			break;
		case R.id.memoryMinus:
			calc.onButtonMemoryMinus();
			break;
		case R.id.clearMemory:
			calc.onButtonClearMemory();
			break;
		case R.id.returnMemory:
			calc.onButtonReturnMemory();
			break;
		default:
			break;
		}
	}
}
