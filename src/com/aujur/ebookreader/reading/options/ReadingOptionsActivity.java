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

import roboguice.inject.InjectFragment;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.PageTurnerActivity;
import com.aujur.ebookreader.activity.ReadingFragment;

public class ReadingOptionsActivity extends PageTurnerActivity {

	@InjectFragment(R.id.fragment_mainoptions)
	private ReadingOptionsFragment readingOptionsFragment;

	@Override
	protected void onCreatePageTurnerActivity(Bundle savedInstanceState) {
		super.onCreatePageTurnerActivity(savedInstanceState);

		ActionBar ab = getSupportActionBar();

		ab.setTitle(ReadingFragment.getBookViewWraper().getBookView().getBook()
				.getTitle());

		// TODO: you can show author's name
		// ab.setSubtitle((CharSequence) ReadingFragment.getBookViewWraper()
		// .getBookView().getBook().getMetadata().getAuthors().get(0));

		Bundle extras = getIntent().getExtras();
		int selectedTab = 0;
		if (extras != null) {
			selectedTab = extras.getInt("SELECTED_TAB");
		}

		if (this.readingOptionsFragment != null) {
			this.readingOptionsFragment.getPager().setCurrentItem(selectedTab);
		}

	}

	@Override
	protected int getMainLayoutResource() {
		return R.layout.activity_mainoptions;
	}

}