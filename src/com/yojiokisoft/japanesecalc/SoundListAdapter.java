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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * クリック音のアダプター
 */
public class SoundListAdapter extends ArrayAdapter<SoundEntity> {
	private List<SoundEntity> mItems = null;

	private class ViewHolder {
		TextView title;
		TextView description;
		RadioButton radioButton;
	}

	public SoundListAdapter(Context context, int resourceId, List<SoundEntity> list) {
		super(context, resourceId, list);

		this.mItems = list;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public SoundEntity getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_sound, null);

			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.description = (TextView) convertView.findViewById(R.id.description);
			viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final SoundEntity item = (SoundEntity) getItem(position);

		viewHolder.title.setText(item.title);
		viewHolder.description.setText(item.description);
		viewHolder.radioButton.setChecked(item.checked);

		viewHolder.radioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// 一旦全てのチェックをクリアする
				for (int i = 0; i < mItems.size(); i++) {
					mItems.get(i).checked = false;
				}

				// クリックした箇所のみチェックする
				item.checked = true;
				// アダプタ内容を即時反映する
				notifyDataSetChanged();
			}
		});

		return convertView;
	}
}