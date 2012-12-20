package com.warting.bubbles;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class OneComment {
	public boolean left;
	public String comment;
	public Bitmap  image;

	public OneComment(boolean left, String comment, Bitmap  image) {
		super();
		this.left = left;
		this.comment = comment;
		this.image=image;
	}

}