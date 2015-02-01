package com.inagata.omahnyewo.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inagata.omahnyewo.R;
import com.squareup.picasso.Picasso;

public class OmhDetailActivity extends Activity {

	private TextView headerTittle;
	private String imageName;

	private String description;
	private String pricePlace;
	private String addressPlace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.omh_frame_detail);
		headerTittle = (TextView) findViewById(R.id.tittle_header);

		Bundle bundle = getIntent().getParcelableExtra(OmhStatic.COORDINAT);
		Intent in = getIntent();
		description = (String) in.getStringExtra(OmhStatic.DESCRIPTION);
		imageName = (String) in.getStringExtra(OmhStatic.IMAGE);
		pricePlace = (String) in.getStringExtra(OmhStatic.PLACE_PRICE);
		addressPlace = (String) in.getStringExtra(OmhStatic.LOCATION);

		changeFragment(new OmhDetailPage(imageName, pricePlace, addressPlace,
				description));
	}

	private void changeFragment(Fragment targetFragment) {
		getFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	public static class OmhDetailPage extends Fragment {

		private ImageView imageNameView;
		private TextView textPrice, textAddress, textdetail;

		private String nameImage, priceOemah, addressOemah, omah_desc;
		private String[] fasilitasOemah;

		public OmhDetailPage(String image, String price, String address,
				String detail) {
			// TODO Auto-generated constructor stub
			nameImage = image;
			priceOemah = price;
			addressOemah = address;
			omah_desc = detail;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.omh_detail_base, container,
					false);
			imageNameView = (ImageView) view.findViewById(R.id.image_detail);
			textPrice = (TextView) view.findViewById(R.id.text_price_detail);
			textAddress = (TextView) view.findViewById(R.id.text_alamat_detail);
			textdetail = (TextView) view.findViewById(R.id.detail_des);

			LoadImageDetail(nameImage, imageNameView);
			LoadDetailPage(priceOemah, addressOemah, omah_desc);
			return view;
		}

		private void LoadImageDetail(String image, ImageView targetImage) {
			Picasso.with(getActivity()).load(OmhStatic.IMAGE_URL_WS + image)
					.placeholder(R.drawable.border_radius)
					.error(R.drawable.border_radius).fit().centerCrop()
					.into(targetImage);
		}

		private void LoadDetailPage(String price, String address, String detail) {
			textPrice.setText("Rp " + price + " /Tahun");
			textAddress.setText(address);
			textdetail.setText(detail);
		}
	}
}
