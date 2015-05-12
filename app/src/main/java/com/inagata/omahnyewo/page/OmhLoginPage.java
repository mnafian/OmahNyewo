package com.inagata.omahnyewo.page;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inagata.omahnyewo.R;

public class OmhLoginPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.omh_login_page, container);
        return view;
    }
}
