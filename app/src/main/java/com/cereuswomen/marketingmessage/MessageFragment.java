package com.cereuswomen.marketingmessage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kim on 10/15/2015.
 */
public class MessageFragment extends Fragment {
    //public static final String EXTRA_LAYOUT = "EXTRA_LAYOUT";

    public static final MessageFragment newInstance() {
        //TODO: After I figure out how I want to store the user, pass the user in here from main activity.)
        MessageFragment f = new MessageFragment();
//        Bundle bdl = new Bundle(1);
//        bdl.putInt(EXTRA_LAYOUT, layout);
//        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //int newLayout = getArguments().getInt(EXTRA_LAYOUT);
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        //TODO: Set User and grab any message data to be entered into the fields.


        return v;

    }


}
