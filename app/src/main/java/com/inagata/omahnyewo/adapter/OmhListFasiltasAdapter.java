package com.inagata.omahnyewo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.inagata.omahnyewo.R;
import com.inagata.omahnyewo.base.OmhStatic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class OmhListFasiltasAdapter extends BaseAdapter {

    private final ArrayList<HashMap<String, String>> urls;
    private Context mContext;
    private LayoutInflater inflater;
    private HashMap<String, String> resultp = new HashMap<String, String>();

    public OmhListFasiltasAdapter(Context context,
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ImageView imageItem;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.omh_view_item, parent, false);
        resultp = urls.get(position);

        imageItem = (ImageView) view.findViewById(R.id.imageGallery);

        Picasso.with(mContext)
                .load(OmhStatic.IMAGE_URL_WS + resultp.get(OmhStatic.IMAGE))
                .placeholder(R.drawable.border_radius)
                .error(R.drawable.border_radius).fit().centerCrop()
                .into(imageItem);

        return view;
    }

}
