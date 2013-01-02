package com.wlyu.bubbles;

import java.util.HashMap;

import android.graphics.Bitmap;

public class OneComment {
	public boolean left;
	public String comment;
	public Bitmap  image;
	public HashMap<String, String> attachments;

	public OneComment(boolean left, String comment, Bitmap  image, HashMap<String, String> attachments) {
		super();
		this.left = left;
		this.comment = comment;
		this.image=image;
		this.attachments=attachments;
	}

}