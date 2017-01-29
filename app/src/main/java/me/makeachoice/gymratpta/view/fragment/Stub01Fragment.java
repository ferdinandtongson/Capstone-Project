package me.makeachoice.gymratpta.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;

/**
 * Created by Usuario on 1/29/2017.
 */

public class Stub01Fragment extends MyFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stub_text, container, false);

        TextView tv = (TextView) v.findViewById(R.id.stub_txtTitle);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static Stub01Fragment newInstance(String text) {

        Stub01Fragment f = new Stub01Fragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}