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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.bookmark.Bookmark;
import com.aujur.ebookreader.bookmark.BookmarkDatabaseHelper;
import com.google.inject.Inject;

public class BookmarksFragment extends RoboFragment {

	@Inject
	private BookmarkDatabaseHelper bookmarkDatabaseHelper;

	public static BookmarksFragment newInstance() {
		BookmarksFragment f = new BookmarksFragment();
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

		if (hasBookmarks()) {

			return (View) inflater.inflate(R.layout.fragment_bookmark_listview,
					container, false);

		} else {

			return (View) inflater.inflate(R.layout.fragment_bookmark_noitems,
					container, false);

		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (hasBookmarks()) {

			ListView bookmarkList = (ListView) view
					.findViewById(R.id.bookmarkList);

			BookmarkListAdapter adapter = new BookmarkListAdapter(getActivity());

			adapter.setBookmarks(this.bookmarkDatabaseHelper
					.getBookmarksForFile(ReadingFragment.getBookViewWraper()
							.getBookView().getFileName()));

			bookmarkList.setAdapter(adapter);
			bookmarkList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view,
						int position, long arg3) {

					Bookmark bookmark = bookmarkDatabaseHelper
							.getBookmarksForFile(
									ReadingFragment.getBookViewWraper()
											.getBookView().getFileName()).get(
									position);

					ReadingFragment
							.getBookViewWraper()
							.getBookView()
							.navigateTo(bookmark.getIndex(),
									bookmark.getPosition());

					getActivity().finish();

				}
			});

			bookmarkList.setLongClickable(true);

			bookmarkList
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View view, int position, long arg3) {

							Bookmark bookmark = bookmarkDatabaseHelper
									.getBookmarksForFile(
											ReadingFragment.getBookViewWraper()
													.getBookView()
													.getFileName()).get(
											position);

							ReadingFragment
									.getBookViewWraper()
									.getBookView()
									.navigateTo(bookmark.getIndex(),
											bookmark.getPosition());

							ReadingFragment.getReadingFragmentWraper()
									.getReadingFragment()
									.onBookmarkClick(bookmark);

							getActivity().finish();

							return true;
						}
					});

		}
	}

	public boolean hasBookmarks() {

		List<Bookmark> bookmarks = this.bookmarkDatabaseHelper
				.getBookmarksForFile(ReadingFragment.getBookViewWraper()
						.getBookView().getFileName());

		return bookmarks != null && !bookmarks.isEmpty();
	}

}