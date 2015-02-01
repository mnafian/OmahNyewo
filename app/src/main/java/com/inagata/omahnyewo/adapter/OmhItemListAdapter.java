package com.inagata.omahnyewo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inagata.omahnyewo.R;
import com.inagata.omahnyewo.base.OmhStatic;
import com.squareup.picasso.Picasso;

public class OmhItemListAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater inflater;
	private final ArrayList<HashMap<String, String>> urls;
	private HashMap<String, String> resultp = new HashMap<String, String>();

	public OmhItemListAdapter(Context context,
			ArrayList<HashMap<String, String>> items) {
		mContext = context;
		urls = items;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView priceItem, alamatItem;
		ImageView imageItem;
s
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.omh_list_base_item, parent,
				false);
		resultp = urls.get(position);

		priceItem = (TextView) view.findViewById(R.id.price_list_item);
		alamatItem = (TextView) view.findViewById(R.id.alamat_item);
		imageItem = (ImageView) view.findViewById(R.id.image_item_launch);

		priceItem.setText("Rp "+resultp.get(OmhStatic.PLACE_PRICE)+" /Tahun");
		alamatItem.setText(resultp.get(OmhStatic.LOCATION));

		Picasso.with(mContext)
				.load(OmhStatic.IMAGE_URL_WS + resultp.get(OmhStatic.IMAGE))
				.placeholder(R.drawable.border_radius).error(R.drawable.border_radius).fit()
				.centerCrop().into(imageItem);
		return view;

	}

}
