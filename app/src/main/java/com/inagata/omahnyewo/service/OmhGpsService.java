package com.inagata.omahnyewo.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class OmhGpsService extends Service implements LocationListener {

	private final Context _context;

	// cek apakah GPS aktif ?
	boolean isGPSEnable = false;
	// cek network aktif ?
	boolean isNetworkEnable = false;

	boolean canGetLocation = false;

	Location location;
	double latitude;
	double longitude;

	// GPS akan update ketika jarak sudah berubah lebih dari 10 meter
	private static final long MIN_JARAK_GPS_UPDATE = 10; // meter

	// GPS akan update pada waktu interval
	private static final long MIN_WAKTU_GPS_UPDATE = 1000 * 60 * 1;

	protected LocationManager locManager;

	public OmhGpsService(Context context) {
		_context = context;
		getLocation();
	}

	private Location getLocation() {

		try {
			locManager = (LocationManager) _context
					.getSystemService(LOCATION_SERVICE);

			// cek GPS status
			isGPSEnable = locManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// cek status koneksi
			isNetworkEnable = locManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnable && !isNetworkEnable) {
				// tidak ada koneksi ke GPS dan Jaringan
			} else {
				// bisa dapatkan lokasi
				canGetLocation = true;

				// cek apakah koneksi internet bisa ?
				if (isNetworkEnable) {
					// ambil posisi berdasarkan Network
					locManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE, this);
					if (locManager != null) {
						// ambil posisi terakhir user menggunakan Network
						location = locManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						// jika lokasi berhasil didapat
						if (location != null) {
							// ambil latitude
							latitude = location.getLatitude();
							// ambil longitude
							longitude = location.getLongitude();
						}
					}
				}

				// jika gps bisa digunakan
				if (isGPSEnable) {
					if (location == null) {
						// ambil posisi berdasar GPS
						locManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE,
								this);
						if (locManager != null) {
							// dapatkan posisi terakhir user menggunakan GPS
							location = locManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							// jika lokasi berhasil didapat
							if (location != null) {
								// ambil latitude
								latitude = location.getLatitude();
								// ambil longitude
								longitude = location.getLongitude();
							}
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getLatitude() {
		if (location != null)
			latitude = location.getLatitude();
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		if (location != null)
			longitude = location.getLongitude();
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettingAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);

		// title Alertnya
		alertDialog.setTitle("GPS Setting");
		// pesan alert
		alertDialog.setMessage("GPS tidak aktif. Mau masuk ke setting Menu ?");

		alertDialog.setPositiveButton("Setting",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						_context.startActivity(intent);

					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	public void stopUsingGPS() {
		if (locManager != null)
			locManager.removeUpdates(OmhGpsService.this);
	}
}
