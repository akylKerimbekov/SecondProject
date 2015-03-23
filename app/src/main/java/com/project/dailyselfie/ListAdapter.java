package com.project.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] name;
	private final Bitmap[] imageId;
	public ListAdapter(Activity context,
                       String[] name) {
		super(context, R.layout.list_single, name);
		this.context = context;
		this.name = name;
		this.imageId = getAllSelfies(name);
	}
	private Bitmap[] getAllSelfies(String[] name2) {
		int i=0;
		Bitmap[] temp = new Bitmap[10];
		for(String bitmapFileName : name2){
			temp[i++] = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/Selfies/"+bitmapFileName);
		}
		i=0;
		return temp;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_single, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(name[position]);
		imageView.setImageBitmap(imageId[position]);
		return rowView;
	}
}