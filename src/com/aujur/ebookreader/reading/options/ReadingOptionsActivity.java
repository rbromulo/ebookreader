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

import android.os.Bundle;

import com.aujur.ebookreader.R;
import com.aujur.ebookreader.activity.PageTurnerActivity;
import com.aujur.ebookreader.activity.ReadingFragment;
import com.aujur.ebookreader.dto.BookViewWraper;

public class ReadingOptionsActivity extends PageTurnerActivity {

	@Override
	protected void onCreatePageTurnerActivity(Bundle savedInstanceState) {

		System.out.println("");

		BookViewWraper b = ReadingFragment.getBookViewWraper();

		super.onCreatePageTurnerActivity(savedInstanceState);

	}

	@Override
	protected int getMainLayoutResource() {
		return R.layout.activity_mainoptions;
	}

}