/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aujur.ebookreader.reading.options;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.dto.TocEntry;

public class IndexFragment extends ListFragment {

	private IndexArrayAdapter mAdapter;

	public static IndexFragment newInstance() {
		IndexFragment f = new IndexFragment();
		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO: modify these fragment to show a sreen with a message that there
		// is no index
		// for book, or to show another screeen with the index list
		View view = (View) inflater.inflate(R.layout.fragment_index_listview,
				container, false);

		mAdapter = new IndexArrayAdapter(getActivity());
		setListAdapter(mAdapter);

		List<TocEntry> tocCallbacks = ReadingFragment.getBookViewWraper()
				.getBookView().getTableOfContents();

		if (tocCallbacks != null && !tocCallbacks.isEmpty()) {
			mAdapter.setData(ReadingFragment.getBookViewWraper().getBookView()
					.getTableOfContents());
		}

		mAdapter.notifyDataSetChanged();

		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		ReadingFragment
				.getBookViewWraper()
				.getBookView()
				.navigateTo(
						ReadingFragment.getBookViewWraper().getBookView()
								.getTableOfContents().get(position).getHref());

		getActivity().finish();

	}

}