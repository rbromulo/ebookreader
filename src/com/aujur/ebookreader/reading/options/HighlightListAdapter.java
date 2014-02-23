/*
 * Copyright (C) 2014 Romulo Bittencourt <rbromulo@gmail.com>
 *
 * 
 * This file is part of AuJur E-Book Reader
 *
 * AuJur E-Book Reader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AuJur E-Book Reader.  If not, see <http://www.gnu.org/licenses/>.*
 */

package com.aujur.ebookreader.reading.options;

import java.util.List;

import net.nightwhistler.nucular.atom.Entry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.dto.TocEntry;
import com.google.inject.Inject;

public class HighlightListAdapter extends BaseAdapter {

	private List<TocEntry> tocEntry;
	private Context context;

	private Entry loadingEntry = new Entry();

	@Inject
	public HighlightListAdapter(Context context) {
		tocEntry = ReadingFragment.getBookViewWraper().getBookView()
				.getTableOfContents();
		this.context = context;
	}

	@Override
	public int getCount() {

		if (tocEntry == null) {
			return 0;
		}

		return tocEntry.size();
	}

	@Override
	public TocEntry getItem(int position) {
		if (position >= 0 && position < tocEntry.size()) {
			return tocEntry.get(position);
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.fragment_index_row, parent,
					false);
		} else {
			rowView = convertView;
		}

		TocEntry item = getItem(position);
		((TextView) rowView.findViewById(R.id.toc_title)).setText(item
				.getTitle());

		return rowView;

	}

}
