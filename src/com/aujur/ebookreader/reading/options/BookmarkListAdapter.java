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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.TextUtil;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.bookmark.Bookmark;
import com.google.inject.Inject;

public class BookmarkListAdapter extends BaseAdapter {

	private Context context;

	private List<Bookmark> bookmarks;

	@Inject
	public BookmarkListAdapter(Context context) {
		this.context = context;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		if (bookmarks == null) {
			return 0;
		}

		return bookmarks.size();
	}

	@Override
	public Bookmark getItem(int position) {
		if (position >= 0 && position < bookmarks.size()) {
			return bookmarks.get(position);
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

			rowView = inflater.inflate(R.layout.fragment_highlight_row, parent,
					false);

		} else {
			rowView = convertView;
		}

		Bookmark item = getItem(position);
		((TextView) rowView.findViewById(R.id.highlight_title)).setText(item
				.getName());

		final String finalText = getHighlightLabel(item.getIndex(),
				item.getPosition(), null);

		((TextView) rowView.findViewById(R.id.highlight_subtitle))
				.setText(finalText);

		return rowView;

	}

	private String getHighlightLabel(int index, int position, String text) {

		final int totalNumberOfPages = ReadingFragment.getBookViewWraper()
				.getBookView().getTotalNumberOfPages();

		int percentage = ReadingFragment.getBookViewWraper().getBookView()
				.getPercentageFor(index, position);
		int pageNumber = ReadingFragment.getBookViewWraper().getBookView()
				.getPageNumberFor(index, position);

		String result = percentage + "%";

		if (pageNumber != -1) {
			result = String.format(context.getString(R.string.page_number_of),
					pageNumber, totalNumberOfPages) + " (" + percentage + "%)";
		}

		if (text != null && text.trim().length() > 0) {
			result += ": " + TextUtil.shortenText(text);
		}

		return result;
	}

}
