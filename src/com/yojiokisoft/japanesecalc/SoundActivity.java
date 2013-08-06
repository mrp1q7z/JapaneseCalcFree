package com.yojiokisoft.japanesecalc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SoundActivity extends Activity {
	private ListView mListView;
	private SoundListAdapter mAdapter;
	private int mCheckedPosition = -1;
	private SoundPool mSound;
	private int[] mSoundId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound);

		mListView = (ListView) findViewById(R.id.sound_list);

		List<SoundEntity> list = getSoundList();
		mAdapter = new SoundListAdapter(this, 0, list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mSoundListItemClicked);

		mSound = new SoundPool(list.size(), AudioManager.STREAM_MUSIC, 0);
		mSoundId = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).resId == 0) {
				continue;
			}
			mSoundId[i] = mSound.load(getApplicationContext(), list.get(i).resId, 0);
		}
	}

	private List<SoundEntity> getSoundList() {
		List<SoundEntity> list = new ArrayList<SoundEntity>();

		SoundEntity sound = new SoundEntity();
		sound.title = "なし";
		sound.description = "無音";
		sound.resId = 0;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "仏壇";
		sound.description = "チーン";
		sound.resId = R.raw.mp_chiiin;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "水滴";
		sound.description = "ポチャ";
		sound.resId = R.raw.mp_drop;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "風";
		sound.description = "ヒュッ";
		sound.resId = R.raw.mp_hyu;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "電子音１";
		sound.description = "ピッィ";
		sound.resId = R.raw.mp_pi1;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "電子音２";
		sound.description = "ピッ。";
		sound.resId = R.raw.mp_pi2;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "木魚";
		sound.description = "ポク";
		sound.resId = R.raw.mp_poku;
		sound.checked = false;
		list.add(sound);

		sound = new SoundEntity();
		sound.title = "太鼓";
		sound.description = "ドン";
		sound.resId = R.raw.mp_taiko;
		sound.checked = false;
		list.add(sound);

		// 設定中のサウンドをチェック
		String resName = SettingDao.getInstance().getClickSound();
		int resId = MyResource.getResourceIdByName(resName, "raw");
		int clickSound = 0;
		for (int i = 0; i < list.size(); i++) {
			if (resId == list.get(i).resId) {
				clickSound = i;
				break;
			}
		}
		list.get(clickSound).checked = true;
		mCheckedPosition = clickSound;

		return list;
	}

	private OnItemClickListener mSoundListItemClicked = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (mCheckedPosition != -1) {
				mSound.stop(mSoundId[mCheckedPosition]);
				mAdapter.getItem(mCheckedPosition).checked = false;
			}
			mSound.play(mSoundId[position], 1.0F, 1.0F, 0, 0, 1.0F);
			mAdapter.getItem(position).checked = true;
			mCheckedPosition = position;

			// アダプタの内容を即時反映する
			mAdapter.notifyDataSetChanged();
		}
	};

	public void onOkButtonClicked(View view) {
		int resId = mAdapter.getItem(mCheckedPosition).resId;
		String resName = "none";
		if (resId != 0) {
			resName = getResources().getResourceEntryName(resId);
		}
		SettingDao.getInstance().setClickSound(resName);
		finish();
	}

	public void onCancelButtonClicked(View view) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSound.release();
	}
}
