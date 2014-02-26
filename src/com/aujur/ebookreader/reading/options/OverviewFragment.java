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

import java.io.IOException;

import nl.siegmann.epublib.domain.Book;
import roboguice.fragment.RoboFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.view.FastBitmapDrawable;

public class OverviewFragment extends RoboFragment {

	public static OverviewFragment newInstance() {
		OverviewFragment f = new OverviewFragment();
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

		return (View) inflater.inflate(R.layout.fragment_overview, container,
				false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Book book = ReadingFragment.getBookViewWraper().getBookView().getBook();

		if (book != null) {

			ImageView coverView = (ImageView) view
					.findViewById(R.id.coverImage);

			if (book.getCoverImage() != null) {

				Drawable coverDrawable = getCover(book);

				if (coverDrawable != null) {
					coverView.setImageDrawable(coverDrawable);
				} else {
					coverView.setImageDrawable(getResources().getDrawable(
							R.drawable.unknown_cover));
				}
			}

		}

		TextView titleView = (TextView) view.findViewById(R.id.titleField);
		TextView authorView = (TextView) view.findViewById(R.id.authorField);
		// TextView lastRead = (TextView) view.findViewById(R.id.lastRead);
		// TextView added = (TextView) view.findViewById(R.id.addedToLibrary);
		TextView descriptionView = (TextView) view
				.findViewById(R.id.bookDescription);
		// TextView fileName = (TextView) view.findViewById(R.id.fileName);

		titleView.setText(book.getTitle());

		if (book.getMetadata().getAuthors().get(0) != null) {

			String authorText = String.format(getString(R.string.book_by), book
					.getMetadata().getAuthors().get(0).getFirstname()
					+ " "
					+ book.getMetadata().getAuthors().get(0).getLastname());
			authorView.setText(authorText);

		}
		
		// descriptionView.setText(spanner.fromHtml( book.getDescription()));

	}

	private FastBitmapDrawable getCover(Book book) {

		try {

			Bitmap bitmap = BitmapFactory.decodeByteArray(book.getCoverImage()
					.getData(), 0, book.getCoverImage().getData().length);

			FastBitmapDrawable drawable = new FastBitmapDrawable(bitmap);

			return drawable;

		} catch (OutOfMemoryError outOfMemoryError) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}