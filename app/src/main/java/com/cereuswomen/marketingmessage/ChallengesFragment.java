package com.cereuswomen.marketingmessage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kim on 10/16/2015.
 */
public class ChallengesFragment extends Fragment {

    public static final ChallengesFragment newInstance() {
        //TODO: After I figure out how I want to store the user, pass the user in here from main activity.)
        ChallengesFragment f = new ChallengesFragment();
//        Bundle bdl = new Bundle(1);
//        bdl.putInt(EXTRA_LAYOUT, layout);
//        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenges, container, false);

        return v;
    }


}
