package com.aujur.ebookreader.reading.options;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.dto.TocEntry;

public class IndexArrayAdapter extends ArrayAdapter<TocEntry> {

	private final LayoutInflater mInflater;

	public IndexArrayAdapter(Context context) {

		super(context, R.layout.fragment_index_listview);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setData(List<TocEntry> data) {

		clear();

		if (data != null) {
			for (TocEntry appEntry : data) {
				add(appEntry);
			}
		}

	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;

		if (convertView == null) {
			view = mInflater
					.inflate(R.layout.fragment_index_row, parent, false);
		} else {
			view = convertView;
		}

		TocEntry item = getItem(position);
		((TextView) view.findViewById(R.id.toc_title)).setText(item.getTitle());

		return view;
	}

}
