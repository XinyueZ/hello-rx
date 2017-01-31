package com.hellorx.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xzhao on 31.01.17.
 */
public class Hello {
	@SerializedName("id") private int mId;
	@SerializedName("content") private String mContent;


	public Hello(int id, String content) {
		mId = id;
		mContent = content;
	}


	public int getId() {
		return mId;
	}

	public String getContent() {
		return mContent;
	}
}
