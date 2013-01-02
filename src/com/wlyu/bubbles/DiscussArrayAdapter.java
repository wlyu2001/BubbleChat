package com.wlyu.bubbles;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscussArrayAdapter extends ArrayAdapter<OneComment> {

	private List<OneComment> comments = new ArrayList<OneComment>();
	private static ImageFetcher imageFetcher;
	

	
	public void add(int ind, OneComment object) {
		comments.add(ind,object);
		super.add(object);
	}
	@Override
	public void add(OneComment object) {
		comments.add(0,object);
		super.add(object);
	}

	public DiscussArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
        if (imageFetcher == null) {
        	imageFetcher = new ImageFetcher();
        }
        imageFetcher.setListener(this);
	}

	public int getCount() {
		return this.comments.size();
	}

	public OneComment getItem(int index) {
		return this.comments.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		

		View row = convertView;
		ViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.wrapper=(LinearLayout) row.findViewById(R.id.wrapper);
			viewHolder.text=(TextView) row.findViewById(R.id.comment);
			viewHolder.background=(LinearLayout) row.findViewById(R.id.background);
			viewHolder.imageViewLeft = (ImageView) row.findViewById(R.id.avatarLeft);
			viewHolder.imageViewRight = (ImageView) row.findViewById(R.id.avatarRight);
			viewHolder.imageRow=(LinearLayout) row.findViewById(R.id.image_row);
			
			row.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)row.getTag();
		}


		OneComment coment = getItem(position);

		if(coment!=null){

			viewHolder.text.setText(coment.comment);
			viewHolder.imageRow.removeAllViews();
			
			for(String uid:coment.attachments.keySet()){
				ImageView imageView=new ImageView(this.getContext());
				
				LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(vp);
				
				imageView.setImageBitmap(imageFetcher.getImage(uid, "http://bubble-chat.appspot.com"+ coment.attachments.get(uid)));
				imageView.setVisibility(View.VISIBLE);
				viewHolder.imageRow.addView(imageView);
				
			}

	
			if(coment.left){
				viewHolder.imageViewRight.setImageBitmap(null);
				viewHolder.imageViewLeft.setImageBitmap(coment.image);
			}else{			
				viewHolder.imageViewRight.setImageBitmap(coment.image);
				viewHolder.imageViewLeft.setImageBitmap(null);
			}

			viewHolder.background.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
			viewHolder.wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);
		}

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
    static class ViewHolder {
    	LinearLayout wrapper;
        ImageView imageViewLeft;
        ImageView imageViewRight;
        TextView text;
        LinearLayout background;
        LinearLayout imageRow;
    }

}