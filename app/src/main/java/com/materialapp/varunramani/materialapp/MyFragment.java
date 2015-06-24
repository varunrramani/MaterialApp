package com.materialapp.varunramani.materialapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import network.VolleySingleton;

/**
 * Created by Varun Ramani on 2/21/2015.
 */
public class MyFragment extends Fragment {

    private TextView mFragmentTextView;
    public static MyFragment getInstance(int position){
        MyFragment myFragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.fragment_my, viewGroup, false);
        mFragmentTextView = (TextView) layout.findViewById(R.id.myFragmentTextView);
        Bundle bundle = getArguments();
        if(bundle != null){
            mFragmentTextView.setText("The page currently selected is "+bundle.getInt("position"));
        }

        /*//To get data from the network...
        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        //StringRequest Constructor ("METHOD", URL, Response on success, Response on Error)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://php.net/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "Response is: " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error is: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //To send the request
        requestQueue.add(stringRequest);*/

        return layout;
    }
}