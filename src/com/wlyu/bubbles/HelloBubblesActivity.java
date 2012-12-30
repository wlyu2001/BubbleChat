package com.wlyu.bubbles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wlyu.bubbles.PullToRefreshListView.OnRefreshListener;

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
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import de.svenjacobs.loremipsum.LoremIpsum;

public class HelloBubblesActivity extends Activity {
	private com.wlyu.bubbles.DiscussArrayAdapter adapter;
	private PullToRefreshListView lv;
	private EditText editText1;
	private Button buttonSend;
	
	Bitmap imageLeft;
	Bitmap imageRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discuss);
		
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
		

		editText1 = (EditText) findViewById(R.id.editText1);
		
		buttonSend=(Button) findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int count=adapter.getCount();
				adapter.add(count, new OneComment(false, editText1.getText().toString(), imageRight));
				lv.setSelection(count);
				editText1.setText("");
			}
		});
		

		addItems();
		
	}

	private void addItems() {
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
  		  
  	      lv.setSelection(message.size()-1);
  		  
  		  super.onPostExecute(result);

        }
    }


}