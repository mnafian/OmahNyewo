package com.inagata.omahnyewo.page;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.inagata.omahnyewo.R;
import com.inagata.omahnyewo.adapter.OmhItemListAdapter;
import com.inagata.omahnyewo.base.OmhDetailActivity;
import com.inagata.omahnyewo.base.OmhStatic;
import com.inagata.omahnyewo.service.GifAnimationDrawable;
import com.inagata.omahnyewo.service.OmhGpsService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class OmhLoaderInfoWebService extends ListFragment {

    public static ImageView loaderInfo;
    public static int topPosition;
    public ArrayList<HashMap<String, String>> listDetail;
    private BaseAdapter mAdapter;
    private GifAnimationDrawable little;
    private String keyCode;

    private OmhGpsService gpsService;
    private LatLng locationUser;
    private String link_url;
    private Boolean statusRefresh = false;

    private ArrayList<HashMap<String, String>> omhList = new ArrayList<HashMap<String, String>>();
    private OkHttpClient clientRest = new OkHttpClient();

    private SwipeRefreshLayout swipeView = null;
    private Handler handler = new Handler();
    private final Runnable refreshing = new Runnable() {
        public void run() {
            try {
                // TODO : isRefreshing should be attached to your data request
                // status
                if (statusRefresh == true) {
                    // re run the verification after 1 second
                    handler.postDelayed(this, 5000);
                } else {
                    // stop the animation after the data is fully loaded
                    swipeView.setRefreshing(false);
                    omhList.clear();
                    new LoadDataOmh().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public OmhLoaderInfoWebService(String id) {
        keyCode = id;
    }

    public static boolean checkConnection(Context context) {

        final ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else
            return false;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsService = new OmhGpsService(getActivity());
        if (gpsService.canGetLocation()) {
            double latitude = gpsService.getLatitude();
            double longitude = gpsService.getLongitude();
            locationUser = new LatLng(latitude, longitude);

            link_url = OmhStatic.URL_WS + keyCode + "&lat="
                    + locationUser.latitude + "&long=" + locationUser.longitude;

            setLink_url(link_url);
        } else {
            gpsService.showSettingAlert();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.omh_list_base, container, false);
        loaderInfo = (ImageView) view.findViewById(R.id.loaderImage);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipeView);
        listDetail = new ArrayList<HashMap<String, String>>();

        new LoadDataOmh().execute();

        swipeView.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeView
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeView.setRefreshing(true);
                        handler.post(refreshing);
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                int topRowVerticalPosition = (getListView() == null || getListView()
                        .getChildCount() == 0) ? 0 : getListView()
                        .getChildAt(0).getTop();
                swipeView.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        //Toast.makeText(getActivity(), "Diklik di posisi= " + position,
        //		Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
        Intent sendToDetail = new Intent(getActivity(),
                OmhDetailActivity.class);
        LatLng positionMap;
        Bundle args = new Bundle();
        String key = listDetail.get(position).get(OmhStatic.KEY).toString();
        String llat = listDetail.get(position).get(OmhStatic.LAT).toString();
        String llong = listDetail.get(position).get(OmhStatic.LONG).toString();
        String desc = listDetail.get(position).get(OmhStatic.DESCRIPTION)
                .toString();
        String image = listDetail.get(position).get(OmhStatic.IMAGE).toString();
        String price = listDetail.get(position).get(OmhStatic.PLACE_PRICE)
                .toString();
        String address = listDetail.get(position).get(OmhStatic.LOCATION)
                .toString();

        positionMap = new LatLng(Double.parseDouble(llat),
                Double.parseDouble(llong));
        args.putParcelable("place_position", positionMap);
        sendToDetail.putExtra(OmhStatic.COORDINAT, args);
        sendToDetail.putExtra(OmhStatic.KEY, key);
        sendToDetail.putExtra(OmhStatic.DESCRIPTION, desc);
        sendToDetail.putExtra(OmhStatic.IMAGE, image);
        sendToDetail.putExtra(OmhStatic.PLACE_PRICE, price);
        sendToDetail.putExtra(OmhStatic.LOCATION, address);


        startActivity(sendToDetail);

    }

    protected boolean isRefreshing(Boolean refresh) {
        statusRefresh = refresh;
        return statusRefresh;
    }

    private void changeFragment(Fragment targetFragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @SuppressLint("NewApi")
    public class LoadDataOmh extends
            AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Toast.makeText(getActivity(), getLink_url(), Toast.LENGTH_LONG)
                    .show();
            Log.d("Link URL", getLink_url());

            try {
                little = new GifAnimationDrawable(getResources()
                        .openRawResource(R.raw.preloader_1));
                little.setOneShot(false);
                loaderInfo.setImageDrawable(little);
                little.setVisible(true, true);
            } catch (IOException e) {
                Log.e("error opening", e.getMessage());
            }
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... arg0) {
            try {

                Request request = new Request.Builder().url(getLink_url())
                        .build();
                Log.d("Request", request.toString());
                Response response = clientRest.newCall(request).execute();
                String responsedata = response.body().string();

                JSONArray callData = new JSONArray(responsedata);
                Log.d("Jumlah Data", String.valueOf(callData.length()));
                if (callData != null) {
                    for (int i = 0; i < callData.length(); i++) {
                        JSONObject objData = callData.getJSONObject(i);
                        String namePlace = objData
                                .getString(OmhStatic.PLACE_NAME);
                        String namePrice = objData
                                .getString(OmhStatic.PLACE_PRICE);
                        String catPlace = objData.getString(OmhStatic.CATEGORY);
                        String keyPlace = objData.getString(OmhStatic.KEY);
                        String locationPlace = objData
                                .getString(OmhStatic.LOCATION);
                        String descriptionPlace = objData
                                .getString(OmhStatic.DESCRIPTION);
                        String latPlace = objData.getString(OmhStatic.LAT);
                        String longPlace = objData.getString(OmhStatic.LONG);
                        String imagePlace = objData.getString(OmhStatic.IMAGE);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(OmhStatic.PLACE_NAME, namePlace);
                        map.put(OmhStatic.PLACE_PRICE, namePrice);
                        map.put(OmhStatic.CATEGORY, catPlace);
                        map.put(OmhStatic.KEY, keyPlace);
                        map.put(OmhStatic.CATEGORY, catPlace);
                        map.put(OmhStatic.LOCATION, locationPlace);
                        map.put(OmhStatic.DESCRIPTION, descriptionPlace);
                        map.put(OmhStatic.LAT, latPlace);
                        map.put(OmhStatic.LONG, longPlace);
                        map.put(OmhStatic.IMAGE, imagePlace);

                        omhList.add(map);
                        listDetail.add(map);
                    }
                }

            } catch (JSONException e) {
                Log.e("errJSON", e.getMessage());
            } catch (IOException e) {

            }
            return omhList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.size() != 0) {
                OmhLoaderInfoWebService.loaderInfo.setImageAlpha(0);
                mAdapter = new OmhItemListAdapter(getActivity()
                        .getApplicationContext(), result);
                getListView().setAdapter(mAdapter);
            } else {
                isRefreshing(true);
            }
        }

    }
}
