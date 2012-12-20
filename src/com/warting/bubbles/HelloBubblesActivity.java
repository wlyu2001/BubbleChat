package com.warting.bubbles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.warting.bubbles.PullToRefreshListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import de.svenjacobs.loremipsum.LoremIpsum;

public class HelloBubblesActivity extends Activity {
	private com.warting.bubbles.DiscussArrayAdapter adapter;
	private PullToRefreshListView lv;
	private LoremIpsum ipsum;
	//private EditText editText1;
	private static Random random;
	
	Bitmap imageLeft;
	Bitmap imageRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discuss);
		random = new Random();
		ipsum = new LoremIpsum();
		
		imageLeft=BitmapFactory.decodeStream(getResources().openRawResource(R.raw.left));
		imageRight=BitmapFactory.decodeStream(getResources().openRawResource(R.raw.right));
		lv = (PullToRefreshListView) findViewById(R.id.listView1);
		lv.setOnRefreshListener(new OnRefreshListener(){
            public void onRefresh() {
            	
            	addItems();
            }
		});

		adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);

		lv.setAdapter(adapter);
		
		adapter.add(new OneComment(false, "Hallo bubbles", imageRight));

//		editText1 = (EditText) findViewById(R.id.editText1);
//		editText1.setOnKeyListener(new OnKeyListener() {
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// If the event is a key-down event on the "enter" button
//				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//					// Perform action on key press
//					adapter.add(new OneComment(false, editText1.getText().toString(), image));
//					editText1.setText("");
//					return true;
//				}
//				return false;
//			}
//		});

		addItems();
	}

	private void addItems() {
//		adapter.add(new OneComment(true, "Hello bubbles!", image));
//
//		for (int i = 0; i < 10000; i++) {
//			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
//			int word = getRandomInteger(1, 10);
//			int start = getRandomInteger(1, 40);
//			String words = ipsum.getWords(word, start);
//
//			adapter.add(new OneComment(left, words, image));
//		}
		String url="http://bubble-chat.appspot.com/bubblechat1";
		message.clear();
		new DownloadFilesTask().execute(url, null, null);
		  

	}
	
	ArrayList<Message> message=new ArrayList<Message>();
	
    private class DownloadFilesTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
        	InputStream content = null;
  		  try {
  		    HttpClient httpclient = new DefaultHttpClient();
  		    HttpResponse response = httpclient.execute(new HttpGet(urls[0]));
  		    content = response.getEntity().getContent();
  		  } catch (Exception e) {
  			  System.out.println(e.getMessage());
  		  }
  		  
  		  Gson gson = new Gson();
  		  Reader reader=new InputStreamReader(content);
  		  
  		  Type type = new TypeToken<Collection<Message>>(){}.getType();
  		  
  		  
  		  message=gson.fromJson(reader, type);
  		  
  		  return "";
        }

        protected void onProgressUpdate(Void... progress) {

        }
        

        protected void onPostExecute(String result) {
          
  		  for(Message msg:message){
  			  if(msg.left)
  				  adapter.add(new OneComment(msg.left, msg.text, imageLeft));
  			  else
  				  adapter.add(new OneComment(msg.left, msg.text, imageRight));
		  }
  		  lv.onRefreshComplete();
  		  lv.setSelection(49);
  		  
  		  super.onPostExecute(result);


        }
    }

	private static int getRandomInteger(int aStart, int aEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}

}