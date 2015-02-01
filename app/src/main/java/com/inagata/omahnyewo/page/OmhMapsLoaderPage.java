package com.inagata.omahnyewo.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inagata.omahnyewo.R;
import com.inagata.omahnyewo.adapter.OmhListHorizontalItemAdapter;
import com.inagata.omahnyewo.base.OmhHorizontalListView;
import com.inagata.omahnyewo.base.OmhStatic;
import com.inagata.omahnyewo.service.OmhGpsService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class OmhMapsLoaderPage extends Fragment {
	
	private BaseAdapter mAdapter;
	private ImageButton mapButton;
	private GoogleMap mMap;
	private Marker home = null;
	private Boolean pos = true;
	private MapView mapView;
	
	private OmhHorizontalListView listview;

	private OmhGpsService gpsService;
	private LatLng locationUser;
	private String link_url;

	private OkHttpClient clientRest = new OkHttpClient();
	private ArrayList<HashMap<String, String>> omhListMap = new ArrayList<HashMap<String, String>>();

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gpsService = new OmhGpsService(getActivity());
		if (gpsService.canGetLocation()) {
			// ambil latitude dan longitude
			double latitude = gpsService.getLatitude();
			double longitude = gpsService.getLongitude();
			locationUser = new LatLng(latitude, longitude);

			link_url = OmhStatic.URL_WS + "&lat=" + locationUser.latitude
					+ "&long=" + locationUser.longitude;

			setLink_url(link_url);
		} else {
			// jika GPS tidak aktif
			gpsService.showSettingAlert();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.omh_maps_fragment, container,
				false);
		
		mapButton = (ImageButton) view.findViewById(R.id.myMapLocationButton);
		
		listview = (OmhHorizontalListView) view.findViewById(R.id.listview);
        
            mapButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (!OmhLoaderInfoWebService.checkConnection(getActivity())) {
                        Toast.makeText(getActivity(), "Tidak Ada Koneksi",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "Lokasi Mungkin Tidak Akurat, Aktifkan GPS Device Agar Hasil Bisa Maksimal",
                                Toast.LENGTH_LONG).show();
                        omhListMap.clear();
                        mMap.clear();
                        new LoadDataOmhMap().execute();
                        getCurrentPosition();
                    }
                }
            });
        
		MapsInitializer.initialize(getActivity());

		switch (GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity())) {
		case ConnectionResult.SUCCESS:
			mapView = (MapView) view.findViewById(R.id.mapContainer);
			mapView.onCreate(savedInstanceState);
			// Gets to GoogleMap from the MapView and does initialization stuff
			if (mapView != null) {
				setUpMapIfNeeded();
			}
			break;
		case ConnectionResult.SERVICE_MISSING:
			Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT)
					.show();
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			Toast.makeText(
					getActivity(),
					GooglePlayServicesUtil
							.isGooglePlayServicesAvailable(getActivity()),
					Toast.LENGTH_SHORT).show();
		}

		return view;
	}

	private void setUpMapIfNeeded() {

		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = mapView.getMap();
			mMap.setMyLocationEnabled(true);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					-0.029401, 116.746222), 3.0f));
			mMap.getUiSettings().setMyLocationButtonEnabled(false);
			// Check if we were successful in obtaining the map.
			Toast.makeText(getActivity(),
					"Klik Tombol Radar Untuk Melihat Lokasi Anda",
					Toast.LENGTH_LONG).show();
		}
	}

	private void getCurrentPosition() {
		pos = false;
		if (mMap != null) {
			mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location arg0) {

					LatLng currentPos = new LatLng(arg0.getLatitude(), arg0
							.getLongitude());
					if (home != null)
						home.remove();
					home = mMap.addMarker(new MarkerOptions()
							.position(currentPos)
							.title("Current Location")
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.slide_direction)));
					if (pos == false) {
						mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
								currentPos, 15));
					}
					pos = true;
				}
			});
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@SuppressLint("NewApi")
	public class LoadDataOmhMap extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(getActivity(), getLink_url(), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... arg0) {
			try {

				Request request = new Request.Builder().url(getLink_url())
						.build();
				Response response = clientRest.newCall(request).execute();
				String responsedata = response.body().string();

				JSONArray callData = new JSONArray(responsedata);
				if (callData != null) {
					for (int i = 0; i < callData.length(); i++) {
						JSONObject objData = callData.getJSONObject(i);
						String namePlace = objData
								.getString(OmhStatic.PLACE_NAME);
						String latPlace = objData.getString(OmhStatic.LAT);
						String longPlace = objData.getString(OmhStatic.LONG);
						String imagePlace = objData.getString(OmhStatic.IMAGE);

						HashMap<String, String> map = new HashMap<String, String>();

						map.put(OmhStatic.PLACE_NAME, namePlace);
						map.put(OmhStatic.LAT, latPlace);
						map.put(OmhStatic.LONG, longPlace);
						map.put(OmhStatic.IMAGE, imagePlace);

						omhListMap.add(map);
					}
				}

			} catch (JSONException e) {
				Log.e("errJSON", e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return omhListMap;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (int i = 0; i < result.size(); i++) {
				LatLng updLocation = new LatLng(Double.valueOf(result
						.get(i).get(OmhStatic.LAT)), Double.valueOf(result
						.get(i).get(OmhStatic.LONG)));
				Marker marker = mMap.addMarker(new MarkerOptions()
						.position(updLocation)
						.title(result.get(i).get(OmhStatic.PLACE_NAME))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.slide_direction)));
				marker.showInfoWindow();
				mAdapter = new OmhListHorizontalItemAdapter(getActivity()
						.getApplicationContext(), result, mMap);
			}
			
	        listview.setAdapter(mAdapter);
		}

	}

}
