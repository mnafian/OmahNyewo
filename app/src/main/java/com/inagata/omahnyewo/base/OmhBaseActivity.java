package com.inagata.omahnyewo.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.inagata.omahnyewo.R;
import com.inagata.omahnyewo.page.OmhAboutPage;
import com.inagata.omahnyewo.page.OmhLoaderInfoWebService;
import com.inagata.omahnyewo.page.OmhMainPage;
import com.inagata.omahnyewo.page.OmhMapsLoaderPage;
import com.inagata.omahnyewo.service.OmhGpsService;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

@SuppressLint("InflateParams")
public class OmhBaseActivity extends Activity implements View.OnClickListener {

	private ResideMenu resideMenu;
	private OmhBaseActivity mContext;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemRadar;
	private ResideMenuItem itemAbout;
	private TextView headerTittle;
	private PopupWindow popupWin;

	private OmhGpsService gpsService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.omh_frame_base);
		headerTittle = (TextView) findViewById(R.id.tittle_header);
		mContext = this;

		gpsService = new OmhGpsService(this);
		if (gpsService.canGetLocation()) {
			setUpMenuLoader();
			changeFragment(new OmhMainPage());
		} else {
			gpsService.showSettingAlert();
		}


	}

	private void setUpMenuLoader() {

		resideMenu = new ResideMenu(mContext);
		resideMenu.setBackground(R.drawable.bg2);
		resideMenu.attachToActivity(mContext);

		resideMenu.setScaleValue(0.6f);

		itemHome = new ResideMenuItem(mContext, R.drawable.ic_menu_home,
				"Beranda");
		itemRadar = new ResideMenuItem(mContext, R.drawable.ic_menu_radar,
				"Temukan");
		itemAbout = new ResideMenuItem(mContext, R.drawable.ic_menu_about,
				"Tentang");

		itemHome.setOnClickListener(mContext);
		itemRadar.setOnClickListener(mContext);
		itemAbout.setOnClickListener(mContext);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemRadar, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);

		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});

		findViewById(R.id.title_bar_right_menu).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				});
	}

	@Override
	public void onBackPressed() {
		if (OmhStatic.back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();
		else
			Toast.makeText(getBaseContext(), "Tekan Sekali Lagi Untuk Keluar",
					Toast.LENGTH_SHORT).show();
		OmhStatic.back_pressed = System.currentTimeMillis();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == itemHome) {
			changeFragment(new OmhMainPage());
			headerTittle.setText("OmahNyewo");
		} else if (view == itemRadar) {
			changeFragment(new OmhMapsLoaderPage());
			headerTittle.setText("Temukan");
		} else if (view == itemAbout) {
			changeFragment(new OmhAboutPage());
			headerTittle.setText("Tentang");
		}
		resideMenu.closeMenu();
	}

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	private void changeFragmentFlip(Fragment targetFragment) {
		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.card_flip_right_in,
						R.animator.card_flip_right_out,
						R.animator.card_flip_left_in,
						R.animator.card_flip_left_out)
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.commit();
	}

	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	public void launchFirst(View view) {
		changeFragmentFlip(new OmhLoaderInfoWebService("kontrakan"));
	}

	public void launchSecond(View view) {
		changeFragmentFlip(new OmhLoaderInfoWebService("kost"));
	}
}
