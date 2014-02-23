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

import roboguice.fragment.RoboFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.dto.TocEntry;

public class IndexFragment extends RoboFragment {

	public static IndexFragment newInstance() {
		IndexFragment f = new IndexFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (hasTableOfContentes()) {

			return (View) inflater.inflate(R.layout.fragment_index_listview,
					container, false);

		} else {

			return (View) inflater.inflate(R.layout.fragment_index_noitems,
					container, false);

		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (hasTableOfContentes()) {

			ListView indexList = (ListView) view
					.findViewById(R.id.indexList);

			IndexListAdapter adapter = new IndexListAdapter(
					getActivity());

			indexList.setAdapter(adapter);
			indexList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View arg1,
						int position, long arg3) {

					ReadingFragment
							.getBookViewWraper()
							.getBookView()
							.navigateTo(
									ReadingFragment.getBookViewWraper()
											.getBookView().getTableOfContents()
											.get(position).getHref());

					getActivity().finish();

				}
			});

		}
	}

	public boolean hasTableOfContentes() {

		List<TocEntry> tocEntries = ReadingFragment.getBookViewWraper()
				.getBookView().getTableOfContents();

		return tocEntries != null && !tocEntries.isEmpty();
	}

}