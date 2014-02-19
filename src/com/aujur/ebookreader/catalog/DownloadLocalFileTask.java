/*
 * Copyright (C) 2013 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
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
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */
package com.aujur.ebookreader.catalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.aujur.ebookreader.Configuration;
import com.aujur.ebookreader.library.LibraryService;
import com.google.inject.Inject;

public class DownloadLocalFileTask extends AsyncTask<String, Long, String> {

	private File destFile;

	private Exception failure;

	private DownloadFileCallback callBack;

	@Inject
	private Context context;

	@Inject
	private Configuration config;

	@Inject
	private LibraryService libraryService;

	@Inject
	private HttpClient httpClient;

	private static final Logger LOG = LoggerFactory
			.getLogger("DownloadFileTask");

	public interface DownloadFileCallback {

		void onDownloadStart();

		void downloadSuccess(File destinationFile);

		void downloadFailed();

		void progressUpdate(long progress, long total, int percentage);
	}

	DownloadLocalFileTask() {
	}

	public void setCallBack(DownloadFileCallback callBack) {
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {
		callBack.onDownloadStart();
	}

	@Override
	protected String doInBackground(String... params) {

		try {

			String url = params[0];
			// url = "vademecum/Editora AuJur/Codigo Civil (1)/Codigo Civil - Editora AuJur.epub";
			// vademecum/Editora AuJur/Codigo Civil (1)/Codigo Civil - Editora AuJur.epub
			LOG.debug("Downloading: " + url);

			String fileName = url.substring(url.lastIndexOf('/') + 1);
			fileName = fileName.replaceAll("\\?|&|=", "_");

			File destFolder = new File(config.getDownloadsFolder());
			if (!destFolder.exists()) {
				destFolder.mkdirs();
			}

			/**
			 * Make sure we always store downloaded files as .epub, so they show
			 * up in scans later on.
			 */
			if (!fileName.endsWith(".epub")) {
				fileName = fileName + ".epub";
			}

			destFile = new File(destFolder, URLDecoder.decode(fileName));

			if (destFile.exists()) {
				destFile.delete();
			}

			// lenghtOfFile is used for calculating download progress
			AssetFileDescriptor fd = context.getAssets().openFd(url);

			long lenghtOfFile = fd.getLength();

			// this is where the file will be seen after the download
			FileOutputStream f = new FileOutputStream(destFile);

			try {
				// file input is from the url
				AssetManager assetManager = context.getAssets();
				InputStream in = assetManager.open(url);

				// here's the download code
				byte[] buffer = new byte[1024];
				int len1 = 0;
				long total = 0;

				while ((len1 = in.read(buffer)) > 0 && !isCancelled()) {

					// Make sure the user can cancel the download.
					if (isCancelled()) {
						return null;
					}

					total += len1;
					publishProgress(total, lenghtOfFile,
							(long) ((total * 100) / lenghtOfFile));
					f.write(buffer, 0, len1);
				}
			} finally {
				f.close();
			}

			if (!isCancelled()) {
				// FIXME: This doesn't belong here really...
				Book book = new EpubReader().readEpubLazy(
						destFile.getAbsolutePath(), "UTF-8");
				libraryService.storeBook(destFile.getAbsolutePath(), book,
						false, config.isCopyToLibrayEnabled());
			}

		} catch (Exception e) {
			LOG.error("Download failed.", e);
			this.failure = e;
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		callBack.progressUpdate(values[0], values[1], values[2].intValue());
	}

	@Override
	protected void onPostExecute(String unused) {
		if (!isCancelled() && failure == null) {
			callBack.downloadSuccess(destFile);
		} else if (failure != null) {
			callBack.downloadFailed();
		}
	}
}
