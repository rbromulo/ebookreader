package com.aujur.ebookreader.dto;

import java.util.List;

public class SearchResultWraper {

	private List<SearchResult> searchResults;

	public void setSearchResult(List<SearchResult> searchResults) {
		this.searchResults = searchResults;
	}

	public List<SearchResult> getSearchResults() {
		return this.searchResults;
	}
}
