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

import java.io.InputStream;
import java.net.URL;

import net.nightwhistler.nucular.atom.AtomConstants;
import net.nightwhistler.nucular.atom.Entry;
import net.nightwhistler.nucular.atom.Feed;
import net.nightwhistler.nucular.atom.Link;
import net.nightwhistler.nucular.parser.Nucular;
import net.nightwhistler.nucular.parser.opensearch.SearchDescription;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetManager;

import com.aujur.ebookreader.Configuration;
import com.aujur.ebookreader.R;
import com.aujur.ebookreader.scheduling.QueueableAsyncTask;
import com.google.inject.Inject;

public class LoadLocalOPDSTask extends QueueableAsyncTask<String, Object, Feed> {

	private static final Logger LOG = LoggerFactory.getLogger("LoadLocalOPDSTask");

	private Configuration config;
	private Context context;

	private HttpClient httpClient;

	private LoadFeedCallback callBack;

	private String errorMessage;
	private boolean asDetailsFeed;
	private boolean asSearchFeed;

	private LoadFeedCallback.ResultType resultType;

	@Inject
	LoadLocalOPDSTask(Context context, Configuration config, HttpClient httpClient) {
		this.context = context;
		this.config = config;
		this.httpClient = httpClient;
	}

	@Override
	protected void onPreExecute() {
		callBack.onLoadingStart();
	}

	@Override
	public void requestCancellation() {
		LOG.debug("Got cancel request");
		super.requestCancellation();
	}

	@Override
	protected Feed doInBackground(String... params) {

		String baseUrl = params[0];

		if (baseUrl == null || baseUrl.trim().length() == 0) {
			return null;
		}

		boolean isBaseFeed = baseUrl.equals(config.getBaseOPDSFeed());

		baseUrl = baseUrl.trim();

		try {

			AssetManager assetManager = context.getAssets();

			InputStream stream = assetManager.open(baseUrl);
			Feed feed = Nucular.readAtomFeedFromStream(stream);
			feed.setURL(baseUrl);
			feed.setDetailFeed(asDetailsFeed);
			feed.setSearchFeed(asSearchFeed);

			for (Entry entry : feed.getEntries()) {
				entry.setBaseURL(baseUrl);
			}

			if (isBaseFeed) {
				addCustomSitesEntry(feed);
			}

			if (isCancelled()) {
				return null;
			}

			Link searchLink = feed.findByRel(AtomConstants.REL_SEARCH);

			if (searchLink != null) {
				URL mBaseUrl = new URL(baseUrl);
				URL mSearchUrl = new URL(mBaseUrl, searchLink.getHref());
				searchLink.setHref(mSearchUrl.toString());

				LOG.debug("Got searchLink of type " + searchLink.getType()
						+ " with href=" + searchLink.getHref());

				/*
				 * Some sites report the search as OpenSearch, but still have
				 * the searchTerms in the URL. If the URL already contains
				 * searchTerms, we ignore the reported type and treat it as Atom
				 */
				if (searchLink.getHref().contains(AtomConstants.SEARCH_TERMS)) {
					searchLink.setType(AtomConstants.TYPE_ATOM);
				}

				if (AtomConstants.TYPE_OPENSEARCH.equals(searchLink.getType())) {

					String searchURL = searchLink.getHref();

					LOG.debug("Attempting to download OpenSearch description from "
							+ searchURL);

//					try {
//						currentRequest = new HttpGet(searchURL);
//						InputStream searchStream = httpClient
//								.execute(currentRequest).getEntity()
//								.getContent();
//
//						SearchDescription desc = Nucular
//								.readOpenSearchFromStream(searchStream);
//
//						if (desc.getSearchLink() != null) {
//							searchLink.setHref(desc.getSearchLink().getHref());
//							searchLink.setType(AtomConstants.TYPE_ATOM);
//						}
//					} catch (Exception searchIO) {
//						LOG.error("Could not get search info", searchIO);
//					}
				}

				LOG.debug("Using searchURL " + searchLink.getHref());

			}

			return feed;
		} catch (Exception e) {
			this.errorMessage = e.getLocalizedMessage();
			LOG.error("Download failed for url: " + baseUrl, e);
			return null;
		}

	}

	void setResultType(LoadFeedCallback.ResultType type) {
		this.resultType = type;
	}

	public LoadLocalOPDSTask setCallBack(LoadFeedCallback callBack) {
		this.callBack = callBack;
		return this;
	}

	public void setAsDetailsFeed(boolean asDetailsFeed) {
		this.asDetailsFeed = asDetailsFeed;
	}

	public void setAsSearchFeed(boolean asSearchFeed) {
		this.asSearchFeed = asSearchFeed;
	}

	@Override
	protected void doOnPostExecute(Feed result) {
		if (result == null) {
			callBack.errorLoadingFeed(errorMessage);
		} else if (result.getSize() == 0) {
			callBack.emptyFeedLoaded(result);
		} else {
			callBack.setNewFeed(result, resultType);
		}
	}

	private void addCustomSitesEntry(Feed feed) {

		Link iconLink = feed.findByRel("pageturner:custom_sites");

		Entry entry = new Entry();
		entry.setTitle(context.getString(R.string.custom_site));
		entry.setSummary(context.getString(R.string.custom_site_desc));
		entry.setId(Catalog.CUSTOM_SITES_ID);
		entry.setBaseURL(feed.getURL());

		if (iconLink != null) {
			Link thumbnailLink = new Link(iconLink.getHref(),
					iconLink.getType(), AtomConstants.REL_IMAGE, null);
			entry.addLink(thumbnailLink);
		}

		feed.addEntry(entry);
	}

}
