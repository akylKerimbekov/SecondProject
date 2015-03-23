package com.project.dailyselfie;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DailySelfie extends ActionBarActivity {
	public static final int cameraRequestCode = 123;
	public String mCurrentPhotoPath;
	public ListView list;
	LinearLayout linearLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), 200, pendingIntent);
	
		String[] name = getSelfiesTaken();
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ListAdapter(this,name));
		list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				linearLayout= new LinearLayout(getApplicationContext());
		        linearLayout.setOrientation(LinearLayout.VERTICAL);
		        linearLayout.setLayoutParams(new LayoutParams(
		                LayoutParams.MATCH_PARENT,
		                LayoutParams.MATCH_PARENT));
		        ImageView imageView = new ImageView(getApplicationContext());
		        String[] list = getSelfiesTaken();
		        Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Selfies/"+list[arg2]);
		        imageView.setImageBitmap(bmp);
		        imageView.setLayoutParams(new LayoutParams(
		        		LayoutParams.MATCH_PARENT,
		        		LayoutParams.WRAP_CONTENT));
		        linearLayout.addView(imageView);
		        setContentView(linearLayout);
		        
			}
		});
		
		
	}
	@Override
    public void onBackPressed(){
		if(linearLayout != null){
			linearLayout.setVisibility(View.GONE);
			linearLayout = null;
			setContentView(R.layout.activity_main);
			list = (ListView) findViewById(R.id.list);
			list.setAdapter(new ListAdapter(this,getSelfiesTaken()));
			list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					linearLayout= new LinearLayout(getApplicationContext());
			        linearLayout.setOrientation(LinearLayout.VERTICAL);
			        linearLayout.setLayoutParams(new LayoutParams(
			                LayoutParams.MATCH_PARENT,
			                LayoutParams.MATCH_PARENT));
			        ImageView imageView = new ImageView(getApplicationContext());
			        String[] list = getSelfiesTaken();
			        Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Selfies/"+list[arg2]);
			        imageView.setImageBitmap(bmp);
			        imageView.setLayoutParams(new LayoutParams(
			        		LayoutParams.MATCH_PARENT,
			        		LayoutParams.WRAP_CONTENT));
			        linearLayout.addView(imageView);
			        setContentView(linearLayout);
			        
				}
			});
		}
		else{
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(id == R.id.capture_selfie){
			//open camera 
			Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(photoFile != null){
				captureImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(captureImage, cameraRequestCode);
			}
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
	    if (resultCode == Activity.RESULT_OK && requestCode == cameraRequestCode) {
			String[] name = getSelfiesTaken();
			list.setAdapter(new ListAdapter(this,name));
			

	    }
	}
	
	private File createImageFile() throws IOException {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = new File(Environment.getExternalStorageDirectory().getPath()+"/Selfies");
	    if(!storageDir.exists()){
	    	storageDir.mkdir();
	    }
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	private String[] getSelfiesTaken(){
	    File storageDir = new File(Environment.getExternalStorageDirectory().getPath()+"/Selfies");
	    String[] temp;
	    temp = storageDir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if(new File(dir, filename).isDirectory())
					return false;
				
				return filename.endsWith(".jpg");
			}
		});
	    if(temp == null){
	    	return new String[]{};
	    }
	    return temp;   
	}
	
}
