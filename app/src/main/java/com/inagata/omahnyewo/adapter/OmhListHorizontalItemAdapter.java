package com.inagata.omahnyewo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.inagata.omahnyewo.base.OmhStatic;
import com.inagata.omahnyewo.R;
import com.squareup.picasso.Picasso;

public class OmhListHorizontalItemAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private final ArrayList<HashMap<String, String>> urls;
	private HashMap<String, String> resultp = new HashMap<String, String>();
	private GoogleMap mMap;

	public OmhListHorizontalItemAdapter(Context context,
			ArrayList<HashMap<String, String>> items, GoogleMap mMap) {
		mContext = context;
		urls = items;
		this.mMap = mMap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageItem;

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.omh_view_item, parent, false);
		resultp = urls.get(position);

		imageItem = (ImageView) view.findViewById(R.id.imageGallery);
		imageItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					LatLng updLocation = new LatLng(Double.valueOf(urls
							.get(position).get(OmhStatic.LAT)), Double.valueOf(urls
							.get(position).get(OmhStatic.LONG)));
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							updLocation, 15));
			}
		});

		Picasso.with(mContext)
				.load(OmhStatic.IMAGE_URL_WS + resultp.get(OmhStatic.IMAGE))
				.placeholder(R.drawable.border_radius)
				.error(R.drawable.border_radius).fit().centerCrop()
				.into(imageItem);
		
		return view;
	}

}
