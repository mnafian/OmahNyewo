package com.inagata.omahnyewo.page;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inagata.omahnyewo.R;

/**
 * Created by mnafian on 5/12/15.
 */
public class OmhChooseOptionMaps extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.omh_maps_fragment, container,
                false);
        return view;
    }
}
