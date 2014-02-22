package com.aujur.ebookreader.dto;

import com.aujur.ebookreader.view.bookview.BookView;

public class BookViewWraper {

	private BookView bookView;

	public void setBookView(BookView booView) {
		this.bookView = booView;
	}

	public BookView getBookView() {
		return this.bookView;
	}
}
